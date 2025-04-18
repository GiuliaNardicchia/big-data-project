import org.apache.spark.HashPartitioner
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK_SER
import org.apache.spark.util.SizeEstimator
import org.slf4j.LoggerFactory
import utils.{Commons, DistanceType}

/**
 * Main application to run the jobs.
 */
object MainApplication {

  private val numParams = 2
  private val datasetsPath = "datasets/big/"
  private val fileName = "itineraries-sample33.csv"
  private val outputPathJobNotOptimized = "output/jobNotOptimized"
  private val outputPathJobOptimized = "output/jobOptimized"
  private val distanceTypes = DistanceType.values.toArray
  private val numClasses = DistanceType.values.size
  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Flight Prices Job").getOrCreate()

    if (args.length != numParams) {
      logger.error("The first parameter should indicate the deployment mode (\"local\" or \"remote\")")
      logger.error("The second parameter should indicate the job (1 for the job not optimized, 2 for the job optimized)")
      sys.exit(1)
    }

    val deploymentMode = args(0)
    val job = args(1).toInt
    val writeMode = if (deploymentMode == "sharedRemote") "remote" else deploymentMode
    val inputPath = Commons.getDatasetPath(deploymentMode, datasetsPath + fileName)
    logger.info(inputPath)
    job match {
      case 1 => jobNotOptimized(spark, inputPath, writeMode)
      case 2 => jobOptimized(spark, inputPath, writeMode)
    }
  }

  /**
   * Job not optimized.
   *
   * @param spark     the SparkSession to submit the job.
   * @param inputPath the input path of the dataset.
   * @param writeMode the write mode (it can be "local" or "remote").
   */
  private def jobNotOptimized(spark: SparkSession, inputPath: String, writeMode: String): Unit = {
    val sc = spark.sparkContext
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

    logger.info("Job Not Optimized")
    val outputPath = Commons.getDatasetPath(writeMode, outputPathJobNotOptimized)
    logger.info(outputPath)

    val rddFlights = sc.textFile(inputPath).flatMap(FlightParser.parseFlightLine)
      // (k,v) => (startingAirport, destinationAirport), (totalTravelDistance, flightDate, totalFare))
      .map(flight => ((flight.startingAirport, flight.destinationAirport),
        (flight.totalTravelDistance, flight.flightMonth, flight.totalFare)))

    val avgDistances = rddFlights
      .aggregateByKey((0.0, 0))(
        (acc, travelDistance) => (acc._1 + travelDistance._1, acc._2 + 1),
        (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
      )
      // (k,v) => ((startingAirport, destinationAirport), avgDistance)
      .mapValues { case (sumDistance, count) => sumDistance / count }

    val (minDistance, maxDistance) = avgDistances
      .aggregate((Double.MaxValue, Double.MinValue))(
        (acc, avgDistance) => (math.min(acc._1, avgDistance._2), math.max(acc._2, avgDistance._2)),
        (acc1, acc2) => (math.min(acc1._1, acc2._1), math.max(acc1._2, acc2._2))
      )

    val range = (maxDistance - minDistance) / numClasses

    avgDistances
      .mapValues {
        d: Double =>
          val index = Math.min(((d - minDistance) / range).toInt, numClasses - 1)
          distanceTypes(index)
      }
      // (k,v) => ((startingAirport, destinationAirport), classification)
      .join(rddFlights)
      // (k,v) => ((startingAirport, destinationAirport), (classification, (totalTravelDistance, flightDate, totalFare)))
      .map { case (_, (classification, (_, month, totalFare))) => ((month, classification), (totalFare, 1)) }
      // (k,v) => ((month, classification), (totalFare, 1))
      .reduceByKey((acc, totalFare) =>
        (BigDecimal(acc._1 + totalFare._1).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble, acc._2 + totalFare._2))
      .map { case ((month, classification), (sumTotalFare: Double, count: Int)) => (month, classification, sumTotalFare / count) }
      // (k,v) => (month, classification, avgTotalFare)
      .coalesce(1)
      .toDF().write.format("csv").mode(SaveMode.Overwrite).save(outputPath)
  }

  /**
   * Job optimized.
   *
   * @param spark     the SparkSession to submit the job.
   * @param inputPath the input path of the dataset.
   * @param writeMode the write mode (it can be "local" or "remote").
   */
  private def jobOptimized(spark: SparkSession, inputPath: String, writeMode: String): Unit = {
    val sc = spark.sparkContext
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

    logger.info("Job Optimized")
    val outputPath = Commons.getDatasetPath(writeMode, outputPathJobOptimized)
    logger.info(outputPath)

    val numPartitions = 24
    val p = new HashPartitioner(numPartitions)

    val rddFlights = sc.textFile(inputPath).flatMap(FlightParser.parseFlightLine)
      // (k,v) => (startingAirport, destinationAirport), (totalTravelDistance, flightDate, totalFare))
      .map(flight => ((flight.startingAirport, flight.destinationAirport),
        (flight.totalTravelDistance, flight.flightMonth, flight.totalFare)))
      .partitionBy(p)
      .persist(MEMORY_AND_DISK_SER)
    logger.info(s"Size of rddFlights: ${SizeEstimator.estimate(rddFlights)} bytes")

    val avgDistances = rddFlights
      .aggregateByKey((0.0, 0))(
        (acc, travelDistance) => (acc._1 + travelDistance._1, acc._2 + 1),
        (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
      )
      //(k,v) => ((startingAirport, destinationAirport), avgDistance)
      .mapValues { case (sumDistance, count) => sumDistance / count }
      .persist(MEMORY_AND_DISK_SER)
    logger.info(s"Size of avgDistances: ${SizeEstimator.estimate(avgDistances)} bytes")

    val (minDistance, maxDistance) = avgDistances
      .aggregate((Double.MaxValue, Double.MinValue))(
        (acc, avgDistance) => (Math.min(acc._1, avgDistance._2), Math.max(acc._2, avgDistance._2)),
        (acc1, acc2) => (Math.min(acc1._1, acc2._1), Math.max(acc1._2, acc2._2))
      )

    val broadcastStats = spark.sparkContext.broadcast((minDistance, (maxDistance - minDistance) / numClasses))
    logger.info(s"Size of broadcastStats: ${SizeEstimator.estimate(broadcastStats.value)} bytes")

    avgDistances
      .mapValues { d =>
        val (minDistance, range) = broadcastStats.value // 40 bytes
        val index = Math.min(((d - minDistance) / range).toInt, numClasses - 1)
        distanceTypes(index)
      }
      // (k,v) => ((startingAirport, destinationAirport), classification)
      .join(rddFlights)
      // (k,v) => ((startingAirport, destinationAirport), (classification, (totalTravelDistance, flightDate, totalFare)))
      .map { case (_, (classification, (_, month, totalFare))) => ((month, classification), (totalFare, 1)) }
      // (k,v) => ((month, classification), (totalFare, 1))
      .reduceByKey((acc, totalFare) =>
        (BigDecimal(acc._1 + totalFare._1).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble, acc._2 + totalFare._2))
      .map { case ((month, classification), (sumTotalFare, count)) => (month, classification, sumTotalFare / count) }
      // (k,v) => (month, classification, avgTotalFare)
      .coalesce(1)
      .toDF().write.format("csv").mode(SaveMode.Overwrite).save(outputPath)
  }
}

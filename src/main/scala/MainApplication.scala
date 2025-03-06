import org.apache.spark.sql._
import utils.{Commons, DistanceType}

/**
 * Main application to run the jobs.
 */
object MainApplication {

  private val datasetsPath = "datasets/" // big/
  private val fileName = "itineraries-sample02.csv"
  private val outputPathJobNotOptimized = "output/jobNotOptimized"
  private val outputPathJobOptimized = "output/jobOptimized"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Flight Prices Job").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

    if (args.length < 2) {
      println("The first parameter should indicate the deployment mode (\"local\" or \"remote\")")
      println("The second parameter should indicate the job (1 for the job not optimized, 2 for the job optimized)")
      return
    }
    // local 1 -> input e output
    // remote 1 -> input e output
    // shared local 1 -> input shared + output local
    // shared remote 1 -> input shared + output remote

    val deploymentMode = args(0)
    var writeMode = deploymentMode
    if(deploymentMode == "sharedRemote"){
      writeMode = "remote"
    }
    val job = args(1)
    val numClasses = DistanceType.values.size

    if (deploymentMode == "local") {
      println(spark.sparkContext.getConf.get("spark.driver.memory"))      // 4g
      println(spark.sparkContext.getConf.get("spark.driver.cores"))       // 4
    }

    if (deploymentMode == "remote") {
      println(spark.sparkContext.getConf.get("spark.executor.memory"))    // 5g
      println(spark.sparkContext.getConf.get("spark.executor.cores"))     // 2
      println(spark.sparkContext.getConf.get("spark.executor.instances")) // 6
    }

    println(spark.sparkContext.defaultParallelism) // local:4 remote: 2

    if (job=="1") {
      println("Job Not Optimized")

      val rddFlights = spark.sparkContext
        .textFile(Commons.getDatasetPath(deploymentMode, datasetsPath + fileName))
        .flatMap(FlightParser.parseFlightLine)
        .map(flight => ((flight.startingAirport, flight.destinationAirport),
          (flight.totalTravelDistance, flight.flightMonth, flight.totalFare)))

      val avgDistancesNO = rddFlights
        .aggregateByKey((0.0, 0))(
          (acc, travelDistance) => (acc._1 + travelDistance._1, acc._2 + 1),
          (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
        )
        .mapValues { case (sumDistance, count) => sumDistance / count }

      val (minDistanceNO, maxDistanceNO) = avgDistancesNO
        .aggregate((Double.MaxValue, Double.MinValue))(
          (acc, avgDistance) => (math.min(acc._1, avgDistance._2), math.max(acc._2, avgDistance._2)),
          (acc1, acc2) => (math.min(acc1._1, acc2._1), math.max(acc1._2, acc2._2))
        )

      val rangeNO = (maxDistanceNO - minDistanceNO) / numClasses

      avgDistancesNO
        .mapValues {
          case d if d < minDistanceNO + rangeNO => DistanceType.short
          case d if d < minDistanceNO + (numClasses - 1) * rangeNO => DistanceType.medium
          case _ => DistanceType.long
        }
        .join(rddFlights)
        .map { case (_, (classification, (_, month, totalFare))) => ((month, classification), (totalFare, 1)) }
        .reduceByKey((acc, totalFare) => (acc._1 + totalFare._1, acc._2 + totalFare._2))
        .map { case ((month, classification), (sumTotalFare : Double, count: Int)) => (month, classification, sumTotalFare / count) }
        .coalesce(1)
        .toDF().write.format("csv").mode(SaveMode.Overwrite).save(Commons.getDatasetPath(writeMode,outputPathJobNotOptimized))

    } else if (job=="2") {
      println("Job Optimized")

    } else {
      println("Wrong job number")
    }
  }
}

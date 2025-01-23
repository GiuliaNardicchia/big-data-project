import org.apache.spark.sql._
import utils._

object MainApplication {

  val fileName = "itineraries-sample16.csv"
  val datasetPath = "../../../../datasets/big/" + fileName
  val outputPathJobNotOptimized = "../../../../output/jobNotOptimized"
  val outputPathJobOptimized = "../../../../output/jobOptimized"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Flight job").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

    if(args.length < 2){
      println("The first parameter should indicate the deployment mode (\"local\" or \"remote\")")
      println("The second parameter should indicate the job (1 for the job not optimized, 2 for the job optimized)")
      return
    }

    val deploymentMode = args(0)
    var writeMode = deploymentMode
    if(deploymentMode == "sharedRemote"){
      writeMode = "remote"
    }
    val job = args(1)

    if (job=="1") {
      println("Job Not Optimized")
    } else if (job=="2") {
      println("Job Optimized")
    } else {
      println("Wrong job number")
    }
  }
}

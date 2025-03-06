package utils

import org.apache.spark.sql.SparkSession

import java.io.InputStream

/**
 * The Commons object provides common functionality for managing dataset paths and configuring the Spark context.
 */
object Commons {

  /**
   * Enumeration representing the possible deployment mode for the application ("local", "remote", "sharedRemote").
   */
  private object DeploymentMode extends Enumeration {
    type DeploymentMode = Value
    val local: Value = Value("local")
    val remote: Value = Value("remote")
    val sharedRemote: Value = Value("sharedRemote")
  }

  /**
   * Initializes the Spark context configuration based on the specified deployment mode.
   * @param deploymentMode The deployment mode.
   * @param spark The active SparkSession instance.
   */
  def initializeSparkContext(deploymentMode: String, spark: SparkSession): Unit = {
    if (DeploymentMode.withName(deploymentMode) == DeploymentMode.remote){
      val stream: InputStream = getClass.getResourceAsStream(Config.credentialsPath)
      val lines = scala.io.Source.fromInputStream( stream ).getLines.toList

      spark.sparkContext.hadoopConfiguration.set("fs.s3a.fast.upload", "true")
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.fast.upload.buffer", "bytebuffer")

      spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", lines(0))
      spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", lines(1))
    }
  }

  /**
   * Computes the dataset path based on the deployment mode and the specified local or remote paths.
   * @param deploymentMode The deployment mode.
   * @param localPath The relative path to the dataset when operating in local deployment mode.
   * @param remotePath The relative path to the dataset when operating in shared or non-shared remote deployment modes.
   * @return A string representing the resolved dataset path.
   */
  private def getDatasetPath(deploymentMode: String, localPath: String, remotePath: String): String = {
    if (deploymentMode == DeploymentMode.local.toString) {
      "file://" + Config.projectDir + "/" + localPath
    }
    else if (deploymentMode == DeploymentMode.sharedRemote.toString) {
      "s3a://" + Config.s3sharedBucketName + "/" + remotePath
    }
    else {
      "s3a://" + Config.s3bucketName + "/" + remotePath
    }
  }

  /**
   * Returns the dataset path based on the deployment mode and the specified path.
   * @param deploymentMode The deployment mode.
   * @param path The dataset path provided as input.
   * @return A string representing the resolved dataset path.
   */
  def getDatasetPath(deploymentMode: String, path: String): String = {
    println(getDatasetPath(deploymentMode, path, path))
    getDatasetPath(deploymentMode, path, path)
  }
}
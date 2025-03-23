package utils

/**
 * The configurations of the application.
 */
object Config {
  /**
   * The local directory containing this repository.
   */
  val projectDir: String = "/C:/Users/HP/Desktop/UNIBO/LaureaMagistrale/2 Anno/I Semestre/Big Data/2024-2025/Laboratory/ProjectBigData"
  /**
   * The name of the shared bucket on AWS S3 to read datasets.
   */
  val s3sharedBucketName: String = "unibo-bd2425-gnardicchia"
  /**
   * The name of the bucket on AWS S3.
   */
  val s3bucketName: String = "unibo-bd2425-gnardicchia"
  /**
   * The path to the credentials file for AWS.
   */
  val credentialsPath: String = "/aws_credentials.txt"
}

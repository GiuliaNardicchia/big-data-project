package utils

/**
 * Enumeration representing the possible distance modes for the application.
 */
object Distance extends Enumeration {
  type Distance = Value
  val short: Value = Value("short")
  val medium: Value = Value("medium")
  val long: Value = Value("long")
}

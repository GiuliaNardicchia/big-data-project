package utils

/**
 * Enumeration representing the possible distance types.
 */
object DistanceType extends Enumeration {
  type DistanceType = Value
  val short: Value = Value("short")
  val medium: Value = Value("medium")
  val long: Value = Value("long")
}

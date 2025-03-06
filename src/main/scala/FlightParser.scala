import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Flight parser.
 */
object FlightParser extends Serializable {

  private val comma = ","

  /**
   * Convert from date to month.
   * @param dateString the date.
   * @return the month.
   */
  private def monthFromDate(dateString: String): Int = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(dateString.trim)
    val cal = Calendar.getInstance()
    cal.setTime(date)
    cal.get(Calendar.MONTH) + 1
  }

  /**
   * Parse a flight record.
   * @param line The line that has to be parsed.
   * @return None in case of input errors, Flight otherwise.
   */
  def parseFlightLine(line: String): Option[Flight] = {
    try {
      val columns = line.split(comma)
      Some(
        Flight(
          legId = columns(0).trim,
          searchMonth = monthFromDate(columns(1)),
          flightMonth = monthFromDate(columns(2)),
          startingAirport = columns(3).trim,
          destinationAirport = columns(4).trim,
          fareBasisCode = columns(5).trim,
          travelDuration = columns(6).trim,
          elapsedDays = columns(7).trim.toInt,
          isBasicEconomy = columns(8).trim.toBoolean,
          isRefundable = columns(9).trim.toBoolean,
          isNonStop = columns(10).trim.toBoolean,
          baseFare = columns(11).trim.toDouble,
          totalFare = columns(12).trim.toDouble,
          seatsRemaining = columns(13).trim.toInt,
          totalTravelDistance = columns(14).trim.toDouble,
          segmentsDepartureTimeEpochSeconds = columns(15).trim,
          segmentsDepartureTimeRaw = columns(16).trim,
          segmentsArrivalTimeEpochSeconds = columns(17).trim,
          segmentsArrivalTimeRaw = columns(18).trim,
          segmentsArrivalAirportCode = columns(19).trim,
          segmentsDepartureAirportCode = columns(20).trim,
          segmentsAirlineName = columns(21).trim,
          segmentsAirlineCode = columns(22).trim,
          segmentsEquipmentDescription = columns(23).trim,
          segmentsDurationInSeconds = columns(24).trim,
          segmentsDistance = columns(25).trim,
          segmentsCabinCode = columns(26).trim
        )
      )
    } catch {
      case e: Exception =>
//        println(s"Error during parsing of the line '$line': ${e.getMessage}")
        None
    }
  }
}
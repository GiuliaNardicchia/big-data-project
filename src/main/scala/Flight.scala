/**
 * Flight case class.
 *
 * @param legId                             The unique identifier for the flight.
 * @param searchMonth                       The month in which the search was conducted.
 * @param flightMonth                       The month in which the flight is scheduled.
 * @param startingAirport                   The airport code IATA for the departure location.
 * @param destinationAirport                The airport code IATA for the arrival location.
 * @param fareBasisCode                     The fare basis code for the flight.
 * @param travelDuration                    The total duration of the flight.
 * @param elapsedDays                       The number of days elapsed.
 * @param isBasicEconomy                    Whether the flight is a basic economy flight.
 * @param isRefundable                      Whether the flight is refundable.
 * @param isNonStop                         Whether the flight is non-stop.
 * @param baseFare                          The base fare for the flight.
 * @param totalFare                         The total fare for the flight, including taxes and other fees.
 * @param seatsRemaining                    The number of seats remaining on the flight.
 * @param totalTravelDistance               The total distance of the flight.
 * @param segmentsDepartureTimeEpochSeconds The departure time of each segment expressed as epoch seconds.
 * @param segmentsDepartureTimeRaw          The departure time of each segment in raw format.
 * @param segmentsArrivalTimeEpochSeconds   The arrival time of each segment expressed as epoch seconds.
 * @param segmentsArrivalTimeRaw            The arrival time of each segment in raw format.
 * @param segmentsArrivalAirportCode        The airport code IATA for the arrival location of each segment.
 * @param segmentsDepartureAirportCode      The airport code IATA for the departure location of each segment.
 * @param segmentsAirlineName               The name of the airline for each segment.
 * @param segmentsAirlineCode               The code of the airline for each segment.
 * @param segmentsEquipmentDescription      The description of the equipment for each segment.
 * @param segmentsDurationInSeconds         The duration of each segment in seconds.
 * @param segmentsDistance                  The distance of each segment.
 * @param segmentsCabinCode                 The cabin code for each segment.
 */
case class Flight(
                   legId: String,
                   searchMonth: Int,
                   flightMonth: Int,
                   startingAirport: String,
                   destinationAirport: String,
                   fareBasisCode: String,
                   travelDuration: String,
                   elapsedDays: Int,
                   isBasicEconomy: Boolean,
                   isRefundable: Boolean,
                   isNonStop: Boolean,
                   baseFare: Double,
                   totalFare: Double,
                   seatsRemaining: Int,
                   totalTravelDistance: Double,
                   segmentsDepartureTimeEpochSeconds: String,
                   segmentsDepartureTimeRaw: String,
                   segmentsArrivalTimeEpochSeconds: String,
                   segmentsArrivalTimeRaw: String,
                   segmentsArrivalAirportCode: String,
                   segmentsDepartureAirportCode: String,
                   segmentsAirlineName: String,
                   segmentsAirlineCode: String,
                   segmentsEquipmentDescription: String,
                   segmentsDurationInSeconds: String,
                   segmentsDistance: String,
                   segmentsCabinCode: String
                 ) extends Serializable

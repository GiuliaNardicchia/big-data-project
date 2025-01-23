/**
 * Flight case class.
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
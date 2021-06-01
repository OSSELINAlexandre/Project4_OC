package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	// The code is taking the INT linked to the hour in order to count the amount of
	// time in the parking slot.
	// For instance, if you arrived at 9h59 AM and you get out at 10h01, the system
	// will count 1 hour.
	// In the same vein, if you arrived at 9h00 and get out at 9h59, the system will
	// count 0 hour.

	// SOLUTION : Instead of taking the HOUR, we take the time (see JavaDoc on Date)
	// We substract the OutTime by the InTime, and we transform that number to a
	// unit in hour as a Double.
	// For Instance, if you came in at 9h30 am and get out at 10h00 am in the same
	// day,
	// the code will count the duration as 0.5, which makes it easier to count the
	// fare
	// of the ticket.
	long inHour = ticket.getInTime().getTime();
	long outHour = ticket.getOutTime().getTime();

	// Because the getTime() method transform the time in MilliSeconds, we have,
	// with the division, to
	// transform the unit in a Hour unit. (There is 1000 ms in a second, 60 seconds
	// in a minute, 60 minutes in an hour).
	double duration = (outHour - inHour) / (1000.0 * 60.0 * 60.0);

	switch (ticket.getParkingSpot().getParkingType()) {
	case CAR: {
	    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
	    break;
	}
	case BIKE: {
	    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
	    break;
	}
	default:
	    throw new IllegalArgumentException("Unkown Parking Type");
	}
    }
}
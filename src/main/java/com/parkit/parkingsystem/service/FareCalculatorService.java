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
	int inHour = ticket.getInTime().getHours();
	int outHour = ticket.getOutTime().getHours();

	// TODO: Some tests are failing here. Need to check if this logic is correct
	int duration = outHour - inHour;

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
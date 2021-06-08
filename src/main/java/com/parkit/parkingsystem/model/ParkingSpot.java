package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * <b>ParkingSpot is the model to represents the Parking from the Database in our application.</b>
 * <p>
 * A parking slot is composed, in the DB and therefore in our model, of :
 * <ul>
 * <li>an ID</li>
 * <li>a TYPE</li>
 * <li>an Availability</li>
 * </ul>
 * </p>
 * 
 * 
 * @author Alexandre OSSELIN
 * @version 1.0
 */

public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return number;
    }

    public void setId(int number) {
        this.number = number;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}

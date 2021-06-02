package com.parkit.parkingsystem.constants;

/**
 * <b>This class contains all required request to communicate with the
 * Database</b>
 * <p>
 * The first category are pre-written requests for the Parking Table
 * </p>
 * <p>
 * The second category are pre-written requests for the Ticket Table
 * </p>
 * 
 * These requests can be use both in a prod or test environment.
 */

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    /**
     * <p>
     * We change the GET_TICKET request and add DESC. This way, we retrieve from the
     * DB the 'oldest' of InTime of all tickets for a given Registration number.
     * </p>
     * <p>
     * Indeed, the former method was retrieving the first ever saved ticket for a
     * given customer.
     * </p>
     * <p>
     * This lead to the OutTime of the second ticket to be saved for the 'oldest
     * ticket' for a customer instead of the current Ticket (which is, in the DB,
     * the most recent one).
     * </p>
     * 
     * <p>
     * The outTime of the current Ticket was therefore not updated and left to Null.
     * </p>
     */
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";

    /**
     * <p>
     * We created the CHECK_TICKET_IF_CUSTOMER to retrieve all ticket from a given
     * registrationNumber.
     * </p>
     * <p>
     * This Request is usefull for use to check the customer faithfullness and apply
     * discount thereof.
     * </p>
     * 
     */
    public static final String CHECK_TICKET_IF_CUSTOMER = "SELECT * FROM ticket WHERE VEHICLE_REG_NUMBER = ?"; 

}

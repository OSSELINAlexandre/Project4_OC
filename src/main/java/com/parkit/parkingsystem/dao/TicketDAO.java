package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * <b>TicketDAO is the DataAccessObject containing all the needed methods to
 * access the Ticket Table. </b>
 * <p>
 * ParkingSpotDAO can get, save and update the Ticket Table.
 * 
 * @see ParkingSpot
 * 
 * @author Alexandre OSSELIN
 * @version 1.0
 */

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * saveTicket save the ticket in the Database
	 * 
	 * 
	 * <p>
	 * At the end of the process, the database updated the availability of the
	 * parking slot. A ticket with the inTime and the registration number of the
	 * client has been created.
	 * </p>
	 * <p>
	 * The method return TRUE if the ticket was appropriately created, and false
	 * otherwise. This method uses a Ticket as an argument and save all the
	 * information on this given ticket in the Database.
	 * </p>
	 * 
	 * @return boolean
	 * @see TicketDAO
	 */

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// ps.setInt(1,ticket.getId());
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			return ps.execute();
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			return false;
		}
	}

	/**
	 * getTicket get a ticket in function of the registration number of the
	 * customer.
	 * 
	 * 
	 * <p>
	 * The return value of the method is a ticket containing a InTime date and
	 * OutTime date, plus a fare depending on these two durations.
	 * </p>
	 * 
	 * @return Ticket
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			return ticket;
		}
	}

	/**
	 * updateTicket update the information on a ticket provided in the argument of
	 * the method.
	 * 
	 * 
	 * <p>
	 * At the end of the process, the database twin of the ticket provided in
	 * argument has been updated in the database.
	 * </p>
	 * 
	 * 
	 * @return boolean
	 * @see ParkingSpotDAO
	 */

	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}

	/**
	 * <b> checkCustomerProgram check if a customer already has been registered in
	 * the database.</b>
	 * <p>
	 * The method will check, in function of a given vehicleRegistrationNumber if
	 * the customer is faithful.
	 * </p>
	 * <p>
	 * The second argument of the method is to avoid duplication of code.
	 *
	 * <ul>
	 * <li>Setting FALSE is used at the processIncomingVehicle</li>
	 * <li>Setting TRUE is used at the processOutcomingVehicle</li>
	 * </ul>
	 * 
	 * <P>
	 * When FALSE is set, the method will return TRUE if a ticket exists in the
	 * Database (a former one).
	 * </p>
	 * <P>
	 * When TRUE is set, the method will return TRUE if at least two ticket exist in
	 * the Database (the former and the current one).
	 * </p>
	 * 
	 * <p>
	 * Indeed, when the customer processOutcomingVehicle, a ticket exists in the
	 * Database (the current one), and instead of duplication we use a boolean.
	 * </p>
	 * 
	 * At the end of the process, we know if the customer has been faithful and can
	 * benefit from our customer program.
	 * 
	 * @return boolean
	 * @see ParkingSpotDAO
	 */

	public boolean checkCustomerProgram(String vehicleRegNumber, Boolean exit) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.CHECK_TICKET_IF_CUSTOMER);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (exit) {
				rs.next();
			}
			if (rs.next()) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}
}

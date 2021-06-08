package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>Class of the User interface in the format of command lines</b>
 * <p>
 * InteractiveShell makes the link between the service and the client. It
 * allocates the needs of the customer based on their answers.
 * </p>
 * <p>
 * The class also instantiate all the needed classes for the ParkingService. In
 * particular the TicketDAO and ParkingDAO.
 * </p>
 * 
 * @see parkingService
 * 
 * @author Alexandre OSSELIN
 * @version 1.0
 */

public class InteractiveShell {

	private static final Logger logger = LogManager.getLogger("InteractiveShell");

	public static void loadInterface() {
		logger.info("App initialized!!!");
		System.out.println("Welcome to Parking System!");

		boolean continueApp = true;
		InputReaderUtil inputReaderUtil = new InputReaderUtil();
		ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
		TicketDAO ticketDAO = new TicketDAO();
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		while (continueApp) {
			loadMenu();
			int option = inputReaderUtil.readSelection();
			switch (option) {
			case 1: {
				Long entryDate = System.currentTimeMillis();
				parkingService.processIncomingVehicle(entryDate);
				break;
			}
			case 2: {
				Long exitDate = System.currentTimeMillis();
				parkingService.processExitingVehicle(exitDate);
				break;
			}
			case 3: {
				System.out.println("Exiting from the system!");
				continueApp = false;
				break;
			}
			default:
				System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
			}
		}
	}

	private static void loadMenu() {
		System.out.println("Please select an option. Simply enter the number to choose an action");
		System.out.println("1 New Vehicle Entering - Allocate Parking Space");
		System.out.println("2 Vehicle Exiting - Generate Ticket Price");
		System.out.println("3 Shutdown System");
	}

}

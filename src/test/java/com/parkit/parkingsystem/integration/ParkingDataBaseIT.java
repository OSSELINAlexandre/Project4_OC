package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void testParkingACar() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processIncomingVehicle();

	int currentSpot = ticketDAO.getTicket("ABCDEF").getParkingSpot().getId();
	int nextSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

	// A1 - We first assert that a Ticket was created.
	// Indeed, the TEST TICKET db is empty at each new launch. If the Ticket of
	// ABCDEF has a InTime that isn't null,
	// Therefore a ticket is saved in the DB.

	// B1 - We then call the Method getNextAvailableSlot() that takes the first
	// available spot in the TEST PARKING db.
	// Because the currentSport (which is the ID of the parking linked to the Ticket
	// of registration Number ABCDEF
	// is different from nextSpot (which is the ID of the ID of the first avaiable
	// parking slot of the type CAR
	// show that the TEST PARKING db took into account the Ticket.

	assertTrue(ticketDAO.getTicket("ABCDEF").getInTime() != null);
	assertNotEquals(currentSpot, nextSpot);
	// TODO: check that a ticket is actualy saved in DB and Parking table is updated
	// with availability
    }

    // A2 - This test isn't complying to the FIRST principle. This test isn't
    // "independant" because it's success or failure
    // is linked to the testParkingACar().

    @Test
    public void testParkingLotExit() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processIncomingVehicle();

	// Necessary of a second so that the InTime and the OutTime is different.
	// Indeed, the code can be too fast for the DB, and the InTime and OutTime can
	// be the same,
	// which could lead to error in the FareCalculatorService(). We put a one second
	// sleep so the times saved
	// are different

	try {
	    Thread.sleep(1000);
	} catch (Exception E) {

	}

	parkingService.processExitingVehicle();

	// B2 - We first assert that the ticket outTime linked to the registered vehicle
	// ABCDEF.
	// Indeed, the ticket outime is null when the ticket is created with
	// processIncomingVehicle. If the Ticket of
	// ABCDEF has a outTime that isn't null,
	// Therefore 'the out time are populated correctly in the DB' for at least a
	// single ticket.

	// C2 - We then also get the Ticket generated during the whole process and check
	// the price.
	// The second test is therefore used to check is the fareCalculatorService
	// calculate a ticket price
	// base on the inTime and outTime, and save that price in the Ticket.
	// Because we put a sleep of one second, the price should be different from 0
	// (which is
	// the default value given to a new ticket).

	// Conclusion, if both these test pass, the fare generated and the outtime are
	// populated correctly.

	assertTrue(ticketDAO.getTicket("ABCDEF").getOutTime() != null);
	assertNotEquals(ticketDAO.getTicket("ABCDEF").getPrice(), 0);

	// TODO: check that the fare generated and out time are populated correctly in
	// the database
    }

    @Test
    public void ACustomer_shouldGetADiscount_WhenCameTwice() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	Long ended = System.currentTimeMillis() + (1000 * 45 * 60);
	Long started = System.currentTimeMillis();

	parkingService.processIncomingVehicle(started);

	parkingService.processExitingVehicle(ended);

	Double priceB = ticketDAO.getTicket("ABCDEF").getPrice();

	parkingService.processIncomingVehicle(started + (1000 * 60 * 47));

	parkingService.processExitingVehicle(ended + (1000 * 60 * 47));

	Double priceA = ticketDAO.getTicket("ABCDEF").getPrice();

	System.out.println("Debut : " + priceA + " || End : " + priceB);
	assertNotEquals(priceB, priceA);
	assertTrue((priceB * 0.95 == priceA));

    }

}

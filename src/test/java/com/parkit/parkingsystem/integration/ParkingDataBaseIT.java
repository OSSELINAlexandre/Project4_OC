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

	// A - We first assert that a Ticket was created.
	// Indeed, the TEST TICKET db is empty at each new launch. If the Ticket of
	// ABCDEF has a InTime that isn't null,
	// Therefore a ticket is saved in the DB.

	// B - We then call the Method getNextAvailableSlot() that takes the first
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

    // A - This test isn't complying to the FIRST principle. This test isn't "independant" because it's success or failure 
    //is linked to the testParkingACar().
    @Test
    public void testParkingLotExit() {
	testParkingACar();
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processExitingVehicle();
	// TODO: check that the fare generated and out time are populated correctly in
	// the database
    }

}

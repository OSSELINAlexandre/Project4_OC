package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;

import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseITIntegrationTest {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtilFirstVehicle;
	@Mock
	private static InputReaderUtil inputReaderUtilSecondVehicle;
	@Mock
	private static InputReaderUtil inputReaderUtilThirdVehicle;
	@Mock
	private static InputReaderUtil inputReaderUtilFourthVehicle;
	@Mock
	private static InputReaderUtil inputReaderUtilFifthVehicle;
	@Mock
	private static InputReaderUtil inputReaderUtilSixthVehicle;

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
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void TwoCustomers_canBeSavedInTheSystem_WhileRegisteringSameTime() {
		try {
			when(inputReaderUtilFirstVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilFirstVehicle.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(inputReaderUtilSecondVehicle.readSelection()).thenReturn(2);
			when(inputReaderUtilSecondVehicle.readVehicleRegistrationNumber()).thenReturn("VWXYZ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		ParkingService parkingServiceFirstCusto = new ParkingService(inputReaderUtilFirstVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceSecondCusto = new ParkingService(inputReaderUtilSecondVehicle, parkingSpotDAO,
				ticketDAO);

		Long ended = System.currentTimeMillis() + (1000 * 45 * 60);
		Long started = System.currentTimeMillis();

		parkingServiceFirstCusto.processIncomingVehicle(started);
		parkingServiceSecondCusto.processIncomingVehicle(started);

		parkingServiceSecondCusto.processExitingVehicle(ended);
		parkingServiceFirstCusto.processExitingVehicle(ended);

		assertTrue(ticketDAO.getTicket("ABCDEF").getOutTime() != null);
		assertTrue(ticketDAO.getTicket("VWXYZ").getOutTime() != null);

	}

	@Test
	public void FiveCustomers_canBeSavedInTheSystem_WhileRegisteringSameTime() {
		try {
			when(inputReaderUtilFirstVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilFirstVehicle.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(inputReaderUtilSecondVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilSecondVehicle.readVehicleRegistrationNumber()).thenReturn("GHIJK");
			when(inputReaderUtilThirdVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilThirdVehicle.readVehicleRegistrationNumber()).thenReturn("LMNOPQ");
			when(inputReaderUtilFourthVehicle.readSelection()).thenReturn(2);
			when(inputReaderUtilFourthVehicle.readVehicleRegistrationNumber()).thenReturn("RSTUV");
			when(inputReaderUtilFifthVehicle.readSelection()).thenReturn(2);
			when(inputReaderUtilFifthVehicle.readVehicleRegistrationNumber()).thenReturn("VWXYZ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		ParkingService parkingServiceFirstCusto = new ParkingService(inputReaderUtilFirstVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceSecondCusto = new ParkingService(inputReaderUtilSecondVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceThirdCusto = new ParkingService(inputReaderUtilThirdVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceFourthCusto = new ParkingService(inputReaderUtilFourthVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceFifthCusto = new ParkingService(inputReaderUtilFifthVehicle, parkingSpotDAO,
				ticketDAO);

		Long ended = System.currentTimeMillis() + (1000 * 45 * 60);
		Long started = System.currentTimeMillis();

		parkingServiceFirstCusto.processIncomingVehicle(started);
		parkingServiceSecondCusto.processIncomingVehicle(started);
		parkingServiceThirdCusto.processIncomingVehicle(started);
		parkingServiceFourthCusto.processIncomingVehicle(started);
		parkingServiceFifthCusto.processIncomingVehicle(started);

		parkingServiceSecondCusto.processExitingVehicle(ended);
		parkingServiceFirstCusto.processExitingVehicle(ended);
		parkingServiceThirdCusto.processExitingVehicle(ended);
		parkingServiceFourthCusto.processExitingVehicle(ended);
		parkingServiceFifthCusto.processExitingVehicle(ended);

		assertTrue(ticketDAO.getTicket("ABCDEF").getOutTime() != null);
		assertTrue(ticketDAO.getTicket("GHIJK").getOutTime() != null);
		assertTrue(ticketDAO.getTicket("LMNOPQ").getOutTime() != null);
		assertTrue(ticketDAO.getTicket("RSTUV").getOutTime() != null);
		assertTrue(ticketDAO.getTicket("VWXYZ").getOutTime() != null);

	}

	@Test
	public void MoreThan5Customers_cannotBeSavedInTheSystem_WhileRegisteringSameTime() {
		try {
			when(inputReaderUtilFirstVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilFirstVehicle.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(inputReaderUtilSecondVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilSecondVehicle.readVehicleRegistrationNumber()).thenReturn("GHIJK");
			when(inputReaderUtilThirdVehicle.readSelection()).thenReturn(1);
			when(inputReaderUtilThirdVehicle.readVehicleRegistrationNumber()).thenReturn("LMNOPQ");
			when(inputReaderUtilFourthVehicle.readSelection()).thenReturn(2);
			when(inputReaderUtilFourthVehicle.readVehicleRegistrationNumber()).thenReturn("RSTUV");
			when(inputReaderUtilFifthVehicle.readSelection()).thenReturn(2);
			when(inputReaderUtilFifthVehicle.readVehicleRegistrationNumber()).thenReturn("VWXYZ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		ParkingService parkingServiceFirstCusto = new ParkingService(inputReaderUtilFirstVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceSecondCusto = new ParkingService(inputReaderUtilSecondVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceThirdCusto = new ParkingService(inputReaderUtilThirdVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceFourthCusto = new ParkingService(inputReaderUtilFourthVehicle, parkingSpotDAO,
				ticketDAO);
		ParkingService parkingServiceFifthCusto = new ParkingService(inputReaderUtilFifthVehicle, parkingSpotDAO,
				ticketDAO);

		Long started = System.currentTimeMillis();

		parkingServiceFirstCusto.processIncomingVehicle(started);
		parkingServiceSecondCusto.processIncomingVehicle(started);
		parkingServiceThirdCusto.processIncomingVehicle(started);
		parkingServiceFourthCusto.processIncomingVehicle(started);
		parkingServiceFifthCusto.processIncomingVehicle(started);

		Object newComerCar = parkingServiceThirdCusto.getNextParkingNumberIfAvailable();
		Object newComerBike = parkingServiceFifthCusto.getNextParkingNumberIfAvailable();

		assertTrue(newComerBike == null && newComerCar == null);
	}

}

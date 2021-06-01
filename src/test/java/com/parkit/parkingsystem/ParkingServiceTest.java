package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    
    
    

    // Implementing this test in order to increase the coverage of Test Unit.
    // Ca ne marche pas pour une raison simple, tu lui a dit de return false
    // lorsqu'on demande
    // si la place de parking est valable ou non.
    @Test
    public void parkingService_shouldUseParkingSpotDao_ForProcessIncomingVehicle() {
	// GIVEN
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);
	parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	
	//WHEN
	parkingService.processIncomingVehicle();
	
	//THEN
	verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void parkingService_shouldNotSaveTicket_ForNoAvailableParkingSpot() {

	// GIVEN
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(0);
	parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	// WHEN
	parkingService.processIncomingVehicle();

	// THEN
	verify(ticketDAO, Mockito.times(0)).saveTicket(any(Ticket.class));

    }

    @Test
    public void parkingService_shouldUseParkingSpotDao_ForProcessExitingVehicle() {
	// GIVEN
	try {
	    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

	    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	    Ticket ticket = new Ticket();
	    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	    ticket.setParkingSpot(parkingSpot);
	    ticket.setVehicleRegNumber("ABCDEF");
	    when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
	    when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

	    when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

	    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	} catch (Exception e) {
	    System.out.println("Couldn't set up the Mocks");
	}

	// WHEN
	parkingService.processExitingVehicle();

	// THEN
	verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

}

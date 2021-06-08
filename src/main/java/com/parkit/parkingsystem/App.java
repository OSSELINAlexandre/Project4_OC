package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>App is the main class of the application.</b>
 * 
 * @see InteractiveShell
 * 
 * @author Alexandre OSSELIN
 * @version 1.0
 */

public class App {
    private static final Logger logger = LogManager.getLogger("App");

    public static void main(String args[]) {
	logger.info("Initializing Parking System");
	InteractiveShell.loadInterface();
    }
}

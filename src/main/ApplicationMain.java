package main;

import main.input.InputThread;
import main.input.settings.ApplicationSetup;

public class ApplicationMain {
	/**
	 * 
	 * @author f
	 * 
	 */
	public static void main(String[] args) {

		// create
		ApplicationSetup.getInstance();
		ApplicationStatus.getInstance().readIndex();
		InputThread input = new InputThread();

		input.start(); // blocks until ended by !end as input

	}

}

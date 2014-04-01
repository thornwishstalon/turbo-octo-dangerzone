package main.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import main.input.command.ApplicationCommandList;
import main.input.parser.CommandParser;

/***
 * 
 * @author F
 * 
 */

public class InputThread extends Thread {

	private BufferedReader reader;
	private CommandParser parser;

	private static Logger logger = LogManager.getLogger("InputThread");

	public void run() {
		parser = new CommandParser(true);
		parser.setCommandList(new ApplicationCommandList());

		System.out.println("WELCOME");
		System.out.println("READY for Input!");

		reader = new BufferedReader(new InputStreamReader(System.in));
		String input;

		try {
			while ((input = reader.readLine()) != null) {

				if (input.equals("!end")) {

					break;
				} else {
					if (input.length() > 0) {
						String query = parser.parse(input.trim());
						if (query.length() > 1) {
							parser.parse(query);
						}

					} else
						System.out.println("");

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("reading input lines failed");
		} finally {
			System.out.println("BYE");
			if (reader != null) {
				try {
					// System.out.println("closing reader");
					reader.close();
					// System.out.println("reader closed");
				} catch (IOException e) {
					// System.err.println("io");
					// e.printStackTrace();
					logger.error("could not close reader");
				}

			}

		}

	}
}

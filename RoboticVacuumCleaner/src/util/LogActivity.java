package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class LogActivity {


	synchronized void loggingCleaning(String str) {

		// The name of the file to open.
		String fileName = "cleaninglog.txt";

		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName, true);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			
			bufferedWriter.write(str+"\r\n");

			// Always close files.
			bufferedWriter.close();

			fileWriter.close();

		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			
		}

	}


}

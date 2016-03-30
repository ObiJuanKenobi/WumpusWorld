package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility (static) class to handle reading text from files
 * @author Team Bits Please
 *
 */
public class FileUtils {
	
	/**
	 * Inactive constructor
	 */
	private FileUtils() {}
	
	/**
	 * Loads a text file as a single string
	 * @param file File to be read
	 * @return String containing file contents
	 */
	public static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer = "";
			while((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}

}

package ie.gmit.sw;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class WriteToCSVfile {
	
	private static DecimalFormat df = new DecimalFormat("000.000");
	private static int langDbSize = 235;

	static void WTCSVfile(double vector[], String language) throws IOException {

		FileWriter fw = new FileWriter("data.csv", true);

		// Write the vectors to the CSV file
		for (int j = 0; j < vector.length; j++) {
			fw.append(String.valueOf(df.format(vector[j])));
			fw.append(",");
		}

		// Write the labels or languages to the CSV file
		// Initialize language to the default NotAlanguage language
		// in case for some reason the language does not exist
		// Wrap the the it in a try and catch error not to interrupt the
		// service in case the language does not exist.
		int langIndex = 235;
		try {
			langIndex = Language.valueOf(language).ordinal();
		} catch (Exception e) {
			System.out.println("Language not in not in the database");
		}

		double langpos = 0.0;
		for (int i = 0; i < langDbSize; i++) {
			if (i == langIndex) {
				langpos = 1.00;
			} else {
				langpos = 0.00;
			}
			fw.append(String.valueOf(df.format(langpos)));
			fw.append(",");
		}
		fw.append("\n");
		fw.flush();
		fw.close();

	}
}

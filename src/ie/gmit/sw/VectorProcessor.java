package ie.gmit.sw;

import java.io.*;

public class VectorProcessor {

	static int vectorSize = 400;
	double[] liveTextVector = new double[vectorSize];

	void readFromFile(int processType, int vectorLength, int ngramSize, Language[] langs, String liveTextData, int epochSize)
			throws Exception {
		vectorSize = vectorLength;

		if (processType == 1) {
			System.out.println("-----Started Reading From The Training Data File...");
			try {
				// open CSV on a non append mode at the start to clear
				// anything in it.
				FileWriter fw = new FileWriter("data.csv", false);
			} catch (Exception e) {
				System.out.println("Can't open File!");
			}
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("./wili-2018-Small-11750-Edited.txt"))))) {
				String line = null;
				while ((line = br.readLine()) != null) {
					processText(line, 1, vectorLength, ngramSize);
				}

			} catch (Exception e) {

			}

		} else {

		}
		// Call the neural network to train
		// and test.
		// IsReadSaveNetwork is false as you are not
		// reading from a save NN
		System.out.println("-----Training Started...");
		new NeuralNetwork(langs, liveTextData, vectorLength, ngramSize, false, epochSize);

	}

	void processText(String line, int dataType, int vectorSize, int ngramSize) throws IOException {

		if (dataType == 1) {
			String[] record = line.split("@");

			// if a line contain more that one @ symbol return
			// some text may have emails
			if (record.length > 2) {
				// return;
			} else {
				String text = record[0].toLowerCase();
				String lang = record[1].replaceAll("\\s+", "");

				NGramProcessor.nGramProcessor(text, ngramSize, lang);

			}
		} else {
			liveTextVector = NGramProcessor.nGramProcessorLive(line, ngramSize);

		}
	}

}

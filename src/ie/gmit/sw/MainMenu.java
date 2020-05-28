package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainMenu {

	Language[] langs = Language.values();
	String input = "start";
	String liveTextData = "live Data";
	String ngramNum;
	int ngramSize = 1000;
	String numCharperNgram;
	int lengthPerNgram = 2;
	String numEpoch;
	int epochSize = 10;

	public void MM() throws Exception {

		do {
			// Enter data using BufferReader
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("*******Welcome To Language Detection Neural Network with Vector Hashing*******");
			System.out.println("1) Enter the word <string> to input a string.");
			System.out.println("2) Enter the word <file> to read input from a file.");
			System.out.println("3) Enter the word <exit> to exit.");

			input = reader.readLine();
			input = input.toLowerCase();

			if (!input.equalsIgnoreCase("exit")) {

				switch (input) {
				case "file":
					liveTextData = FileString();
					break;
				case "string":
					liveTextData = InputString();
					break;
				case "exit":
					System.out.println("\n");
					break;
				default:
					System.out.println("Invalid input, must either be file or String \n");
				}

				try {
					System.out.println("Enter the size of the vector, 1000 is advisable?  ");
					ngramNum = reader.readLine();
					ngramSize = Integer.parseInt(ngramNum);
				} catch (Exception e) {
					System.out.println(
							"Must be a number, it takes 100 as a default if a non numeric character is enter!");
				}
				try {
					System.out.println("Enter the size of the ngram, 2 is advisable? ");
					numCharperNgram = reader.readLine();
					lengthPerNgram = Integer.parseInt(numCharperNgram);
				} catch (Exception e) {
					System.out.println("Must be a number, it takes 2 as a defult if a non numeric character is enter!");
				}
				try {
					System.out.println("Enter the number of Epoch, 10 is advisable? ");
					numEpoch = reader.readLine();
					epochSize = Integer.parseInt(numEpoch);
				} catch (Exception e) {
					System.out.println("Must be a number, it takes 10 as a defult if a non numeric character is enter!");
				}

				VectorProcessor vp = new VectorProcessor();
				vp.readFromFile(1, ngramSize, lengthPerNgram, langs, liveTextData, epochSize);

				System.out.println("1) Enter the word <yes> to predict from the save network.");
				System.out.println("2) Enter the word <no> to start again.");
				System.out.println("3) Enter the word <exit> to exit.");

				input = reader.readLine();
				input = input.toLowerCase();

				if (input.equals("yes")) {
					do {

						if (input.equals("yes")) {
							System.out.println("1) Enter the word <string> to input a string.");
							System.out.println("2) Enter the word <file> to read input from a file.");

							input = reader.readLine();
							input = input.toLowerCase();
							switch (input) {
							case "file":
								liveTextData = FileString();
								break;
							case "string":
								liveTextData = InputString();
								break;
							default:
								System.out.println("Invalid input, must either be file or String \n");
							}

							new NeuralNetwork(langs, liveTextData, ngramSize, lengthPerNgram, true, epochSize);

							System.out.println("1) Enter the word <yes> to predict from a save network again.");
							System.out.println("2) Enter the word <stop> to exit.");

							input = reader.readLine();
							input = input.toLowerCase();

						} // end of if(input.equals("yes"))
					} while (!input.equalsIgnoreCase("stop"));
				}

			} // end of if (!input.equalsIgnoreCase("exit"))

		} while (!input.equalsIgnoreCase("exit"));
		System.out.println("******Goodbye!*******");

	}

	public static String InputString() throws IOException {
		String input = "start";

		// Enter data using BufferReader
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter enter a string? ");
		input = reader.readLine();
		input = input.toLowerCase();

		return input;

	}

	public static String FileString() throws IOException {
		String text = "";
		// Enter data using BufferReader
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String filePath;
		System.out.println("Enter the File path? \n");
		filePath = reader.readLine();
		String line = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
			while ((line = br.readLine()) != null) {
				text = text + " " + line.toLowerCase();
			}

		} catch (Exception e) {
			System.out.println("Can't read file.\n" + e);
		}

		return text;

	}

}

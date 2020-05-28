package ie.gmit.sw;

import java.io.IOException;

public class NGramProcessor {

	static double[] nGramProcessor(String text, int ngramSize, String lang) throws IOException {

		double[] vector = new double[VectorProcessor.vectorSize];

		for (int i = 0; i <= text.length() - ngramSize; i++) {
			CharSequence kmer = text.substring(i, i + ngramSize);

			int index = kmer.hashCode() % vector.length;
			// System.out.println(counter + " "+"---index--- " +index);
			try {
				vector[index] = vector[index] + 1;
			} catch (Exception e) {
				// vector[counter]= 0.0;
			}

			Utilities.normalize(vector, 0, 1);

		}
		WriteToCSVfile.WTCSVfile(vector, lang);
		return vector;
	}

	static double[] nGramProcessorLive(String text, int ngramSize) throws IOException {

		double[] vector = new double[VectorProcessor.vectorSize];

		for (int i = 0; i <= text.length() - ngramSize; i++) {
			CharSequence kmer = text.substring(i, i + ngramSize);

			int index = kmer.hashCode() % vector.length;
			// System.out.println(counter + " "+"---index--- " +index);
			try {
				vector[index] = vector[index] + 1;
			} catch (Exception e) {

			}

			Utilities.normalize(vector, 0, 1);

		}

		return vector;
	}
}

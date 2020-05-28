package ie.gmit.sw;

import java.io.File;
import java.io.IOException;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.MemoryDataLoader;
import org.encog.ml.data.buffer.codec.CSVDataCODEC;
import org.encog.ml.data.buffer.codec.DataSetCODEC;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.csv.CSVFormat;

public class NeuralNetwork {

	public NeuralNetwork(Language[] langs, String liveTextData, int vectorSize, int charPerngram,
			boolean IsReadSaveNetwork, int epochSize) throws IOException {
		String saveNNFile = "./trainedNetwork.nn";
		int inputs = vectorSize; // Change this to the number of input neurons
		int outputs = 235; // Change this to the number of output neurons
		int hlInput = inputs/4; // Hidden layer input
		double correct = 0;
		double total = 0;
		double tp = 0.0;
		double tn = 0.0;
		double fp = 0.0;
		double fn = 0.0;
		double sn = 0.0;
		double sp = 0.0;
		int y = 0;
		int yd = 0;
		double precision = 0.0;
		double prevalence = 0.0;

		if (IsReadSaveNetwork == false) {
			// Configure the neural network topology.
			BasicNetwork network = new BasicNetwork();
			// Input layer
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, inputs));
			// Hidden layer
			network.addLayer(new BasicLayer(new ActivationTANH(), true, hlInput));
			// Output Layer
			network.addLayer(new BasicLayer(new ActivationSoftMax(), true, outputs));
			network.getStructure().finalizeStructure();
			network.reset();

			// Read the CSV file "data.csv" into memory. Encog expects your CSV file to have
			// input + output number of columns.
			DataSetCODEC dsc = new CSVDataCODEC(new File("data.csv"), CSVFormat.ENGLISH, false, inputs, outputs, false);
			MemoryDataLoader mdl = new MemoryDataLoader(dsc);
			MLDataSet trainingSet = mdl.external2Memory();

			FoldedDataSet folded = new FoldedDataSet(trainingSet);
			MLTrain train = new ResilientPropagation(network, folded);
			CrossValidationKFold cv = new CrossValidationKFold(train, 5);

			int epoch = 0;
			int epochLimit=epochSize;
			// double minError = 0.0001;
			do {
				cv.iteration();
				epoch++;
				System.out.println(epoch + "-----------Train Error------- " + cv.getError());
			} while (epoch <epochLimit );
			
			System.out.println();
			System.out.println("The Number of Epochs are : " + epoch);
			System.out.println();

			// Save the Neural Network
			System.out.println("-------Automatic Saving Trained Network...");
			Utilities.saveNeuralNetwork(network, saveNNFile);
			System.out.println();

			

			// Test the NN
			System.out.println("---------Testing Started...");
			for (MLDataPair pair : trainingSet) {
				total++;
				MLData testOutput = network.compute(pair.getInput());
				y = (int) Math.round(testOutput.getData(2));
				yd = (int) pair.getIdeal().getData(2);
				if (y == yd) {
					correct++;
				}

				// True positive, we said they match the correct language and they do.
				tp = correct;
				// True negative, we said they don't match the correct language and they don't.
				tn = total - correct;
				// False positive, we said they match the correct language but they don't.
				fp = yd - correct;
				// False negative, we said they don't match the correct language but they do.
				fn = Math.abs(tn - 0.0);

				// sensitivity (sn) a.k.a True Positive rate TP/actual
				sn = correct / total;
				// specificity (sP) a.k.a True Negative rate TN/actual
				sp = Math.abs(tp) / total;

				// Precision: When it predicts yes, how often is it correct?
				// TP/predicted yes
				precision = tp / correct;

				// Prevalence: How often does the yes condition actually occur in our sample?
				// actual yes/total
				prevalence = correct / total;

			}
			double e = network.calculateError(trainingSet);
			System.out.println();
			System.out.println("Confusion Matrix Statistic.");
			System.out.println("Network training Error: " + e);
			System.out.println("The Acurracy is : " + String.format("%.2f", (correct / total) * 100));
			System.out.println("True Positive : " + String.format("%.2f", tp));
			System.out.println("True Negative : " + String.format("%.2f", tn));
			System.out.println("False Positive : " + String.format("%.2f", fp));
			System.out.println("False Negative : " + String.format("%.2f", fn));
			System.out.println("Sensitivity (sn) : " + String.format("%.2f", sn));
			System.out.println("Specificity (sP) : " + String.format("%.2f", sp));
			System.out.println("Precision  : " + String.format("%.2f", precision));
			System.out.println("Prevalence : " + String.format("%.2f", prevalence));
			System.out.println();

			// process text without a save model.
			VectorProcessor vp = new VectorProcessor();
			vp.processText(liveTextData, 2, vectorSize, charPerngram);
			double[] liveData1DArr = vp.liveTextVector;

			languagePrinter(network, liveData1DArr);

		} else {
			System.out.println("Loading Saved Network...");
			BasicNetwork saveNetwork = Utilities.loadNeuralNetwork(saveNNFile);
			VectorProcessor vp = new VectorProcessor();
			vp.processText(liveTextData, 2, vectorSize, charPerngram);
			double[] liveData1DArr = vp.liveTextVector;

			languagePrinter(saveNetwork, liveData1DArr);

		}

	}

	public void languagePrinter(BasicNetwork network, double[] vector) {
		MLData mlData = new BasicMLData(vector);
		MLData output = network.compute(mlData);
		double results[] = output.getData();
		Language[] langs = Language.values();
		int rlts = Utilities.getMaxIndex(results);
		System.out.println("The predicted Language  is : " + langs[rlts]);
		System.out.println();
	}

}
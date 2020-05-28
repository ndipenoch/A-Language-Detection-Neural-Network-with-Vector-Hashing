package ie.gmit.sw;

import java.io.File;
import java.util.Arrays;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

public class Utilities {

	/**
	 * Normalizes the hash vector of language k-mer / n-grams to values in a given 
	 * range. The lower and upper bounds should correspond to the activation function(s)
	 * that you are using in your neural network, e.g. Tanh, Sigmoid, etc.
	 *
	 * @param vector the array of hashed n-grams.
	 * @param lower the lower bound to squash the vector values from, eg. -1 or 0.
	 * @param upper the upper bound to squash the vector values to, e.g. 1.
	 * @return the vector of values normalized within the range [lower..upper].
	 */
    public static double[] normalize(double[] vector, double lower, double upper) {
        double[] normalized = new double[vector.length];
        double max = Arrays.stream(vector).max().getAsDouble();
        double min = Arrays.stream(vector).min().getAsDouble();
        
        for(int i=0; i<normalized.length; i++) {
            normalized[i] = (vector[i] - min)*(upper - lower)/(max - min) + lower;
        }
        return normalized;
    }
    
    
    /**
     * Saves an Encog multilayer perceptron to file. Once a neural network has been
     * trained, it can be saved and loaded again when needed. A trained neural network
     * consists of the network topology with a set of fixed weights. Thus, the file size
     * is typically very small. Assuming that you have a neural network defined as 
     * follows:
     * 
     * 		BasicNetwork network = new BasicNetwork();
     *		network.addLayer(new BasicLayer(null, true, 777));
     *		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 35));
     *		network.addLayer(.......);
     *		network.addLayer(.......);
     *		network.getStructure().finalizeStructure();
     *		network.reset();
     * 
     * you can save the network using the following syntax:
     * 		Utilities.saveNeuralNetwork(network, "language-detect.nn");
     * 
     * @param network the instance of BasicNetwork to save.
     * @param fileName the name of the file to save the network to.
     */
    public static void saveNeuralNetwork(BasicNetwork network, String fileName){
    	EncogDirectoryPersistence.saveObject(new File(fileName), network);
    }
    
    
    /**
     * Loads a trained multilayer perceptron from a file. As the neural network has already
     * been trained, the model can be deserialized and reused without further training. Use
     * this method as follows:
     *  
     *  	BasicNetwork network = Utilities.loadNeuralNetwork("language-detect.nn");
     * 
     * @param fileName the name of the file containing the serialized instance of BasicNetwork.
     * @return
     */
    public static BasicNetwork loadNeuralNetwork(String fileName){
    	return (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(fileName));
    }
    
    //Returns the index of the output layer with the highest value
    public static int getMaxIndex(double[] vector){
		double max = Double.MIN_VALUE;
		int maxIndex = -1;
		for (int i = 0; i < vector.length;i++){
			if (vector[i] > max){
				max = vector[i];
				maxIndex = i;
			}
		}
		return maxIndex; //Corresponds to the classification
    }
}
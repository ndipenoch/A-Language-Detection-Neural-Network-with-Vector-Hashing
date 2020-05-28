## A Language Detection Neural Network with Vector Hashing

Name: Mark Ndipenoch

#### OVERVIEW
When the application is launch the user is  presented with a command line interface(CLI) to enter the word “string” to read from a string, “file” to read from a file and  “exit” to exit the application.
If the user chooses either a string or file option they will be asked to enter the size of the ngram, the size of the vector and the number of epochs. There is an advisable value for each entry.
The number of epoch and the size of the size of the vector is directly propositional to the language prediction and time. The bigger the vector size or number of epochs, the longer it takes to train the network and the better the prediction. 
The application will then read data from the language text file and populate the comma separate values(CSV) file. Data from the CSV file will then be read and feed into a Basic Network of 3 layers(input, hidden and output layers).
The network will then be trained and tested using 5-fold cross-validation.
Testing is done using the same data used to train the network.
After training is complete the number of Epoch, the  error margin, accuracy and confusion matrix statistics are printed to the console.
After testing the network is saved automatically.
The predicted language is printed to the screen.
The user will then be asked if they will like to predict another language using the saved model or if they will like to trained and predict again. If the user says “yes” they will be prompted to enter the input text and the language will be predicted from the saved model and the resulted will be displayed to the user.</b><br>

#### How To Run Application
Place the file "wili-2018-Small-11750-Edited.txt" in the current directory like the jar file. </b><br>
Go to the CLI in the directory and run: java -cp ./language-nn.jar ie.gmit.sw.Runner</b><br>
PS: The Jar was compile using Java 11.1, to run the jar you must have java 11.1 or above.


## DESIGN
#### Ngram
The user can decide how many characters to fit per n-gram and they are advised to use 2.
The user also have control over the size of the vector, which is also the number of input neurons of the input layer. The  total number of n-gram is a 1:1 mapping to the input layer.
The user is advised to use 1000.
The hash values of each n-gram modulus the size of the vector is written to a CSV file. 
When writing to the CSV file, If the text is too short and can’t generate enough n-grams to fill the require vector size, the empty indexes will be filled with zeros by default.</b><br>




#### Hashing
Each n-gram is hashed by calling the hashCode() method on each n-gram.  The result is then divided by the size of the vector to get the modulus. The vector at that index is then incremented by one. That is if the answer is 4 the value at index 4 will be incremented by 1.
The vector is then normalise between 0 and 1. Between zero and 1 because I am using ActivationSigmoid as the activation function in the input layer and it evaluates only positive number.</b><br>

#### Network Topology
After studying the requirement of the project I found that the connections between the nodes do not form a cycle. So it is a feedforward neural network. I used BasicNetwork and BasicLayer classes to construct a feedforward neural networks using  Encog. Neural networks can take a considerable amount of time to train. Because of this I  save  the trained network automatically and I have provided the option to predict the language from the saved network.
The network in this project has 3 layers, the input, hidden and output layers. The number of neurons in the input and output layers do not change for a given data set. 
To make use of the neural network, the input must be expressed as an array of floating point numbers. Likewise, the solution must be an array of floating point numbers. 
Neural networks take one array and transform it into a second. Neural networks do not loop, call subroutines, or perform any of the other tasks associated with traditional programming. Neural networks recognize patterns. A neural network is much like a hash table in traditional programming. A hash table is used to map keys to values, somewhat like a dictionary.
This is explained in Programming Neural Networks with Encog3 in Java by Jeff Heaton[1] in section 0.2.1
The output layer does not have bias neurons, and the input layer does not have an activation function. This is because the bias neuron affects the next layer, and the activation function affects data coming from the previous layer. But in this application I have added ActivationSigmoid in the input layer. For some reasons it turns to increase the accuracy to detect the language.</b><br>

#### Input Layer
The input layer is the first layer in a neural network. The input to a neural network is always an array of the type double. The size of this array directly corresponds to the number of neurons on the input layer. In this project the size of the input layer is the size of the input vector. The user can decide what size they want to use. 
Encog uses the MLData interface to define classes that hold the input array. Input are presented to the neural network inside a MLData object. The BasicMLData class implements the MLData interface. The BasicMLData provides a memory-based data holder for the neural network data. 
Once the neural network processes the input, a MLData-based class will be returned from the neural network. This is explained in Programming Neural Networks with Encog3 in Java by Jeff Heaton[1] in section 1.4.1</b><br>

#### Hidden Layer
A neural network might or might not have hidden layers. Hidden layers are inserted between the input and output layer. 
The purpose of the hidden layers is to allow the neural network to better produce the expected output for the given input. Once the structure of the input and output layers is defined, the hidden layer structure that optimally learns the problem must also be defined.
The challenge is to avoid creating a hidden structure that is either too complex or too simple. Too complex of a hidden structure will take too long to train. Too simple of a hidden structure will not learn the problem. A good starting point is a single hidden layer with a number of neurons equal to twice the input layer. Depending on the network’s performance, the hidden layer’s number of neurons is either increased or decreased. 
The research on this layer  is explained in Programming Neural Networks with Encog3 in Java by Jeff Heaton[1] in section 1.4.3
Following the advice mentioned above from my research I included one hidden layer in the project and multiply the neuron by twice the input size but it was taking too long to train. It tried different combinations and concluded to use a single layer with the number of neurons equals the vector size divided by 4. 
The activation function I used in hidden layer is the ActivationTANH which is explained further in details later.</b><br>

#### Output Layer
The output layer is the final layer in a neural network. This layer provides the output after all previous layers have processed the input. The output from the output layer is formatted very similarly to the data that was provided to the input layer. The neural network outputs an array of doubles.
The neural network wraps the output in a class based on the MLData interface. Most of the built-in neural network types return a BasicMLData class as the output. However, future and third party neural network classes may return different classes based other implementations of the MLData
interface.
Neural networks are designed to accept input (an array of doubles) and then produce output (also an array of doubles). 
The research on this layer is explained in Programming Neural Networks with Encog3 in Java by Jeff Heaton[1] in section 1.4.2.
In this project the output layer has 235 neurons which is equivalent to the number of languages.
Each language is a node in the output layer.  Also the activation function used in this layer is ActivationSoftMax  which I will explain later.
Furthermore the number of neurons in the output layer is fixed as it is determined by the number of languages in the language data file.</b><br>

 #### Finalize Structure and Reset
The finalizeStructure method is called to inform the network that no more layers are to be added.
The call to reset randomizes the weights in the connections between the layers.</b><br>

#### Activation Function
Activation functions are attached to layers and are used to scale data output from a layer. Encog applies a layer’s activation function to the data that the layer is about to output. If an activation function is not specified for BasicLayer, the hyperbolic tangent activation will be the defaulted. All classes that serve as activation functions must implement the ActivationFunction
interface.
Activation function is not implemented in the input layer, because the activation function affects data coming from the previous layer.
Activation functions play a very important role in training neural networks.
There are several activation function supported by Encog. After my research on reading Programming Neural Networks with Encog3 in Java by Jeff Heaton[1] in section 4.3.
I picked out three activation functions which I implemented in this project. These activation functions are ActivationSigmoid , ActivationTANH and ActivationSoftMax.
ActivationSigmoid because it used for output that produces only positive numbers like in this project and it is faster to train.
ActivationTANH is the default activation function used in Encog if no activation function is specified. It produces both positive and negative output values but it was slower to train when implemented in this project but increase the accuracy. So I used it in the hidden layer.
The ActivationSoftMax activation function will scale all of the input values so that the sum will equal one. The ActivationSoftMax activation function is sometimes used as a hidden layer activation function.
The activation function begins by summing the natural exponent of all of the neuron outputs.
   
#### Normalization
Neural networks are designed to accept floating-point numbers as their input. Usually these input numbers should be in either the range of -1 to +1 or 0 to +1 for maximum efficiency. The choice of which range is often dictated by the choice of activation function, as certain activation functions
have a positive range and others have both a negative and positive range. This is explained in Programming Neural Networks with Encog3 in Java by Jeff Heaton[1] in section 2.2.
Normalization, causes all of the attributes to be in the same range with no one attribute more
powerful than the others. Following the advice from this research, I have scaled the normalization values between 0 and +1, since I am using ActivationSigmoid that evaluates on positive values.
Normalization, is done before the vector is written to the CSV file and it is also done before the hash values of the text to be predicted is loaded to the double array.</b><br>

## Training and Testing
#### 5-fold cross-validation
The network was trained and tested using 5-fold cross-validation as required by the project spec.
Cross-validation is a statistical method used to estimate the skill of machine learning models.
It is mainly used in settings where the goal is prediction, and one wants to estimate how accurately a predictive model will perform in practice.
In a prediction problem, a model is usually given a dataset of known data on which training is run (training dataset), and a dataset of unknown data (or first seen data) against which the model is tested (called the validation dataset or testing set).
The goal of cross-validation is to test the model's ability to predict new data that was not used in estimating it, in order to flag problems like overfitting or selection bias and to give an insight on how the model will generalize to an independent dataset (i.e., an unknown dataset, for instance from a real problem). This is explain in Wikipedia Cross-validation (statistics)[2].
The method takes 2 parameters, the train network and an integer value, k.  K refers to the number of groups that a given data sample is to be split into. As such, the procedure is often called k-fold cross-validation. When a specific value for k is chosen, it may be used in place of k in the reference to the model, such as k=10 becoming 10-fold cross-validation. 
In this project the value of k is 5.


#### Confusion Matrix
•	True positive, we said they match the correct language and they do.</b><br>
•	True negative, we said they don't match correct language and they don’t.</b><br>
•	False positive, we said the match the correct language but they don't.</b><br>
•	False negative, we said they don't match the correct language but they do.</b><br>
•	Accuracy: Overall, how often is the classifier correct?</b><br>
               (TP+TN)/total </b><br>
•	Misclassification Rate: Overall, how often is it wrong?</b><br>
               (FP+FN)/total equivalent to 1 minus Accuracy also known as "Error Rate"</b><br>
•	True Positive Rate: When it's actually yes, how often does it predict yes?</b><br>
               TP/actual also known as "Sensitivity" or "Recall"</b><br>
•	True Negative Rate: When it's actually no, how often does it predict no?</b><br>
               TN/actual equivalent to 1 minus False Positive Rate also known as "Specificity"</b><br>
•	Precision: When it predicts yes, how often is it correct?</b><br>
                 TP/predicted </b><br>
•	Prevalence: How often does the yes condition actually occur in the sample?</b><br>
                actual yes/total</b><br>

## Conclusion
I learned a lot and enjoy this project. It was challenging and fun. It was fun because I enjoyed doing it and I learned a lot of new things. It was challenging, because in my first submission attempt I couldn’t get the application to predict the correct language. The prediction was not stable and it was random all the time. To meet the dateline I did my first submission attempt, but I kept on working on the problem. Finally I got the application to predict the various languages. The application can start to predict most languages after the 6th epoch and will start to get really good after the 10th epoch. 
After the 3rd  or 4th epoch it can predict non alphanumeric languages like Greek, Japanese, Russian…ect.
The application allows the users to decide what their inputs are. User can choose to take a longer time to train and get a better result with a larger vector size and epoch.
If users prefer time over efficiency they can choose a small vector size and  a small epoch number.
In the future I will like to apply this reasoning and experience in a real life problem that can help others. I have a few ides in mind.</b><br>



### RESEARCH
1-	@misc{heaton2011programming,
 title={Programming Neural Networks with Encog3 in Java 1st Edition, Heaton Research},
  author={Heaton, J},
 year={2011},
  publisher={Inc}
}
https://s3.amazonaws.com/heatonresearch-books/free/Encog3Java-User.pdf</b><br>

2-	https://en.wikipedia.org/wiki/Cross-validation_(statistics)</b><br>




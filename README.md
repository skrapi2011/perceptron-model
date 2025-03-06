# Perceptron Model

Implementation of perceptron recognizing plants based on input vector.

Project does not use any AI framework, it is made from scratch in java using math equations to calculate weights and bias.

# Setup
To properly run project, we need to specify CLI arguments indicating:
+ alpha (learning rate) - specifies learning accuracy (0.1 - slow, 0.9 - high)
+ train set file location in .csv format (project includes simple train set named **train-set.csv**)
+ test set file location in .csv format (project also includes simple test set named **test-set.csv**)

For example, default launch options can be:
`0.4 train-set.csv test-set.csv`

# Project Settings
Project includes simple settings panel in ``Main.java`` file, where we can specify which debug info should be printed on the console:
+ GENERATIONS_ACCURACY - Prints training accuracy per generation
+ PERCEPTRON_MAPPING - Prints what plant has been assigned to which number
+ PERCEPTRON_CONFIG - Prints info about created perceptron (mapping, dimension size, starting vector, theta)
+ IRIS_CONSTRUCTOR - Prints info about each Iris created
+ TRAIN_INCORRECT - Prints info about each incorrect guess during training phase
+ TRAIN_CORRECT - Prints info about each correct guess during training phase
+ CALCULATE - Prints info about classifying process
+ SETTER - Prints info about setters calls
+ INPUT - Enables user manual vector input after training
+ LEARN - Prints info about learning process and weights change
+ TEST - Enables testing phase on test-set file, with accuracy inputs

Also, in ``Perceptron.java`` we can specify ``trainPercent`` value which indicates to which percent we want to train perceptron on given test-set. That value should be kept around 0.8-0.95 (allows for error margin)

Now, everything is configured and ready to train!

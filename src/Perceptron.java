import java.util.*;

public class Perceptron {

    private final static Map<String,Boolean> s = Main.settings;

    private final double trainPercent = 0.95;
    // Perceptron data
    private List<Double> weightVector = new ArrayList<>();
    private double theta;
    private final double alpha;
    private final int dimension;
    private final List<Iris> trainData;

    /*  Decision table
        1 - Versicolor
        0 - Virginica
    */
    private Map.Entry<String,Integer> oneValue;
    private Map.Entry<String,Integer> zeroValue;

    // Constructor
    public Perceptron(List<Iris> trainData, double alpha){
        this.trainData = trainData;
        this.dimension = Main.dimension;
        this.alpha = alpha;
        init();
    }

    // Starts perceptron configuration
    public void init(){
        initVector();
        map();
        if(s.get("PERCEPTRON_CONFIG")) System.out.println("[PERCEPTRON] Created with "+dimension+" dimensions. \n[PERCEPTRON] Vector: "+weightVector);
        if(s.get("PERCEPTRON_CONFIG")) System.out.println("[PERCEPTRON] Theta: "+theta);
    }

    // Training phase
    public void train(){
        double accuracy = 0;
        if(s.get("TRAIN_CORRECT") || s.get("TRAIN_INCORRECT") || s.get("GENERATIONS_ACCURACY")) System.out.println("============TRAINING PHASE============");
        int correct;
        int i = 0;
        while(accuracy <= trainPercent) {
            correct = 0;
            i++;
            for (Iris trainIris : trainData) {
                // 1 or 0
                int trueValue = trainIris.getNumber();
                int value = calculateOutput(trainIris.getVector());

                if (trueValue == value) {
                    // Correct
                    correct++;
                    if(s.get("TRAIN_CORRECT")) System.out.println("[TRAIN] CORRECT FOR: " + trainIris.getType() + " d: " + trueValue + " y: " + value);
                } else {
                    // Classified incorrectly, training needed
                    if(s.get("TRAIN_INCORRECT")) System.out.println("[TRAIN] INCORRECT FOR:\n" + trainIris.getType() + " d: " + trueValue + " y: " + value+"\nV: "+trainIris.getVector());
                    learnWeights(trainIris.getVector(), trueValue, value);
                }
            }
            accuracy = correct/(double)trainData.size();
            if(s.get("GENERATIONS_ACCURACY")) System.out.println("[TRAIN] GENERATION #"+i+" with accuracy: "+accuracy*100 +" %");
       }
    }

    // Testing phase
    public void test(List<Iris> testData){
        if(s.get("TEST")) System.out.println("==================TEST PHASE==================");

        int oneValueCorrect = 0, zeroValueCorrect = 0;
        double dataSizePerType = (double)testData.size()/2;
        int trueValue, value;

        // Mapping values for verification
        for(Iris mapIris : testData){
            // versicolor  1, virginica - 0
            if(mapIris.getType().equals(oneValue.getKey())){
                mapIris.setNumber(1);
            }else{
                mapIris.setNumber(0);
            }
        }

        for(Iris testIris : testData){
            trueValue = testIris.getNumber();
            // testing
            value = calculateOutput(testIris.getVector());

            // If input correct
            if(trueValue == value){
                if(testIris.getType().equals(oneValue.getKey())){
                    if(s.get("TEST")) System.out.println("[TEST] "+testIris.getType()+" trueVal:"+trueValue+" output: "+value);
                    // for versicolor - 1
                    oneValueCorrect++;
                }else{
                    if(s.get("TEST")) System.out.println("[TEST] "+testIris.getType()+" trueVal:"+trueValue+" output: "+value);
                    // for virginica - 0
                    zeroValueCorrect++;
                }
            }else{
                if(s.get("TEST")) System.out.println("[TEST]!"+testIris.getType()+" trueVal:"+trueValue+" value: "+value);
            }
        }

        if(s.get("TEST")) {
            System.out.println("==================TEST RESULT==================");
            // for versicolor - 1
            System.out.println("| "+oneValue.getKey() + " accuracy: " + (oneValueCorrect / dataSizePerType) * 100 + "% ("+oneValueCorrect+"/"+(int)dataSizePerType+")");
            // for virginica - 0
            System.out.println("| "+zeroValue.getKey() + " accuracy: " + (zeroValueCorrect / dataSizePerType) * 100 + "% ("+zeroValueCorrect+"/"+(int)dataSizePerType+")");
        }

    }

    // Testing single vector
    public void testSingle(List<Double> inputVector){
        int result = calculateOutput(inputVector);
        // for example 1 == 1
        if(result == oneValue.getValue()){
            System.out.println("Recognized as: "+oneValue.getKey());
        }else{
            System.out.println("Recognized as: "+zeroValue.getKey());
        }
    }

    // Re-balancing weights based on input Vector
    public void learnWeights(List<Double> inputVector, int d,int y){
        // (d-y)*alpha
        double newAlpha = (d-y)*alpha;
        double newTheta;
        List<Double> newWeights = new ArrayList<>();
        List<Double> newInputVector = new ArrayList<>(inputVector);

        // Applying -1 to vector
        newInputVector.add(-1.0);
        if(s.get("LEARN")) System.out.println("[LEARN] ADDING -1: "+newInputVector);

        // Multiplying (d-y)aX in X
        for(int j=0;j<dimension+1;j++){
            newInputVector.set(j,newInputVector.get(j)*newAlpha);
        }

        if(s.get("LEARN")) System.out.println("[LEARN] INPUT MULTIPLIED: "+newInputVector);

        // Adding X + W, without theta+lastIndex
        for(int i=0;i<dimension;i++){
            newWeights.add(
                    weightVector.get(i) + newInputVector.get(i)
            );
        }

        // Calculating theta+lastIndex
        newTheta = newInputVector.get(newInputVector.size()-1) + theta;
        setWeightVector(newWeights);
        setTheta(newTheta);
    }

    // Calculating perceptron output
    public int calculateOutput(List<Double> inputVector){
        double scalar = 0;
        for(int i=0;i<dimension;i++){
            scalar += inputVector.get(i)*weightVector.get(i);
        }
        if(s.get("CALCULATE")) System.out.println("[CALCULATE] CALCULATING: "+inputVector+" == "+weightVector +" SCALAR: "+scalar);
        return scalar > theta ? 1 : 0;
    }

    // Mapping types
    public void map(){
        // Mapping values
        trainData.forEach(trainIris -> {
            if(oneValue == null){
                oneValue = new AbstractMap.SimpleEntry<>(trainIris.getType(), 1);
            }else if(!trainIris.getType().equals(oneValue.getKey())) {
                zeroValue = new AbstractMap.SimpleEntry<>(trainIris.getType(), 0);
            }
        });

        for(Iris trainIris : trainData){
            if(trainIris.getType().equals(oneValue.getKey())){
                trainIris.setNumber(1);
                if(s.get("PERCEPTRON_MAPPING")) System.out.println("Mapped 1 for: "+trainIris.getType()+" V: "+trainIris.getVector()+" : "+trainIris.getNumber());
            }else{
                trainIris.setNumber(0);
                if(s.get("PERCEPTRON_MAPPING")) System.out.println("Mapped 0 for: "+trainIris.getType()+" V: "+trainIris.getVector()+" : "+trainIris.getNumber());
            }
        }
        if(s.get("PERCEPTRON_CONFIG")) System.out.println("[PERCEPTRON] Mapped as: ["+oneValue+","+zeroValue+"]");
    }

    // Generating starting weights
    public void initVector(){
        Random rand = new Random();
        for(int i=0;i<dimension;i++){
            weightVector.add(
                    (rand.nextDouble()*20) - 10
            );
        }
        theta = rand.nextDouble();
    }

    // Setter for weightVector
    public void setWeightVector(List<Double> newVector){
        this.weightVector = newVector;
        if(s.get("SETTER")) System.out.println("[SETWEIGHT] NEW WEIGHT SET: "+weightVector);
    }

    // Setter for theta
    public void setTheta(double newTheta){
        theta = newTheta;
        if(s.get("SETTER")) System.out.println("[SETTHETA] THETA SET AS: "+theta);
    }

}
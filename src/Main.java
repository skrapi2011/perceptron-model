import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static int dimension;
    public static boolean dimensionChecked = false;
    public static final Scanner SC = new Scanner(System.in);

    public static Map<String,Boolean> settings = new HashMap<String,Boolean>(){{
       put("GENERATIONS_ACCURACY", true);
       put("PERCEPTRON_MAPPING", false);
       put("PERCEPTRON_CONFIG", true);
       put("IRIS_CONSTRUCTOR", false);
       put("TRAIN_INCORRECT", false);
       put("TRAIN_CORRECT", false);
       put("CALCULATE", false);
       put("SETTER", false);
       put("INPUT", true);
       put("LEARN", false);
       put("TEST", true);
    }};


    public static void main(String[] args) throws Exception {

        List<Iris> trainData, testData;
        Perceptron perc;

        // 3 args
        if(args.length == 3){
            final double alpha = Double.parseDouble(args[0]);
            final String trainPath = args[1];
            final String testPath = args[2];
            trainData = readData(trainPath);
            testData = readData(testPath);

            // Applying trainData to perceptron
            perc = new Perceptron(trainData, alpha);

        }else{
            throw new Exception("Missing arguments! provided: "+args.length);
        }


        // Training
        perc.train();

        // Testing
        perc.test(testData);

        // User input
        if(settings.get("INPUT")) {
            // User vector
            while(true) {
                List<Double> userVector = new ArrayList<>();

                System.out.print("Input vector: ");
                String[] line = SC.nextLine().split(",");
                for (String s : line) {
                    userVector.add(Double.parseDouble(s));
                }

                if (userVector.size() == dimension) {
                    perc.testSingle(userVector);
                } else {
                    System.out.println("Wrong dimension! Needed: " + dimension);
                }

            }
        }
    }

    // Reads Iris files
    public static List<Iris> readData(String Path) throws FileNotFoundException {

        // Variables
        File dataFile = new File(Path);
        Scanner sc = new Scanner(dataFile);
        List<Iris> testData = new ArrayList<>();

        // Reading phase
        while(sc.hasNextLine()){

            // Splitting line
            String[] lineSplit = sc.nextLine().split(",");

            // Checking dimension
            if(!dimensionChecked){
                dimension = lineSplit.length-1;
                dimensionChecked = true;
            }

            // Creating vector
            List<Double> vector = new ArrayList<Double>();

            // Type is last index of lineSplit array
            String type = lineSplit[lineSplit.length-1];

            // Cutting invisible whitespace character
            if((int)lineSplit[0].charAt(0) == 65279){
                lineSplit[0] = lineSplit[0].substring(1);
            }
            for(int i=0;i<dimension; i++){
                vector.add(Double.parseDouble(lineSplit[i]));
            }
            testData.add(
                    new Iris(vector,type)
            );
        }

        return testData;
    }

}
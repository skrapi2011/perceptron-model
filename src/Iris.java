import java.util.List;

public class Iris {

    private int number;
    private final String type;
    private final List<Double> vector;

    public Iris(List<Double> vector, String type){
        this.vector = vector;
        this.type = type;
        if(Main.settings.get("IRIS_CONSTRUCTOR")) System.out.print("Iris created! " + type + " : ");
        if(Main.settings.get("IRIS_CONSTRUCTOR")) printVector();
    }

    public String getType(){
        return type;
    }

    public List<Double> getVector(){
        return vector;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    private void printVector(){
        System.out.print("[ ");
        for(double i : vector){
            System.out.print(i + " ");
        }
        System.out.println("]");
    }
}
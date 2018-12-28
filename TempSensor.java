public class TempSensor{

    private int ambientTemp;
    private int threshold;
    private String sensorName;

    public TempSensor(){
    } //end TempSensor()

    public TempSensor(int ambient, int thresh, String name){
        ambientTemp = ambient;
        threshold = thresh;
        sensorName = name;
    } //end TempSensor(all info)

    public int getTemp(){
        return ambientTemp;
    } //end getTemp()

    public int getThreshold(){
        return threshold;
    } //end getThreshold()

    public void setThreshold(int newThresh){
        threshold = newThresh;
    } //end setThreshold()

    public String getName(){
        return sensorName;
    } //end getName()

    public void setName(String newName){
        sensorName = newName;
    } //end setName()

} //end TempSensor

import java.util.concurrent.ThreadLocalRandom;

public class SimulatedTempSensor extends TempSensor{

    private int ambientTemp;
    private int threshold;
    private String sensorName;

    // default fills with basic data
    public SimulatedTempSensor(){
        ambientTemp = 72;
        threshold = ambientTemp + 100;
        sensorName = "Sensor";
    } //end SimulatedTempSensor(no info)

    // allows for random generation from AlertSystem test data
    public SimulatedTempSensor(int ambient, String name){
        ambientTemp = ambient;
        threshold = ambientTemp + 100;
        sensorName = name;
    } //end SimulatedTempSensor(random from AlertSystem test)

    public int getTemp(){
        int randomNum = ThreadLocalRandom.current().nextInt(1,11);
        int increaseIf = 7; // out of 10
        if (randomNum > increaseIf){
            ambientTemp += 5;
            // if already 100, increase faster
            if (ambientTemp > 100){
                ambientTemp += 5;
            }
        }
        return ambientTemp;
    } //end getTemp()

} //end SimulatedTempSensor

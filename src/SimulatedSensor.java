/**
 * Class which extends the functionality of a non-flammable item to make it work as a sensor.
 */
public class SimulatedSensor extends NonFlammableItem implements Sensor {

   /** Base values for the sensor. */
   private static final String DEFAULT_SENSOR_NAME = "sensor";
   private static final int DEFAULT_ALARM_THRESHOLD = 100;

   /** Temp at which alarm will go off */
   private final int alarmThreshold;

   /** Whether alarm has gone off */
   private boolean alarmed;

   /** Base constructor for FireAlarm objects. */
   public SimulatedSensor() {
      super(DEFAULT_SENSOR_NAME);
      alarmThreshold = DEFAULT_ALARM_THRESHOLD;
   } // end FireAlarm()

   /**
    * Gets the temperature at which the sensor will go off.
    * @return the threshold the alarm is set to
    */
   public int getAlarmThreshold() {
      return alarmThreshold;
   } // end getAlarmThreshold()

   /**
    * Checks if the alarm has been alarmed.
    * @return the value held by 'alarmed'
    */
   public boolean isAlarmed() {
      return alarmed;
   } // end isAlerted()

   /** Lets the alarm know that it has been triggered */
   public void triggerAlarm() {
      alarmed = true;
   } // end triggerAlarm()

   /** Turns off the alarm */
   public void stopAlarm() {
      alarmed = false;
   } // end stopAlarm()

} // end FireAlarm
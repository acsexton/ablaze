/**
 * Class which extends the functionality of a non-flammable item to make it work as a sensor.
 */
public class FireAlarm extends NonFlammableItem implements Sensor {

   /** Base values for the sensor. */
   private static final String DEFAULT_SENSOR_NAME = "sensor";
   private static final int DEFAULT_ALARM_THRESHOLD = 100;

   private int alarmThreshold;   // The temperature at which the alarm will go off
   private boolean alerted;      // True if the sensor has gone off

   /** Base constructor for FireAlarm objects. */
   public FireAlarm() {
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
    * Checks if the alarm has been alerted.
    * @return the value held by 'alerted'
    */
   public boolean isAlerted() {
      return alerted;
   } // end isAlerted()

   /** Lets the alarm know that it has been triggered */
   public void triggerAlarm() {
      alerted = true;
   } // end triggerAlarm()

   /** Turns off the alarm */
   public void stopAlarm() {
      alerted = false;
   } // end stopAlarm()

} // end FireAlarm
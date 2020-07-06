/** Interface for the necessary functions of an alarm sensor */
public interface Sensor {

   // Default info
   static final String DEFAULT_SENSOR_NAME = "sensor";
   static final int DEFAULT_ALARM_THRESHOLD = 100;

   /** Return the threshold for the sensor to go into 'alarm' state */
   public int getAlarmThreshold();

   /** Return whether the sensor has been alarmed */
   public boolean isAlarmed();

   /** Starts the alert */
   public void triggerAlarm();

   /** Stops the alert */
   public void stopAlarm();

}

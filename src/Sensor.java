public interface Sensor {

   static final String DEFAULT_SENSOR_NAME = "sensor";
   static final int DEFAULT_ALARM_THRESHOLD = 100;

   public int getAlarmThreshold();

   public boolean isAlerted();

   public void triggerAlarm();

   public void stopAlarm();

}

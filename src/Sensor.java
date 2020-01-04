public class Sensor extends NonFlammableItem {

   private static final String DEFAULT_SENSOR_NAME = "sensor";
   private static final int DEFAULT_ALARM_THRESHOLD = 100;

   private int alarmThreshold;
   private boolean alerted;

   public Sensor(){
      super(DEFAULT_SENSOR_NAME);
      alarmThreshold = DEFAULT_ALARM_THRESHOLD;
   }

   public int getAlarmThreshold() {
      return alarmThreshold;
   }

   public boolean isAlerted() {
      return alerted;
   }

   public void triggerAlarm(){
      alerted = true;
   }

   public void stopAlarm(){
      alerted = false;
   }

   public void updateStatus(){
      alerted = getCurrentTemperature() >= alarmThreshold;
   }

}
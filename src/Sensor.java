public class Sensor extends WorldItem {

   private static final String DEFAULT_SENSOR_NAME = "sensor";
   private static final boolean DEFAULT_SENSOR_FLAMMABILITY = false;
   private static final int DEFAULT_ALARM_THRESHOLD = 100;

   private int alarmThreshold;
   private boolean alerted;

   public Sensor(){
      super(DEFAULT_SENSOR_NAME, DEFAULT_SENSOR_FLAMMABILITY);
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
      alerted = this.getCurrentTemperature() >= alarmThreshold;
   }

}

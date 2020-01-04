public abstract class WorldItem {

   private static final int DEFAULT_TEMP = 72;

   public String name;
   private int currentTemp;

   public WorldItem(String name){
      this.name = name;
      currentTemp = DEFAULT_TEMP;
   }

   public int getCurrentTemperature() {
      return currentTemp;
   }

   public void resetTemp() {
      currentTemp = DEFAULT_TEMP;
   }

   public String getName() {
      return name;
   }

   public void setCurrentTemp(int newTemp){
      currentTemp = newTemp;
   }

   public void updateTemp(){
      // TODO: Temperature calculation calls here
   }

   public void updateStatus(){
      // To be overridden as useful for other objects
   }

}
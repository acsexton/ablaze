public class WorldItem {

   private static final int DEFAULT_COMBUSTION_THRESHOLD = 451;
   private static final int DEFAULT_TEMP = 72;

   public String name;
   private boolean flammable;
   private boolean onFire;
   private int currentTemp;
   private int combustionThreshold;

   public WorldItem(String name, boolean flammable){
      this.name = name;
      this.flammable = flammable;

      this.onFire = false;
      currentTemp = DEFAULT_TEMP;
      combustionThreshold = DEFAULT_COMBUSTION_THRESHOLD;
   }

   public int getCurrentTemperature() {
      return currentTemp;
   }

   public boolean isFlammable(){
      return flammable;
   }

   public boolean isOnFire(){
      return onFire;
   }

   public int getCombustionThreshold(){
      return combustionThreshold;
   }

   public String getName() {
      return name;
   }

   public void setCurrentTemp(int newTemp){
      currentTemp = newTemp;
   }

   public void setOnFire(boolean setFire){
      onFire = setFire;
   }

   public void quench(){
      onFire = false;
      currentTemp = DEFAULT_TEMP;
   }

   public void updateStatus(){
      if (!onFire) {
         if (flammable) {
            onFire = (currentTemp >= combustionThreshold);
         }
      }
   }

}
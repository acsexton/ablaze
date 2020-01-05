public class FlammableItem extends WorldItem {

   private static final int DEFAULT_COMBUSTION_THRESHOLD = 451;

   private boolean onFire;
   private int combustionThreshold;

   public FlammableItem(String name){
      super(name);
      this.onFire = false;
      combustionThreshold = DEFAULT_COMBUSTION_THRESHOLD;
   }

   public boolean isOnFire(){
      return onFire;
   }

   public int getCombustionThreshold(){
      return combustionThreshold;
   }

   public void ignite(){
      onFire = true;
   }

   public void quench(){
      onFire = false;
      resetTemp();
   }

   public void updateStatus(){
      if (!onFire) {
         onFire = (getCurrentTemperature() >= combustionThreshold);
      }
   }

}

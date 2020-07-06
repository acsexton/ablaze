/**
 * Represents an item which may catch fire
 */
public class FlammableItem extends WorldItem {

   /** The default temperature required for the given item to catch fire and become another source
    *  of heat in the system */
   private static int DEFAULT_COMBUSTION_THRESHOLD = 451;

   /** Whether the item is on fire */
   private boolean onFire;

   /** The temperature required for the given item to catch fire and become another source of
    * heat in the system */
   private final int combustionThreshold;

   /** Constructor -- items do not initialize as being on fire */
   public FlammableItem(String name) {
      super(name);
      this.onFire = false;
      combustionThreshold = DEFAULT_COMBUSTION_THRESHOLD;
   }

   public boolean isOnFire() {
      return onFire;
   }

   public int getCombustionThreshold() {
      return combustionThreshold;
   }

   public void ignite() {
      onFire = true;
   }

   public void quench() {
      onFire = false;
   }

}

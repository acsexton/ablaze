/** Represents an item in the world */
public abstract class WorldItem {

   private final String name;

   public WorldItem(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

}
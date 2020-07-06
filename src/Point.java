/**
 * Represents all points in the room's grid
 */
public class Point {

   /** The default temperature for each point in the room */
   private static final int DEFAULT_TEMP = 72;

   // Location and item data
   private final int row;
   private final int column;
   private double currentTemp;

   /** The item stored at the given point */
   private WorldItem containedItem;

   /** Constructor for a point in the room, associating it with an item */
   public Point(int row, int column, WorldItem containedItem) {
      this.row = row;
      this.column = column;
      this.currentTemp = DEFAULT_TEMP;
      this.containedItem = containedItem;
   }

   public int getRow() {
      return row;
   }

   public int getColumn() {
      return column;
   }

   public double getCurrentTemp() {
      return currentTemp;
   }

   /** Allows for the point in the room to be set back to default for debug purposes */
   public void resetTemp() {
      currentTemp = DEFAULT_TEMP;
   }

   public void setCurrentTemp(double newTemp) {
      currentTemp = newTemp;
   }

   public void setContainedItem(WorldItem containedItem) {
      this.containedItem = containedItem;
   }

   public WorldItem getContainedItem() {
      return containedItem;
   }

   public void update() {
   }

}

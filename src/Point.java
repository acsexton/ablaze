public class Point {

   private static final int DEFAULT_TEMP = 72;

   // Location and item data
   private final int row;
   private final int column;
   private double currentTemp;
   private WorldItem containedItem;

   public Point(int row, int column, WorldItem containedItem){
      this.row = row;
      this.column = column;
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

   public void resetTemp() {
      currentTemp = DEFAULT_TEMP;
   }

   public void setCurrentTemp(double newTemp){
      currentTemp = newTemp;
   }

   public void setContainedItem(WorldItem containedItem){
      this.containedItem = containedItem;
   }

   public WorldItem getContainedItem(){
      return containedItem;
   }

   public void update(){
   }

}

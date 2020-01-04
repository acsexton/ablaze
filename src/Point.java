public class Point {

   private final int row;
   private final int column;
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

   public void placeItem(WorldItem containedItem){
      this.containedItem = containedItem;
   }

   public WorldItem getContainedItem(){
      return containedItem;
   }

}

public class Point {

   private final int xCoord;
   private final int yCoord;
   private WorldItem containedItem;

   public Point(int xCoord, int yCoord, WorldItem containedItem){
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      this.containedItem = containedItem;
   }

   public int getxCoord() {
      return xCoord;
   }

   public int getyCoord() {
      return yCoord;
   }

   public void placeItem(WorldItem containedItem){
      this.containedItem = containedItem;
   }

   public WorldItem getContainedItem(){
      return containedItem;
   }

}

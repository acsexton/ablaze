public class Room extends WorldItem {

   private int rows;
   private int columns;
   private int flammableItemCount;
   private int itemsOnFire;
   private Point[][] roomPoints;

   public Room(String roomName, int rows, int columns){
      super(roomName);
      this.rows = rows+1;
      this.columns = columns+1;
      this.flammableItemCount = 0;
      this.itemsOnFire = 0;
      roomPoints = new Point[this.rows][this.columns];
      fillRoomWithAir();
   }

   public Point getPointAtLocation(int row, int column){
      return roomPoints[row][column];
   }

   public WorldItem getItemAtLocation(int row, int column){
      Point checkPoint = getPointAtLocation(row, column);
      return checkPoint.getContainedItem();
   }

   private void fillRoomWithAir(){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Air emptySpace = new Air();
            roomPoints[i][j] = new Point(i, j, emptySpace);
         }
      }
   }

   public int getRows(){
      return rows;
   }

   public int getColumns() {
      return columns;
   }

   public void placeItemInRoomAtCoords(WorldItem item, int row, int column){
      Point targetPoint = getPointAtLocation(row, column);
      targetPoint.placeItem(item);
      if (item instanceof FlammableItem){
         flammableItemCount++;
      }
   }

   public void updateStatus(){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            WorldItem item = getItemAtLocation(i, j);
            item.updateTemp();
            item.updateStatus();
            if (item instanceof FlammableItem){
               if (((FlammableItem) item).isOnFire()){
                  itemsOnFire++;
               }
            }
         }
      }
   }

   public boolean isAllBurntUp(){
      return (flammableItemCount == itemsOnFire);
   }

   // TODO: Clean this up
   public String toString(){
      String output = "";
      for (int i = 0; i < rows; i++){
         for (int j = 0; j < columns; j++){
            WorldItem item = getItemAtLocation(i, j);
            output += "|";
            if (item instanceof Air){
               output += " ";
            }
            else if (item instanceof FireAlarm){
               if (((FireAlarm) item).isAlerted()){
                  output += "!";
               } else {
                  output += "s";
               }
            }
            else{
               if (item instanceof FlammableItem){
                  if (((FlammableItem) item).isOnFire()){
                     output += "^";
                  } else {
                     output += "x";
                  }
               } else {
                  output += "x";
               }
            }
            output += "|";
         }
         output += "\n";
      }
      return output;
   }

}

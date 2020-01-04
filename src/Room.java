public class Room extends WorldItem {

   private int xSize;
   private int ySize;
   private Point[][] roomPoints;

   public Room(String roomName, int xSize, int ySize){
      super(roomName);
      this.xSize = xSize;
      this.ySize = ySize;
      roomPoints = new Point[xSize][ySize];
      fillRoomWithAir();
   }

   public boolean checkPointExists(int xCoord, int yCoord){
      return (xCoord < xSize && yCoord <= ySize);
   }

   public Point getPointAtCoords(int xCoord, int yCoord){
      if (checkPointExists(xCoord, yCoord)){
         return roomPoints[xCoord][yCoord];
      } else {
         return null;
      }
   }

   private void fillRoomWithAir(){
      for (int i = 0; i < xSize; i++) {
         for (int j = 0; j < ySize; j++) {
            Air emptySpace = new Air();
            roomPoints[i][j] = new Point(i, j, emptySpace);
         }
      }
   }

   public int getxSize(){
      return xSize;
   }

   public int getySize() {
      return ySize;
   }

   // TODO: This'll need validation
   public WorldItem findItem(String search){
      for (int i = 0; i < xSize; i++) {
         for (int j = 0; j < ySize; j++) {
            Point updatePoint = getPointAtCoords(i, j);
            WorldItem item = updatePoint.getContainedItem();
            if (item.getName().equals(search)){
               return item;
            }
         }
      }
      return null;
   }

   public void placeItemInRoom(String name, Point roomCoords){
      int x = roomCoords.getxCoord();
      int y = roomCoords.getyCoord();
      if (x <= xSize && y <= ySize){
         WorldItem newItem = new FlammableItem(name);
         roomPoints[x][y].placeItem(newItem);
      }
   }

   public void updateStatus(){
      for (int i = 0; i < xSize; i++) {
         for (int j = 0; j < ySize; j++) {
            Point updatePoint = getPointAtCoords(i, j);
            WorldItem item = updatePoint.getContainedItem();
            item.updateTemp();
            item.updateStatus();
         }
      }
   }

   // TODO: Clean this up
   public String toString(){
      String output = "";
      for (int i = 0; i < xSize; i++){
         for (int j = 0; j < ySize; j++){
            Point updatePoint = getPointAtCoords(i, j);
            WorldItem item = updatePoint.getContainedItem();
            output += "|";
            if (item instanceof Air){
               output += " ";
            }
            else if (item instanceof Sensor){
               if (((Sensor) item).isAlerted()){
                  output += "!";
               } else {
                  output += "?";
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

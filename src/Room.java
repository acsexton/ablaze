public class Room extends WorldItem {

   private int xSize;
   private int ySize;
   private WorldItem[][] roomSpots;

   private static final boolean DEFAULT_ROOM_FLAMMABILITY = false;

   public Room(String roomName, int xSize, int ySize){
      super(roomName, DEFAULT_ROOM_FLAMMABILITY);
      this.xSize = xSize;
      this.ySize = ySize;
      roomSpots = new WorldItem[xSize][ySize];
      fillRoomWithAir();
   }

   private void fillRoomWithAir(){
      for (int i = 0; i < xSize; i++) {
         for (int j = 0; j < ySize; j++) {
            Air emptySpace = new Air();
            roomSpots[i][j] = emptySpace;
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
            WorldItem check = getItem(i, j);
            if (check.getName().equals(search)){
               return check;
            }
         }
      }
      return null;
   }

   // TODO: Validate location check
   public void placeItem(String name, boolean flammable, int xCoord,
                         int yCoord){
      if (xCoord <= xSize && yCoord <= ySize){
         WorldItem newItem = new WorldItem(name, flammable);
         roomSpots[xCoord][yCoord] = newItem;
      }
   }

   // TODO: Validate location check
   public WorldItem getItem(int xCoord, int yCoord) {
      return roomSpots[xCoord][yCoord];
   }

   public void updateStatus(){
      for (int i = 0; i < xSize; i++) {
         for (int j = 0; j < ySize; j++) {
            getItem(i, j).updateStatus();
            // TODO: This is where calculations are called
         }
      }
   }

   // TODO: Clean this up
   public String toString(){
      String output = "";
      for (int i = 0; i < xSize; i++){
         for (int j = 0; j < ySize; j++){
            WorldItem item = getItem(i, j);
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
               if (item.isOnFire()){
                  output += "^";
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

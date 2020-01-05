public class Room extends WorldItem {

   private int rows;
   private int columns;
   private Point[][] roomPoints;

   public Room(String roomName, int rows, int columns){
      super(roomName);
      this.rows = rows+1;
      this.columns = columns+1;
      roomPoints = new Point[this.rows][this.columns];
      fillRoomWithAir();
   }

   public Point getPointAtLocation(int row, int column){
      return roomPoints[row][column];
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

   // TODO: This'll need validation
   public WorldItem findItem(String search){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point updatePoint = getPointAtLocation(i, j);
            WorldItem item = updatePoint.getContainedItem();
            if (item.getName().equals(search)){
               return item;
            }
         }
      }
      return null;
   }

   public void placeItemInRoomAtCoords(WorldItem item, int row, int column){
      Point targetPoint = getPointAtLocation(row, column);
      targetPoint.placeItem(item);
   }

   public void updateStatus(){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point updatePoint = getPointAtLocation(i, j);
            WorldItem item = updatePoint.getContainedItem();
            item.updateTemp();
            item.updateStatus();
         }
      }
   }

   // TODO: Clean this up
   public String toString(){
      String output = "";
      for (int i = 0; i < rows; i++){
         for (int j = 0; j < columns; j++){
            Point updatePoint = getPointAtLocation(i, j);
            WorldItem item = updatePoint.getContainedItem();
            output += "|";
            if (item instanceof Air){
               output += " ";
            }
            else if (item instanceof FireAlarm){
               if (((FireAlarm) item).isAlerted()){
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

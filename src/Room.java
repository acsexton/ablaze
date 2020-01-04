public class Room extends WorldItem {

   private int rows;
   private int columns;
   private Point[][] roomPoints;

   public Room(String roomName, int rows, int columns){
      super(roomName);
      this.rows = rows;
      this.columns = columns;
      roomPoints = new Point[columns][rows];
      fillRoomWithAir();
   }

   public boolean pointExists(int row, int column){
      return (row <= rows && column <= columns);
   }

   public Point getPointAtCoords(int row, int column){
      if (pointExists(row, column)){
         return roomPoints[column][row];
      } else {
         return null;
      }
   }

   private void fillRoomWithAir(){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Air emptySpace = new Air();
            roomPoints[j][i] = new Point(j, i, emptySpace);
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
            Point updatePoint = getPointAtCoords(i, j);
            WorldItem item = updatePoint.getContainedItem();
            if (item.getName().equals(search)){
               return item;
            }
         }
      }
      return null;
   }

   public void placeItemInRoomAtCoords(WorldItem item, int row, int column){
      if (pointExists(row, column)){
         Point targetPoint = getPointAtCoords(row, column);
         targetPoint.placeItem(item);
      }
   }

   public void updateStatus(){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
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
      for (int i = 0; i < rows; i++){
         for (int j = 0; j < columns; j++){
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

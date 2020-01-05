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

   public void updateIgnition(Point point, FlammableItem flammable){
      if (!flammable.isOnFire()){
         if (point.getCurrentTemperature() >= flammable.getCombustionThreshold()) {
            flammable.ignite();
            itemsOnFire++;
         }
      }
   }

   public void updateAlarm(Point point, FireAlarm alarm){
      if (!alarm.isAlerted()){
         if (point.getCurrentTemperature() >= alarm.getAlarmThreshold()){
            alarm.triggerAlarm();
         }
      } else if (point.getCurrentTemperature() < alarm.getAlarmThreshold()){
         alarm.stopAlarm();
      }
   }

   public void update(){
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point point = getPointAtLocation(i, j);
            WorldItem item = getItemAtLocation(i, j);
            point.update();
            if (item instanceof FlammableItem){
               updateIgnition(point, (FlammableItem) item);
            } else if (item instanceof FireAlarm){
               updateAlarm(point, (FireAlarm) item);
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

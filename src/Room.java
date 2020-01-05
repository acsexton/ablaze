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
      targetPoint.setContainedItem(item);
      if (item instanceof FlammableItem){
         flammableItemCount++;
      }
   }

   public void updateIgnition(Point point, FlammableItem flammable){
      if (!flammable.isOnFire()){
         if (point.getCurrentTemp() >= flammable.getCombustionThreshold()) {
            flammable.ignite();
            itemsOnFire++;
         }
      }
   }

   public void updateAlarm(Point point, FireAlarm alarm){
      if (!alarm.isAlerted()){
         if (point.getCurrentTemp() >= alarm.getAlarmThreshold()){
            alarm.triggerAlarm();
         }
      } else if (point.getCurrentTemp() < alarm.getAlarmThreshold()){
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

   public void calculatePointTemps(Point point){
      WorldItem containedItem = point.getContainedItem();

      // Maintain temperature if the item here is flammable and on fire
      if (containedItem instanceof FlammableItem){
         if (((FlammableItem) containedItem).isOnFire()){
            return;
         }
      }

      // 1 kW raises temp by 100deg C assuming almost no air flow
      int kWDegreeIncrease = 100;

      // Temperature factors
      int radQDot = calcRadQDot(point);
      int convQDot = calcConvQDot(point);
      int totalQDot = radQDot + convQDot;

      point.setCurrentTemp(kWDegreeIncrease*totalQDot);
   }

   public int calcRadQDot(Point point){
      return 1;
   }

   public int calcConvQDot(Point point){
      return 1;
   }

   public Point[] getSpacesOneAway(Point point){
      int col = point.getColumn();
      int row = point.getRow();
      int surrounding = 7;

      Point[] oneAway = new Point[surrounding];

      // Clockwise around point
      Point topLeft = getPointAtLocation(row+1, col-1);
      if (topLeft != null){
         oneAway[0] = topLeft;
      }
      Point top = getPointAtLocation(row+1, col);
      if (top != null){
         oneAway[1] = top;
      }
      Point topRight = getPointAtLocation(row+1, col+1);
      if (topRight != null){
         oneAway[2] = topRight;
      }
      Point left = getPointAtLocation(row, col-1);
      if (left != null){
         oneAway[3] = left;
      }
      Point right = getPointAtLocation(row, col+1);
      if (right != null){
         oneAway[4] = right;
      }
      Point bottomLeft = getPointAtLocation(row-1, col-1);
      if (bottomLeft != null){
         oneAway[5] = bottomLeft;
      }
      Point bottom = getPointAtLocation(row-1, col);
      if (bottom != null) {
         oneAway[6] = bottom;
      }
      Point bottomRight = getPointAtLocation(row-1, col+1);
      if (bottomRight != null) {
         oneAway[7] = bottomRight;
      }

      return oneAway;
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

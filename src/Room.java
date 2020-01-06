public class Room extends WorldItem {

   private int rows;
   private int columns;
   private int numOfFires;
   private int flammableItemCount;
   private int itemsOnFire;
   private Point[] fireLocations;
   private Point[][] roomPoints;

   // Math!
   private final static int SURROUNDING_POINTS_ON_GRID = 8;

   // Physics!
   private final static double XR_FOR_AIR_IN_ROOM = 0.15;
   private final static int QDOT_FOR_AIR_IN_ROOM = 100;
   private final static double HEAT_TRANSFER_COEFFICIENT = 0.01;
   // 1 kW raises temp by 100deg C assuming almost no air flow
   private final static int KW_DEGREE_INCREASE = 100;


   public Room(String roomName, int rows, int columns, int numOfFires) {
      super(roomName);
      this.rows = rows + 1;
      this.columns = columns + 1;
      this.numOfFires = numOfFires;
      this.flammableItemCount = 0;
      this.itemsOnFire = 0;
      fireLocations = new Point[numOfFires];
      roomPoints = new Point[this.rows][this.columns];
      fillRoomWithAir();
   }

   public Point getPointAtLocation(int row, int column) {
      if (row < this.rows && column <= this.columns) {
         return roomPoints[row][column];
      } else {
         return null;
      }
   }

   public WorldItem getItemAtLocation(int row, int column) {
      Point checkPoint = getPointAtLocation(row, column);
      return checkPoint.getContainedItem();
   }

   private void fillRoomWithAir() {
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Air emptySpace = new Air();
            roomPoints[i][j] = new Point(i, j, emptySpace);
         }
      }
   }

   public int getRows() {
      return rows;
   }

   public int getColumns() {
      return columns;
   }

   public int getNumOfFires() {
      return numOfFires;
   }

   public void setNumOfFires(int numOfFires) {
      this.numOfFires = numOfFires;
   }

   public int getFlammableItemCount() {
      return flammableItemCount;
   }

   public void setFlammableItemCount(int flammableItemCount) {
      this.flammableItemCount = flammableItemCount;
   }

   public int getItemsOnFire() {
      return itemsOnFire;
   }

   public void setItemsOnFire(int itemsOnFire) {
      this.itemsOnFire = itemsOnFire;
   }

   public void placeItemInRoomAtCoords(WorldItem item, int row, int column) {
      Point targetPoint = getPointAtLocation(row, column);
      targetPoint.setContainedItem(item);
      if (item instanceof FlammableItem) {
         flammableItemCount++;
      }
   }

   public void updateIgnition(Point point, FlammableItem flammable) {
      if (!flammable.isOnFire()) {
         if (point.getCurrentTemp() >= flammable.getCombustionThreshold()) {
            flammable.ignite();
            itemsOnFire++;
            fireLocations[getLastSpotInArray(fireLocations)] = point;
         }
      }
   }

   public boolean pointInFlammableLocations(Point point) {
      for (Point location : fireLocations) {
         if (location == point) {
            return true;
         }
      }
      return false;
   }

   public void updateAlarm(Point point, FireAlarm alarm) {
      if (!alarm.isAlerted()) {
         if (point.getCurrentTemp() >= alarm.getAlarmThreshold()) {
            alarm.triggerAlarm();
         }
      } else if (point.getCurrentTemp() < alarm.getAlarmThreshold()) {
         alarm.stopAlarm();
      }
   }

   public void update() {
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point point = getPointAtLocation(i, j);
            WorldItem item = getItemAtLocation(i, j);
            calculatePointTemp(point);
            point.update();
            if (item instanceof FlammableItem) {
               updateIgnition(point, (FlammableItem) item);
            } else if (item instanceof FireAlarm) {
               updateAlarm(point, (FireAlarm) item);
            }
         }
      }
   }

   public void calculatePointTemp(Point point) {
      WorldItem containedItem = point.getContainedItem();

      // Maintain temperature if the item here is flammable and on fire
      if (containedItem instanceof FlammableItem) {
         if (((FlammableItem) containedItem).isOnFire()) {
            return;
         }
      }

      // Temperature factors
      double radQDot = calcRadQDot(point);
      double convQDot = calcConvQDot(point);
      double totalQDot = radQDot + convQDot;

      point.setCurrentTemp(KW_DEGREE_INCREASE * totalQDot);
   }

   public double calcRadQDot(Point point) {
      int currentCol = point.getColumn();
      int currentRow = point.getRow();
      double minDist = 0;
      for (Point fire : fireLocations) {
         if (fire != null) {
            int fireCol = fire.getColumn();
            int fireRow = fire.getRow();
            double dist = distFromFire(currentCol, currentRow, fireCol, fireRow);
            if (dist < minDist) {
               minDist = dist;
            }
         }
      }

      // Xr * qDot for air in room
      double factor = (XR_FOR_AIR_IN_ROOM * QDOT_FOR_AIR_IN_ROOM);

      // Account for division by zero
      if (minDist > 0) {
         return (factor / (4 * Math.PI * Math.pow(minDist, 2)));
      } else {
         return 0;
      }

   }

   public double distFromFire(int x1, int y1, int x2, int y2) {
      return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
   }

   public double calcConvQDot(Point point) {
      Point[] oneAway = getSpacesOneAway(point);
      int localAverage = getAverageSurroundingTemp(oneAway);
      double currentTemp = point.getCurrentTemp();

      if (localAverage > currentTemp) {
         return (HEAT_TRANSFER_COEFFICIENT * (localAverage - currentTemp));
      } else {
         return (HEAT_TRANSFER_COEFFICIENT * (currentTemp - localAverage));
      }
   }

   public int getAverageSurroundingTemp(Point[] spacesOneAway) {
      int total = 0;
      for (Point point : spacesOneAway) {
         if (point != null) {
            total += point.getCurrentTemp();
         }
      }
      return total / spacesOneAway.length;
   }

   public Point[] getSpacesOneAway(Point point) {
      int col = point.getColumn();
      int row = point.getRow();

      Point[] oneAway = new Point[SURROUNDING_POINTS_ON_GRID];

      // Clockwise around point.
      // TODO: Constant above is fine. Fix magic numbers...
      int aboveRow = row - 1;
      int belowRow = row + 1;
      int leftCol = col + 1;
      int rightCol = col - 1;
      if (pointExists(aboveRow, leftCol)) {
         oneAway[0] = getPointAtLocation(aboveRow, leftCol);
         ;
      } else {
         oneAway[0] = null;
      }
      if (pointExists(aboveRow, col)) {
         oneAway[1] = getPointAtLocation(aboveRow, col);
      } else {
         oneAway[1] = null;
      }
      if (pointExists(aboveRow, rightCol)) {
         oneAway[2] = getPointAtLocation(aboveRow, rightCol);
      } else {
         oneAway[2] = null;
      }
      if (pointExists(row, leftCol)) {
         oneAway[3] = getPointAtLocation(row, leftCol);
      } else {
         oneAway[3] = null;
      }
      if (pointExists(row, rightCol)) {
         oneAway[4] = getPointAtLocation(row, rightCol);
      } else {
         oneAway[4] = null;
      }
      if (pointExists(belowRow, leftCol)) {
         oneAway[5] = getPointAtLocation(belowRow, leftCol);
      } else {
         oneAway[5] = null;
      }
      if (pointExists(belowRow, col)) {
         oneAway[6] = getPointAtLocation(belowRow, col);
      } else {
         oneAway[6] = null;
      }
      if (pointExists(belowRow, rightCol)) {
         oneAway[7] = getPointAtLocation(belowRow, rightCol);
      } else {
         oneAway[7] = null;
      }

      return oneAway;
   }

   public boolean pointExists(int row, int column) {
      return (row > 0 && row < rows && column > 0 && column < columns);
   }

   public boolean isAllBurntUp() {
      return (flammableItemCount == itemsOnFire);
   }

   public int getLastSpotInArray(Point[] array) {
      int spot = -1;
      for (int i = 0; i < array.length; i++) {
         if (array[i] == null) {
            spot = i;
            break;
         }
      }
      return spot;
   }

   // TODO: Clean this up
   public String toString() {
      String output = "";
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point point = getPointAtLocation(i, j);
            output += "|";
            if (point.getContainedItem() instanceof Air) {
//               output += " ";
               output += point.getCurrentTemp();
            } else if (point.getContainedItem() instanceof FireAlarm) {
               if (((FireAlarm) point.getContainedItem()).isAlerted()) {
                  output += "!";
               } else {
                  output += "s";
               }
            } else {
               if (point.getContainedItem() instanceof FlammableItem) {
                  if (((FlammableItem) point.getContainedItem()).isOnFire()) {
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

/** Represents the room as a whole for the simulation */
public class Room extends WorldItem {

   // Room dimensions
   private final int rows;
   private final int columns;

   // Tracking number of fires vs items vs how many of those items are on fire
   private int numOfFires;
   private int flammableItemCount;
   private int itemsOnFire;

   // Locations of items in the room
   private final Point[][] roomPoints;
   private Point[] fireLocations;
   private Point[] sensorLocations;

   // Reset string for colors!
   private final static String RESET = "\u001b[0m";

   // Math!
   private final static int SURROUNDING_POINTS_ON_GRID = 8;

   // Physics!
   private final static double XR_FOR_AIR_IN_ROOM = 0.15;
   private final static int QDOT_FOR_AIR_IN_ROOM = 100;
   private final static double HEAT_TRANSFER_COEFFICIENT = 0.01;
   // 1 kW raises temp by 100deg C assuming almost no air flow
   private final static int KW_DEGREE_INCREASE = 100;

   /** Constructor, establishes room information and the number of fires within */
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

   /** Return the number of flammable items in the room */
   public int getFlammableItemCount() {
      return flammableItemCount;
   }

   /** Set the number of flammable items in the room */
   public void setFlammableItemCount(int flammableItemCount) {
      this.flammableItemCount = flammableItemCount;
   }

   /** Return the number of flammable items in the room */
   public int getItemsOnFire() {
      return itemsOnFire;
   }

   /** Set the number of items currently on fire in the room. Increment/decrement is handled by
    * updateIgnition()  */
   public void setItemsOnFire(int itemsOnFire) {
      this.itemsOnFire = itemsOnFire;
   }

   /** Add an item to the room at given coordinates, and if it's flammable, increase the number
    * of flammable items in the room */
   public void placeItemInRoomAtCoords(WorldItem item, int row, int column) {
      Point targetPoint = getPointAtLocation(row, column);
      targetPoint.setContainedItem(item);
      if (item instanceof FlammableItem) {
         flammableItemCount++;
      }
   }

   /** Check whether an item at a given point should be on fire */
   public void updateIgnition(Point point) {
      WorldItem item = point.getContainedItem();
      if (item instanceof FlammableItem) {
         FlammableItem flammable = (FlammableItem) item;
         if (!flammable.isOnFire()) {
            if (point.getCurrentTemp() >= flammable.getCombustionThreshold()) {
               flammable.ignite();
               itemsOnFire++;
               fireLocations[getLastSpotInArray(fireLocations)] = point;
            }
         }
      }
   }

   /** Check whether a given point is within the currently known locations for fires */
   public boolean pointInFlammableLocations(Point point) {
      for (Point location : fireLocations) {
         if (location == point) {
            return true;
         }
      }
      return false;
   }

   /** Toggle the alarm as necessary */
   public void updateAlarm(Point point) {
      WorldItem item = point.getContainedItem();
      if (item instanceof Sensor) {
         Sensor alarm = (Sensor) item;
         if (!alarm.isAlarmed()) {
            if (point.getCurrentTemp() >= alarm.getAlarmThreshold()) {
               alarm.triggerAlarm();
            }
         } else if (point.getCurrentTemp() < alarm.getAlarmThreshold()) {
            alarm.stopAlarm();
         }
      }
   }

   /** Iterate through the points in the room and determine the temperature at each tick */
   public void update() {
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point point = getPointAtLocation(i, j);
            WorldItem item = point.getContainedItem();
            calculatePointTemp(point);
            point.update();
            // TODO: Presumes only one item per 'spot', should an alarm be in the same place as
            //  an item?
            if (item instanceof FlammableItem) {
               updateIgnition(point);
            } else if (item instanceof SimulatedSensor) {
               updateAlarm(point);
            }
         }
      }
   }

   public void calculatePointTemp(Point point) {
      // TODO: Handle calculations in another class, Room should manage room responsibilities
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

      point.setCurrentTemp(point.getCurrentTemp() + (KW_DEGREE_INCREASE * totalQDot));
   }

   /** Calculate the radiative qdot */
   public double calcRadQDot(Point point) {
      if(numOfFires == 0)
         return 0;
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

   /** Determine the distance of a given point from fire */
   public double distFromFire(int x1, int y1, int x2, int y2) {
      return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
   }

   /** Calculate the convective q dot */
   public double calcConvQDot(Point point) {
      Point[] oneAway = getSpacesOneAway(point);
      double localAverage = getAverageSurroundingTemp(oneAway);
      double currentTemp = point.getCurrentTemp();

      if (localAverage > currentTemp) {
         return (HEAT_TRANSFER_COEFFICIENT * (localAverage - currentTemp));
      } else {
         return (HEAT_TRANSFER_COEFFICIENT * (currentTemp - localAverage));
      }
   }

   public double getAverageSurroundingTemp(Point[] spacesOneAway) {
      double total = 0;
      int numPoints = 0;
      for (Point point : spacesOneAway) {
         if (point != null) {
            total += point.getCurrentTemp();
            numPoints++;
         }
      }
      return total / numPoints;
   }

   /** Get all points that are within one point away from the current point */
   public Point[] getSpacesOneAway(Point point) {
      int col = point.getColumn();
      int row = point.getRow();

      Point[] oneAway = new Point[SURROUNDING_POINTS_ON_GRID];

      int count = 0;

      // Get all points that aren't the other points.
      for(int i = row - 1; i <= row + 1; i++)
         for(int j = col - 1; j <= col + 1; j++) {
            if(i != row || j != col) {
               if(pointExists(i, j))
                  oneAway[count] = getPointAtLocation(i, j);
               else
                  oneAway[count] = null;
               count++;
            }
         } // end nested for

      return oneAway;
   }

   /** Check whether a given point exists at the given row and column */
   public boolean pointExists(int row, int column) {
      return (row > 0 && row < rows && column > 0 && column < columns);
   }

   /** Return whether there are any flammable items in the room that are not currently on fire */
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

   /** Generate string for room display */
   // TODO: Clean this up
   public String toString() {
      String output = "";

      // Cell-ify the room output
      String finishRow = "";
      for(int i = 0; i < columns; i++)
         finishRow += "======";
      finishRow += "=\n";

      output += finishRow;

      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point point = getPointAtLocation(i, j);
            String clr = getColorString(point);
            output += "|" + clr + " ";
            if (point.getContainedItem() instanceof Air) {
               String toAdd = "" + (int)point.getCurrentTemp();
               if(toAdd.length() < 3)
                  for(int loop = toAdd.length(); loop < 3; loop++)
                     toAdd = " " + toAdd;
               output += toAdd;
            } else if (point.getContainedItem() instanceof SimulatedSensor) {
               if (((SimulatedSensor) point.getContainedItem()).isAlarmed()) {
                  output += " ! ";
               } else {
                  output += " s ";
               }
            } else {
               if (point.getContainedItem() instanceof FlammableItem) {
                  if (((FlammableItem) point.getContainedItem()).isOnFire()) {
                     output += " ^ ";
                  } else {
                     output += " x ";
                  }
               } else {
                  output += " x ";
               }
            }
            output += " " + RESET;
         }
         output += "|\n" + finishRow;
      }
      return output;
   }

   /**
    * Creates a String which is used to display a command-line color (makes for better coloring).
    * @param p - the point being displayed
    * @return a String used for color formatting
    */
   private String getColorString(Point p) {
      double temp = p.getCurrentTemp();
      if(temp < 100)
         return "";
      if(temp < 200)
         return "\u001b[42;1m";
      if(temp < 300)
         return "\u001b[43;1m";
      return "\u001b[41;1m";
   } // end getColorString()

}

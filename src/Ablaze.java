import java.util.ArrayList;

/**
 * Main driver class. Creates a room and a test sim to see if things work correctly.
 */
public class Ablaze {

   // UI
   private static UI ui;

   // Room
   private static Room room;

   // Sensor location
   private static Sensor sensor;

   // List of flammable items
   private static final ArrayList<Point> flammablePoints = new ArrayList<>();

   /**
    * Method in charge of building a basic room with a fire and a sensor (with set locations).
    */
   public static void buildRoom(UI ui) {
      // Get room size
      int[] roomSize = ui.selectRoomSize();

      // Room locations count from 0
      String roomName = "basicroom";
      int rows = roomSize[0];
      int columns = roomSize[1];
      int numOfFires = 1;

      room = new Room(roomName, rows, columns, numOfFires);
   }

   /**
    * Request and place items in the current room
    */
   public static void fillRoomWithStuff() {

      // Place Sensor
      int[] sensorCoords = ui.selectPointForSensor();
      int sensorRow = sensorCoords[0];
      int sensorColumn = sensorCoords[1];

      sensor = new SimulatedSensor();
      room.placeItemInRoomAtCoords((WorldItem) sensor, sensorRow, sensorColumn);

      int itemCounter = 0;
      int numItemsToPlace = ui.selectNumItemsToPlace();

      while (itemCounter < numItemsToPlace) {

         int[] itemCoords = ui.selectPointForItem();
         int row = itemCoords[0];
         int column = itemCoords[1];

         if (ui.setItemFlammable()) {
            int turns = ui.selectNumOfTurnsBeforeIgnition();
            FlammableItem flammable = new FlammableItem("Chair", turns);
            room.placeItemInRoomAtCoords(flammable, row, column);
            flammablePoints.add(room.getPointAtLocation(row, column));
         } else {
            NonFlammableItem nonFlam = new NonFlammableItem("Chair");
            room.placeItemInRoomAtCoords(nonFlam, row, column);
         }

         // Put a chair in the room
         itemCounter++;
      }
   }

   /**
    * Runs the simulation for a supplied room until sensor is in alarm status
    */
   public static void runSimUntilAlarmed() {
      int turns = 0;

      while (!sensor.isAlarmed()) {
         turns++;

         for (Point point : flammablePoints) {
            FlammableItem item = (FlammableItem) point.getContainedItem();
            // Wait a few turns
            if (turns == item.getTurnsBeforeIgnition()) {
               // Set the point hot enough to light the item
               point.setCurrentTemp(item.getCombustionThreshold());
            } // end if
         }
         room.update();
         ui.drawRoom(room);

      } // end while
   } // end testSimWithChair()

   /**
    * Main entry method. Runs a basic simulation with set locations.
    *
    * @param args - any supplied arguments (unused)
    */
   public static void main(String[] args) {
      ui = new ConsoleUI();
      buildRoom(ui);
      fillRoomWithStuff();
      runSimUntilAlarmed();

   } // end main()

} // end Ablaze
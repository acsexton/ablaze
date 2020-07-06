import java.util.ArrayList;

/**
 * Main driver class. Creates a room and a test sim to see if things work correctly.
 */
public class Ablaze {

   /** Sensor for use in base simulation */
   private static SimulatedSensor sensor;

   /**
    * Method in charge of building a basic room with a fire and a sensor (with set locations).
    * @return a newly instantiated Room
    */
   public static Room buildRoomWithSensor(int[] roomSize, int[] sensorCoords) {
      // Room locations count from 0
      String roomName = "basicroom";
      int rows = roomSize[0];
      int columns = roomSize[1];
      int numOfFires = 1;

      // Sensor location (also from 0)
      int sensorRow = sensorCoords[0];
      int sensorColumn = sensorCoords[1];

      Room testRoom = new Room(roomName, rows, columns, numOfFires);
      sensor = new SimulatedSensor();
      testRoom.placeItemInRoomAtCoords(sensor, sensorRow, sensorColumn);
      return testRoom;
   } // end buildRoomWithSensor()

   /**
    * Runs the simulation for a supplied room.
    * @param testRoom - the Room on which to run the simulation
    */
   public static void testSimWithChair(Room testRoom, ArrayList<FlammableItem> flammableItems) {
      int turns = 0;

      while (!sensor.isAlarmed()) {
         turns++;

         for (FlammableItem item : flammableItems) {
            // Wait a few turns
            if (turns == item.getTurnsBeforeIgnition()) {
               // Light it
               item.ignite();
            } // end if
         }
         testRoom.update();
         System.out.println(testRoom);
      } // end while
   } // end testSimWithChair()

   /**
    * Main entry method. Runs a basic simulation with set locations.
    * @param args - any supplied arguments (unused)
    */
   public static void main(String[] args) {
      ConsoleUI ui = new ConsoleUI();

      // Get room size
      int[] roomSize = ui.selectRoomSize();

      // Get sensor location and build room
      int[] sensorCoords = ui.selectPointForSensor();
      Room testRoom = buildRoomWithSensor(roomSize, sensorCoords);

      // List of flammable items
      ArrayList<FlammableItem> flammableItems = new ArrayList<>();

      int itemCounter = 0;

      int numItemsToPlace = ui.selectNumItemsToPlace();
      while (itemCounter <= numItemsToPlace) {

         int[] itemCoords = ui.selectPointForItem();
         int row = itemCoords[0];
         int column = itemCoords[1];

         if (ui.setItemFlammable()){
            int turns = ui.selectNumOfTurnsBeforeIgnition();
            FlammableItem flammable = new FlammableItem("Chair", turns);
            flammableItems.add(flammable);
         } else {
            NonFlammableItem nonFlam = new NonFlammableItem("Chair");
            testRoom.placeItemInRoomAtCoords(nonFlam, row, column);
         }

         // Put a chair in the room
         itemCounter++;
      }

      testSimWithChair(testRoom, flammableItems);

   } // end main()

} // end Ablaze
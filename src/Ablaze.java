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
   public static Room buildRoomWithSensor() {
      // Room locations count from 0
      String roomName = "basicroom";
      int rows = 5;
      int columns = 7;
      int numOfFires = 1;

      // Sensor location (also from 0)
      int sensorRow = 2;
      int sensorColumn = 3;

      Room testRoom = new Room(roomName, rows, columns, numOfFires);
      sensor = new SimulatedSensor();
      testRoom.placeItemInRoomAtCoords(sensor, sensorRow, sensorColumn);
      return testRoom;
   } // end buildRoomWithSensor()

   /**
    * Runs the simulation for a supplied room.
    * @param testRoom - the Room on which to run the simulation
    */
   public static void testSimWithChair(Room testRoom) {
      int turns = 0;
      int turnsBeforeIgnitingChair = 4;

      // Put a chair in the room
      FlammableItem chair = new FlammableItem("Chair");
      testRoom.placeItemInRoomAtCoords(chair, 5, 7);

      while (!sensor.isAlarmed()) {
         turns++;
         // Wait a few turns
         if (turns == turnsBeforeIgnitingChair) {
            // Light the chair
            Point testPoint = testRoom.getPointAtLocation(5, 7);
            testPoint.setCurrentTemp(chair.getCombustionThreshold() + 50);
         } // end if
         testRoom.update();
         System.out.println(testRoom);
      } // end while
   } // end testSimWithChair()

   /**
    * Main entry method. Runs a basic simulation with set locations.
    * @param args - any supplied arguments (unused)
    */
   public static void main(String[] args) {
      Room testRoom = buildRoomWithSensor();
      testSimWithChair(testRoom);
   } // end main()

} // end Ablaze
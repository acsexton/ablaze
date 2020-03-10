public class Ablaze {

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
      FireAlarm sensor = new FireAlarm();
      testRoom.placeItemInRoomAtCoords(sensor, sensorRow, sensorColumn);
      return testRoom;
   }

   public static void testSimWithChair(Room testRoom) {
      int turns = 0;
      int turnsBeforeIgnitingChair = 4;

      // Put a chair in the room
      FlammableItem chair = new FlammableItem("Chair");
      testRoom.placeItemInRoomAtCoords(chair, 5, 7);

      while (!testRoom.isAllBurntUp()) {
         turns++;
         // Wait a few turns
         if (turns == turnsBeforeIgnitingChair) {
            // Light the chair
            Point testPoint = testRoom.getPointAtLocation(5, 7);
            testPoint.setCurrentTemp(chair.getCombustionThreshold() + 50);
         }
         testRoom.update();
         System.out.println(testRoom);
      }
   }

   public static void main(String[] args) {
      Room testRoom = buildRoomWithSensor();
      testSimWithChair(testRoom);
   }

}

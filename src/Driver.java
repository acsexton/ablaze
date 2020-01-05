public class Driver {

   private static Room testRoom;
   private static FireAlarm sensor;

   public static void buildRoomAndPlaceSensor(){
      // Room locations count from 0
      String roomName = "basicroom";
      int rows = 5;
      int columns = 7;

      // Sensor location (also from 0)
      int sensorRow = 2;
      int sensorColumn = 3;

      testRoom = new Room(roomName, rows, columns);
      sensor = new FireAlarm();
      testRoom.placeItemInRoomAtCoords(sensor, sensorRow, sensorColumn);
   }

   public static void placeExtraItems(){
      FlammableItem chair = new FlammableItem("Chair");
      testRoom.placeItemInRoomAtCoords(chair, 5, 7);
   }

   public static void runSim(){
      while (!sensor.isAlerted()){
         testRoom.updateStatus();
         System.out.println(testRoom);
      }
   }

   public static void main(String[] args) {
      buildRoomAndPlaceSensor();
      placeExtraItems();
      runSim();
   }

}

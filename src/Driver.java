public class Driver {

   private static Room testRoom;
   private static FireAlarm sensor;

   public static void buildRoomAndPlaceSensor(){
      // Room locations count from 0
      String roomName = "basicroom";
      int rows = 5;
      int columns = 7;
      int numOfFires = 1;

      // Sensor location (also from 0)
      int sensorRow = 2;
      int sensorColumn = 3;

      testRoom = new Room(roomName, rows, columns, numOfFires);
      sensor = new FireAlarm();
      testRoom.placeItemInRoomAtCoords(sensor, sensorRow, sensorColumn);
   }

   public static void runSim(){
      int turns = 0;
      int turnsBeforeIgnitingChair = 4;

      FlammableItem chair = new FlammableItem("Chair");
      testRoom.placeItemInRoomAtCoords(chair, 5, 7);

      while (!testRoom.isAllBurntUp()){
         turns++;
         if (turns == turnsBeforeIgnitingChair){
            chair.ignite();
         }
         testRoom.update();
         System.out.println(testRoom);
      }
   }

   public static void main(String[] args) {
      buildRoomAndPlaceSensor();
      runSim();
   }

}

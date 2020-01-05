public class Driver {

   public static void main(String[] args) {
      // Count from 0 for row/column
      Room testRoom = new Room("basicroom", 5, 7);
      System.out.println(testRoom);
      FlammableItem chair = new FlammableItem("Chair");
      testRoom.placeItemInRoomAtCoords(chair, 5, 7);

      System.out.println(testRoom);
      chair.setOnFire();
      System.out.println(testRoom);
   }

}

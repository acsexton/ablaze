public class Driver {

   public static void main(String[] args) {
      Room testRoom = new Room("basicroom", 5, 7);
      // TODO: Prints room 5 tall, 7 wide...
      System.out.println(testRoom);
      FlammableItem chair = new FlammableItem("Chair");
      testRoom.placeItemInRoomAtCoords(chair, 3, 4);
      // TODO: Clean up coordinate placement, places 3 down, 4 over
      System.out.println(testRoom);
      chair.setOnFire();
      System.out.println(testRoom);
   }

}

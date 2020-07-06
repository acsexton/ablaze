import java.util.Scanner;

public class ConsoleUI implements UI {

   private final Scanner input;

   public ConsoleUI() {
      input = new Scanner(System.in);
   }

   @Override
   public int selectNumItemsToPlace() {
      System.out.println("How many items would you like to place?");
      return input.nextInt();
   }

   @Override
   public boolean setItemFlammable() {
      String flammable = "";
      while (!flammable.equals("y") && !flammable.equals("n")) {
         System.out.println("Would you like this item to be flammable? y/n");
         flammable = input.nextLine().toLowerCase();
      }
      return flammable.equals("y");
   }

   @Override
   public int[] selectPointForItem() {
      int[] coords = new int[2];
      System.out.println("Please enter row for the placement of the item.");
      coords[0] = input.nextInt();
      System.out.println("Please enter column for the placement of the item.");
      coords[1] = input.nextInt();
      return coords;
   }

   @Override
   public int[] selectPointForSensor() {
      int[] coords = new int[2];
      System.out.println("Please enter row for the placement of the sensor.");
      coords[0] = input.nextInt();
      System.out.println("Please enter column for the placement of the sensor.");
      coords[1] = input.nextInt();
      return coords;
   }

   @Override
   public int selectNumOfTurnsBeforeIgnition() {
      System.out.println("How many turns before igniting your item?");
      return input.nextInt();
   }

   @Override
   public int[] selectRoomSize() {
      int[] coords = new int[2];
      System.out.println("Please enter how many rows to place in room.");
      coords[0] = input.nextInt();
      System.out.println("Please enter how many columns to place in room.");
      coords[1] = input.nextInt();
      return coords;
   }

}

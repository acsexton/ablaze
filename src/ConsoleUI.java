import java.util.Scanner;

public class ConsoleUI implements UI {

   private final Scanner input;

   // Reset string for colors!
   private final static String RESET = "\u001b[0m";

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
      input.nextLine(); // flush from previous nextInt
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
      System.out.println("On which turn should this item ignite?");
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

   /** Generate string for room display */
   @Override
   public void drawRoom(Room room) {
      String output = "";

      int columns = room.getColumns();
      int rows = room.getRows();

      // Cell-ify the room output
      String finishRow = "";
      for(int i = 0; i < columns; i++)
         finishRow += "======";
      finishRow += "=\n";

      output += finishRow;

      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            Point point = room.getPointAtLocation(i, j);
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

      System.out.println(output);
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

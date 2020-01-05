public class Point {

   private static final int DEFAULT_TEMP = 72;

   // Location and item data
   private final int row;
   private final int column;
   private int currentTemp;
   private WorldItem containedItem;

   // Temperature factors
   private int radQDot;
   private int convQDot;
   private int totalQDot;

   public Point(int row, int column, WorldItem containedItem){
      this.row = row;
      this.column = column;
      this.containedItem = containedItem;
   }

   public int getRow() {
      return row;
   }

   public int getColumn() {
      return column;
   }

   public int getCurrentTemperature() {
      return currentTemp;
   }

   public void resetTemp() {
      currentTemp = DEFAULT_TEMP;
   }

   public void setCurrentTemp(int newTemp){
      currentTemp = newTemp;
   }

   public void placeItem(WorldItem containedItem){
      this.containedItem = containedItem;
   }

   public WorldItem getContainedItem(){
      return containedItem;
   }

   public void update(){
      // 1 kW raises temp by 100deg C assuming almost no air flow
      int kWDegreeIncrease = 100;

      totalQDot = radQDot + convQDot;
      int currentTemp = kWDegreeIncrease*totalQDot;

      // Reset to 0 for further recalculations
      totalQDot = 0;
   }

}

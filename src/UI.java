public interface UI {

   /** Give the size of the room */
   int[] selectRoomSize();

   /** Give the coordinates for the location of the sensor */
   int[] selectPointForSensor();

   /** Give how many items would like to be placed */
   int selectNumItemsToPlace();

   /** Set whether or not a created item should be flammable */
   boolean setItemFlammable();

   /** Select the coordinates for the locatino of an item */
   int[] selectPointForItem();

   /** Give the number of turns before the location on which the item is placed will exceed its
    * combustion threshold */
   int selectNumOfTurnsBeforeIgnition();

   /** Display the room in its current state */
   void drawRoom(Room room);

}

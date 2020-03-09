# Work In Progress: Fire Alarm System #
This will be a physics-supported system for simulating heat distribution 
within a room with flammable objects. When ignited, the heat will eventually 
set off a sensor to alert registered users. 

The simulation is based on average temperatures in a local area as heat moves
through air in a still room.

## More about
Though some sketches of this project began a year or so ago, I built this 
current iteration over about a two week period during winter break 2019. Though 
I intend to continue working on it, it is very much a work in progress.

It currently uses as basic data structures as I can get away with (e.g. arrays 
instead of ArrayLists) just to keep practice up.

### Todo
* Polishing temperature calculation
    * Things ignite when they should temperature-wise and the program is 
    aware of ending once everything's on fire
    * Still getting a handle on the specifics of the math, though.
    * Cleaning up Room class logic -- a lot is handled there and not in the 
    most readable organization approach
* Sensor alert handling
* Framework for real world sensor responses and implementation
* Graphic display of room temperatures with console colors
* User interface for placing and igniting items at will

### References
* [Fire Dynamics](https://www.nist.gov/el/fire-research-division-73300/firegov-fire-service/fire-dynamics)
* [Burning Wastepaper Fire](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.132.3106&rep=rep1&type=pdf)
* [Radiant Heat Transfer](https://pdhonline.com/courses/m312/Radiant%20Flux.pdf) 
* [Convective Heat Transfer Coefficients](https://www.engineersedge.com/heat_transfer/convective_heat_transfer_coefficients__13378.htm)
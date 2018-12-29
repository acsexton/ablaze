# Fire Alarm System #

This is a simulation of a fire alarm system. It monitors data from temperature sensors in all connected rooms to determine whether a fire is occurring therein. If a temperature in a tracked room is high enough to indicate a fire is present, it will send a message to a designated recipient.

## Current Capabilities ##

- None

## Planned Capabilities ##

- Single room setup
- Temperature monitoring
- In-app alert system 
- Multiple room / grid setup
- Respond to requests for temperature in any room at request

## Long-term goals ##

- External alert system
    - Email
    - Text messaging
- Tracking temperature in each room over time, building a 'heat map'
- As proof of concept, include simulated fully-flammable environment (see below)


# World Ablaze #

As proof of concept, a small, simulated world in which any given object may 
spontaneously erupt in flames.
    
<!-- TODO -->
Conduction/convection heat distribution to sensor pick-up from python project
    * Time / turns, item.updateStatus() in room spots
Observer Pattern for sensors

## Planned Capabilities ##

- Hand-built 'map' with a grid of buildings and assorted objects
    - Stationary objects which all have a chance to spontaneously combust, 
    igniting a fire to test the alert system
    - Mobile 'NPCs' which also have such a chance to combust
    - Use of local alert just as it would function in a real-world system
    - A local, simulated authority who may respond to and put out these fires
- Interface
    - Counter: "It has been [x] minutes since the last fire"
    - Cursor-look tool to view object/NPC descriptions 
    - Object descriptions and NPC names pulled randomly from a local file

## Long-term goals ##

- Procedurally generate the buildings within the environment
- Procedurally generate the objects and NPCs within the environment
- Rebuilding/replacing of the buildings and objects within the environment

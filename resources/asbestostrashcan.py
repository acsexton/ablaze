#####
# Project 4: Functional Programming with Asbestos Trashcan Fires
# 
# Authors: Justin Goodberry and Andrew Sexton
# Version: 05-03-2019
#####

# Imports 
import math
import random
from functools import reduce

# Constants 
# These may be implemented in a future version to allow for
#   changing the type of fire considered
## Energy Output (Measured in kW)
# CIGARETTE_ENERGY_OUTPUT = 0.005
# CANDLE_ENERGY_OUTPUT = 0.08
# PERSON_ENERGY_OUTPUT = 0.1
WASTEPAPER_ENERGY_OUTPUT = 100 
# WOOD_PALLET_ENERGY_OUTPUT = 7000 # 7 MW (stacked to a height of 3m)

## Temperature (Measured in C)
WASTEPAPER_BURN_TEMP = 593
# GASOLINE_BURN_TEMP = 1026
# WOOD_BURN_TEMP = 1027

# Nonconstant
room = []


#####
# Builds a room 
# 
# Param RoomWidth: Width of the room
# Param roomLength: Length of the room
# Param baseTemp: Base temperature of the room
#####
def buildRoom(roomWidth, roomLength, baseTemp):
    count = 0
    # Add a blank dictionary object for each index of the room.
    while count < (roomWidth * roomLength):
        room.append({'currentTemp': baseTemp, 'onFire': False, \
            'isSensor': False, 'qDot': 0})
        count += 1

    # Get the locations for the sensor and the fire and place them in room.
    global sensorIndex
    sensorIndex = placeSensor()

    global fireIndex
    fireIndex = placeFire()

    return 


######
# Places a sensor based on user input or randomization
# 
# Return Location of sensor
#####
def placeSensor():
    # Should the sensor be randomly placed or chosen?
    pickOrRandom = -1
    # Choose whether to choose or to randomize
    print("Would you like to choose the sensor location or have it placed " +
        "randomly?")
    while pickOrRandom != "1" and pickOrRandom != "2":
        pickOrRandom = input("1 for pick, 2 for random: ")
    # If picking a number
    if pickOrRandom == "1":
        sensorLocation = -1
        while (not sensorLocation > 0) or (not sensorLocation <= len(room)):
            print("Choose a number between 1 and %d for the sensor location." \
                %(len(room)))
            sensorLocation = input("Pick a number: ")
            inputIsNumber = False
            try:
                sensorLocation = int(sensorLocation)
                inputIsNumber = True
            except:
                sensorLocation = -1
                print("Non-number passed.")
            if inputIsNumber:
                if (sensorLocation < 0) or (sensorLocation >= len(room)):
                    print("Number out of range.")
    # If randomizing through recursion
    else: 
        sensorLocation = recurseRand(1, len(room))

    # Mark the space that has the sensor
    room[sensorLocation-1]['isSensor'] = True

    # Tell the user where the sensor is
    print("The location %d was chosen for the sensor." %(sensorLocation))

    # Return the sensor location to be made into a global (sensorIndex)
    return sensorLocation-1 


######
# Recurses through from 1 to max (inclusive) 
# 
# Param Current: Current space in the room
# Param Max: Maximum possible space 
# Return: Random space for use by another function
#####
def recurseRand(current, max):
    # Randomly chooses a space between 1 and max inclusive
    if(current < 1):
        current = 1
    if(current > max):
        current = 1
    if(random.randint(1, len(room)) == 1):
        return current
    else:
        current += 1
        return recurseRand(current, max)


######
# Places a firey trash can based on user input or randomization
# Requirement Met: Recursion
# 
# Return: Location of fire
#####
def placeFire(): 
    # Should the sensor be randomly placed or chosen?
    pickOrRandom = -1
    # Choose whether to choose or to randomize
    print("Would you like to choose the fire location or have it " + 
        "placed randomly?")
    while pickOrRandom != "1" and pickOrRandom != "2":
        pickOrRandom = input("1 for pick, 2 for random: ")
    # If picking a number
    if pickOrRandom == "1":
        fireLocation = -1
        while (not fireLocation > 0) or (not fireLocation <= len(room)):
            print("Choose a number between 1 and %d for the fire location." \
                %(len(room)))
            fireLocation = input("Pick a number: ")
            inputIsNumber = False
            try:
                fireLocation = int(fireLocation )
                inputIsNumber = True
            except:
                fireLocation = -1
                print("Non-number passed.")
            if inputIsNumber:
                if (fireLocation < 0) or (fireLocation >= len(room)):
                    print("Number out of range.")
    # If randomizing through recursion
    else: 
        fireLocation = recurseRand(1, len(room))

    # Mark the space which is on fire
    room[fireLocation-1]['onFire'] = True

    # Tell the user where it is
    print("The location %d was chosen for the fire." %(fireLocation))
    
    # Return the value so that it can be placed in a global
    return fireLocation-1


#####
# Calculates and applies qdot values to a given location in room.
#
# Param location: Location to which qdot values will be applied
# Return: location - the dictionary object being modified
#####
def addQDots(location, rad, conv):
    
    # Add the qDot values together (in kW)
    location['qDot'] =  rad + conv
    
    # Return the location with the new qDot value
    return location


######
# Calculates the qdot value of the given location based on convection
# 
# Param Location: For which to calculate convection qdot
# Return: Convection qdot at that location
#####
def calcConvQDot(location):

    # Find all the spaces dist < 2 away
    oneAway = getSpacesOneAway(location)

    # Take the average temperature of those locations
    avg = getAvgTemp(oneAway)
    
    # Get the temperature of the given location
    locTemp = room[location]['currentTemp']

    # Convection heat transfer coefficient = 10W -> KW
    convCoeff = 0.01

    # Calculate depending on which is the larger of the two values
    if avg > locTemp:
        return (float(convCoeff) * (avg - locTemp))
    else:
        return (float(convCoeff) * (locTemp - avg))


######
# Calculates the qdot value of the given location based on radiation
# 
# Param Location: For which to calculate radiation qdot
# Return: Radiation qdot at that location
#####
def calcRadQDot(location):
    
    global roomWidth
    global roomLength

    # Get the function for calculating distance
    dist = calcDistance()
    
    # Values for use by the dist function
    row1 = math.floor(location / roomLength)
    col1 = location % roomWidth
    row2 = math.floor(fireIndex / roomLength)
    col2 = fireIndex % roomWidth

    # Find the distance away
    distFromFire = dist(row1, col1, row2, col2)

    # Xr * Qdot for air in room
    xr = 0.15
    qdot = 100

    # To account for division by zero
    if distFromFire > 0:
        return (float(xr * qdot) / (4 * math.pi * math.pow(distFromFire, 2)))
    else:
        return 0


######
# Calculates distance between two less consistent spaces
# Requirement Met: Pure function, return value is a function, anonymous 
#   function
# 
# Return: Distance between two given points
#####
def calcDistance():

    # Returns a distance function between two arbitary points
    return lambda x1, y1, x2, y2: math.sqrt(math.pow((x2-x1), 2) + \
        math.pow((y2-y1), 2))


######
# Calculates distance between a named (fire or sensor) space and a given 
#   location
# Requirement Met: Pure function, return value is a function, anonymous 
#   function
# 
# Param Location: Space to calculate distance from
# Return: Distance between named and an arbitrary space
#####
def calcDistanceFromNamed(location):
    
    global roomWidth
    global roomLength

    # Given location that this function will be used for (i.e. sensor)
    x2 = location // roomWidth
    y2 = location % roomLength

    # Return a distance function from that explicit point
    return lambda x1, y1:  math.sqrt(math.pow((x2-x1), 2) + \
        math.pow((y2-y1), 2))


######
# Returns a function which will alter the temperature based on space type
# Requirement Met:  Closure
# 
# Param Location: space in room
# Return Function for either maintaining temp or calculating temperature change
#####
def findChangeType(location):

    # If you are looking at the space that's on fire, maintain the temp
    if location == fireIndex:
        fireTypeTemp = WASTEPAPER_BURN_TEMP
        # State is maintained through closure in the fire 
        # temperature maintaining function
        def maintainTemp(location):
            room[location]['convQDot'] = 0
            room[location]['radQDot'] = 0
            room[location]['currentTemp'] = fireTypeTemp
        return maintainTemp
    
    # Otherwise, figure out how much you're changing the temp by
    else:
        return calcTempChange


######
# Calculates the change in temperature in a non-fiery location based on qdot 
#   values
# Requirement Met: List Comprehension
#
# Param Location: Space in room
#####
def calcTempChange(location):
    # apply list comprehension to calculate change in temp based 
    # on calculated qdot
    global room
    newRoom = [changeTemp(a) for a in range(len(room))]
    room = newRoom
    
    return
    

######
# Changes the temperature of a given location in a room based on its qdot 
#   values.
# 
# Param Location: Space in room
# Return: Location in room with altered temperature
#####
def changeTemp(location):

    # 1 kW raises temperature by 100deg C assuming almost no air flow
    kwTempInc = 100

    # Change the temp based on the qDot, then set it to 0 to avoid repeats
    tempSpace = room[location]
    tempQdot = tempSpace['qDot']
    tempSpace['currentTemp'] += float(kwTempInc)*tempQdot
    room[location]['qDot'] = 0
    return tempSpace


######
# Finds the spaces distance < 2 from a given location
# Requirement Met: Filter
# 
# Param loc: Location from which to calculate distance
# Return: List of spaces with distance < 2 from the location
#####
def getSpacesOneAway(loc):
    global room
    global roomWidth
    global roomLength
    spaceDist = calcDistanceFromNamed(loc)
    spaceDistList = []
    for i in range(len(room)):
        row = i // roomLength
        col = i % roomWidth
        spaceDistList.append({'loc': -1, 'dist': -1})
        spaceDistList[i]['loc'] = i
        spaceDistList[i]['dist'] = spaceDist(row, col)
    spacesNearLoc = list(filter(lambda x : x['dist'] < 2, spaceDistList))
    for i in range(len(spacesNearLoc)):
        spacesNearLoc[i] = spacesNearLoc[i]['loc']
    return spacesNearLoc


######
# Finds the average temperature value of the spaces near a location
# Requirement Met: Reduce
# 
# Param Locations: Locations near the location which need to be averaged
# Return: Average area temperature
#####
def getAvgTemp(locations):
    temps = []
    global room
    for i in range(len(locations)):
        temps.append(room[locations[i]]['currentTemp'])

    total = reduce((lambda x1, x2: x1 + x2), temps)
    return (total / len(locations))


######
# Calls necessary function paramter for a given location
# Requirement Met: Function Received as Argument
# 
# Param Location: Location for which to determine function
#####
def updateTemp(location):
    room[location]['changeType'](location)
    return 


######
# Displays room on each tick
# 
# Param Threshold: Threshold temperature to measure from
#####
def displayRoom(threshold):

    displayString = ""
    global room
    global roomWidth
    counter = 0
  
    # Color values to be put into different heat thresholds
    reset = '\u001b[0m '
  
    whiteText = '\u001b[37m '
    yellowText = '\u001b[33m '
    cyanText = '\u001b[36m '
  
    yellowBg = '\u001b[43m '
    blackBg = '\u001b[40m '
    redBg = '\u001b[41m '
    orangeBg = '\u001b[48;5;214m '

    onFire = yellowText + redBg
    highHeat = whiteText + redBg
    medHeat = whiteText + orangeBg
    lowHeat = whiteText + yellowBg
    noHeat = cyanText + blackBg

    for space in room:
        
        # Take the heat
        heat = space['currentTemp']
        
        # Add to the display based on relation to threshold
        if(space['onFire']):
            displayString += onFire
        elif(heat >= threshold):
            displayString += highHeat
        elif(heat >= (.5 * threshold)):
            displayString += medHeat
        elif(heat > (.35 * threshold)):
            displayString += lowHeat
        else:
            displayString += noHeat

        # add the text value of the given space
        if (space['isSensor']):
            displayString += ("\u001b[32m$")
        elif (space['onFire']):
            displayString += ("@")
        else:
            displayString += ("-")
        if (counter % roomWidth == (roomWidth - 1)): # at edge?
            displayString += reset + "\n"
        counter += 1

    # Print the created string
    print(displayString)


######
# Main entry and driver function
# Requirement Met: Map()
#####
def main():

    # Base size of the room
    global roomWidth
    roomWidth = 20      
    global roomLength
    roomLength = 20

    # Base temperature of the room
    baseTemp = float(20) # 20C = 68F

    # Base temperature of the fire
    fireTypeTemp = WASTEPAPER_BURN_TEMP

    # Temperature at which the sensor will go off
    threshold = float(80)

    # Set up the room and mark time 0
    global room
    fireSensed = False
    buildRoom(roomWidth, roomLength, baseTemp)
    time = 0

    # Until the alarm goes off (the sensor reaches its threshold)
    while not fireSensed:
        
        # Find the qDot values for each space
        radQDotList = []
        convQDotList = []
        for i in range(len(room)):
            radQDotList.append(calcRadQDot(i))
            convQDotList.append(calcConvQDot(i))

        # Put all of the new temps based on the qdots into the list
        room = list(map(addQDots, room, radQDotList, convQDotList))

        # Display the room before any temp changes happen
        displayRoom(threshold)

        # Change / maintain the temp of each space
        for i in range(len(room)):
            neededFunction = findChangeType(i)
            room[i]['changeType'] = neededFunction
        
        # Update the temp of each space
        for i in range(len(room)):
            updateTemp(i)

        # Compute the average value around the sensor
        sensorSpaces = getSpacesOneAway(sensorIndex) 
        sensorTemp = getAvgTemp(sensorSpaces)

        # If the fie should be sensed:
        if sensorTemp > threshold:
            fireSensed = True

        # Increment the number of turns taken
        time += 1

    # Output final results
    displayRoom(threshold)
    print("It took %s seconds for the sensor to go off." %(time))


if __name__ == "__main__":
    main()

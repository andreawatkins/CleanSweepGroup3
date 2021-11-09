import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Thread.sleep;

public class CleanSweep {
    public double battery;
    double currCapacity;
    double totalCapacity = 50.0;
    public FloorCell currentLocation;
    public FloorCell previousLocation;
    public SensorSimulator sensors;
    public State currentState;
    public FloorNode currentNode;
    public Logger logger = new Logger("bob.dobalina@gmail.com_");
    //Maybe this should be an additional argument at run time

    private Stack<FloorNode> traverseStack = new Stack<>();
    private ArrayList<FloorCell> visitedCells = new ArrayList<>();
    private List<Location> chargingStations = new ArrayList<>();


    public CleanSweep(Double battery, double currCapacity, SensorSimulator sensors, FloorCell currentLocation, FloorCell previousLocation) {
        this.sensors = sensors;
        this.currentLocation = currentLocation;
        this.previousLocation = previousLocation;
        this.battery = battery;
        this.currCapacity = currCapacity;
    }


    public void turnOff() {
        currentState = State.OFF;
        System.out.println("Clean Sweep is shutting down...");
        logger.logEndTime();
        System.exit(0);
    }


    public boolean isOn() {
        return (currentState.equals(State.ON));
    }


    public double useBattery() throws LowBatteryException {

        double batteryDec = 0;

        if (currentLocation.surfaceType == SurfaceType.BARE_FLOOR)
            batteryDec = 1;
        if (currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET)
            batteryDec = 2;
        else if (currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET)
            batteryDec = 3;

        if (previousLocation.surfaceType == currentLocation.surfaceType || previousLocation.surfaceType== SurfaceType.CHARGING_STATION) { // previous location was charging station
            battery = battery - batteryDec;
        } else {
            if ((currentLocation.surfaceType == SurfaceType.BARE_FLOOR) && (previousLocation.surfaceType == SurfaceType.LOW_PILE_CARPET))
                battery = battery - ((batteryDec + 2) / 2);
            if ((currentLocation.surfaceType == SurfaceType.BARE_FLOOR) && (previousLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET))
                battery = battery - ((batteryDec + 3) / 2);
            if ((currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.BARE_FLOOR))
                battery = battery - ((batteryDec + 1) / 2);
            if ((currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET))
                battery = battery - ((batteryDec + 3) / 2);
            if ((currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.BARE_FLOOR))
                battery = battery - ((batteryDec + 1) / 2);
            if ((currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.LOW_PILE_CARPET))
                battery = battery - ((batteryDec + 2) / 2);
        }
        if (needToCharge()) {
            //System.out.println(this.currentState);
            throw new LowBatteryException();
        } else System.out.println("Battery: " + String.format("%.1f", (battery / 250) * 100) + "% left");
        logger.logBatteryLevel((battery/250)*100);

        return battery;
    }


    public boolean needToCharge() throws LowBatteryException {
        if (battery < .20 * 250) {
            //currentState = State.LOW_BATTERY;
            System.out.println("Need to charge! Low Battery!");
            logger.logLowBattery();
            return true;
        }
        return false;
    }


    public void suckUpDirt() throws FullCapacityException, InterruptedException, LowBatteryException {
        if (isOn()) {
            logger.logDirtLevel(currentLocation.location, currentLocation);
            while (currentLocation.dirtAmount > 0 && (((currCapacity / totalCapacity) * 100)<100)) {
                try {
                    System.out.println("Cleaning... " + currentLocation.dirtAmount + " unit" + (currentLocation.dirtAmount == 1 ? "" : "s") + " of dirt left");
                    currentLocation.dirtAmount--;
                    useBattery();
                    currCapacity++;
                    sleep(1000);
                } catch (LowBatteryException e){
                    throw new LowBatteryException();
                }
            }

            if (((currCapacity / totalCapacity) * 100) >= 100) {
                //currentState= State.AT_CAPACITY;
                System.out.println("\nCapacity is full!");
                logger.logAtCapacity();
                throw new FullCapacityException();
            }
            else {
                System.out.println("Clean!\n");
                logger.logCellHasBeenCleaned(currentLocation.location);
                logger.logCurrentCapacity((currCapacity/totalCapacity)*100);
                System.out.println("Capacity: " + String.format("%.1f", (currCapacity / totalCapacity) * 100) + "% full");
            }
        }
    }


    public void charge() throws InterruptedException {
        while (battery <= 250.0) {
            System.out.println("Charging... battery at " + String.format("%.1f%%", (battery / 250.0) * 100.0));
            battery += 10.0;
            sleep(1000);
        }

        battery = 250.0;
        System.out.println("Battery at " + String.format("%.1f%%", (battery / 250.0) * 100.0));
    }


    public void moveToAdjacentNode(FloorNode destination) throws InterruptedException {
        if (isOn()) {
            previousLocation = currentLocation;

            if (destination.onGrid.x == currentLocation.rowIndex + 1)
                moveSouth();
            else if (destination.onGrid.x == currentLocation.rowIndex - 1)
                moveNorth();
            else if (destination.onGrid.y == currentLocation.colIndex + 1)
                moveEast();
            else if (destination.onGrid.y == currentLocation.colIndex - 1)
                moveWest();
            else {
                System.out.format("Current Location: (%d, %d)\n", sensors.currentLocation.x, sensors.currentLocation.y);
                System.out.format("Destination Location: (%d, %d)\n", destination.onGrid.x, destination.onGrid.y);
                throw new RuntimeException("Very bad, should never happen!");
            }

            currentNode = destination;
            sleep(1000);
        }
    }


    public Direction moveDirection(Direction direction) {
        Direction moveDirection = null;

        if (isOn()) {
            if (direction == Direction.SOUTH) {
                moveSouth();
                moveDirection = Direction.SOUTH;
            }

            if (direction == Direction.EAST) {
                moveEast();
                moveDirection = Direction.EAST;
            }

            if (direction == Direction.NORTH) {
                moveNorth();
                moveDirection = Direction.NORTH;
            }

            if (direction == Direction.WEST) {
                moveWest();
                moveDirection = Direction.WEST;
            }
        }

        return moveDirection;
    }


    public void moveNorth() {
        if (isOn()) {
            int x = sensors.currentLocation.x - 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            logger.logMoveNorth();
            System.out.format("%-15s%10s\n", "Moved north.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void moveSouth() {
        if (isOn()) {
            int x = sensors.currentLocation.x + 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            logger.logMoveSouth();
            System.out.format("%-15s%10s\n", "Moved south.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void moveEast() {
        if (isOn()) {
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y + 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            logger.logMoveEast();
            System.out.format("%-15s%10s\n", "Moved east.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void moveWest() {
        if (isOn()) {
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y - 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            logger.logMoveWest();
            System.out.printf("%-15s%10s\n", "Moved west.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void updateCurrentCell() {
        int x = sensors.currentLocation.x;
        int y = sensors.currentLocation.y;

        currentLocation = sensors.floorPlan.floorLayout.get(x).get(y);
    }


    public void traverseFloor() throws InterruptedException, FullCapacityException, LowBatteryException {
        if (isOn()) {
            FloorNode startNode = new FloorNode(null, new Location(0, 0), null);
            FloorNode previousNode = startNode;

            traverseStack.push(startNode);

            while (!traverseStack.isEmpty()) {
                FloorNode nodeBacktrackingTo = traverseStack.pop();

                // MOVE FROM THE PREVIOUS NODE TO THE CURRENT NODE
                // PREVIOUS NODE - last node processed
                // CURRENT NODE  - node we are backtracking to
                if (nodeBacktrackingTo != startNode) {
                    if (nodeBacktrackingTo.parent != previousNode) {
                        System.out.println("\nBacktracking...");
                        FloorNode intermediaryNode = previousNode.parent;
                        moveToAdjacentNode(intermediaryNode);

                        while (intermediaryNode != nodeBacktrackingTo.parent) {
                            intermediaryNode = intermediaryNode.parent;
                            moveToAdjacentNode(intermediaryNode);
                        }
                    }

                    moveToAdjacentNode(nodeBacktrackingTo);
                }
                else {
                    currentNode = nodeBacktrackingTo;
                }

                // WORK DONE ON CURRENT NODE HERE:
                FloorCell currentCell = sensors.floorPlan.floorLayout.get(currentNode.onGrid.x).get(currentNode.onGrid.y);
                if (currentCell.surfaceType == SurfaceType.CHARGING_STATION)
                    chargingStations.add(new Location(currentNode.onGrid.x, currentNode.onGrid.y));

                System.out.println();
                sensors.floorPlan.print((FloorCell cell) -> {
                    if (cell.colIndex == currentLocation.colIndex && cell.rowIndex == currentLocation.rowIndex) {
                        return "*";
                    }
                    else
                        return FloorPlan.printDirtAmount(cell);
                });
                System.out.println(String.format("Current Location: (%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
                logger.logCurrentLocation(sensors.currentLocation);
                sleep(1000);
                visitedCells.add(currentLocation);

                try {
                    suckUpDirt();
                    useBattery();
                }
                catch (LowBatteryException | FullCapacityException e) {
                    System.out.printf("Returning to charging station from: (%d, %d)\n", currentNode.onGrid.x, currentNode.onGrid.y);
                    returnToCharger();
                    System.out.printf("\nResuming cleaning at: (%d, %d)\n", currentNode.onGrid.x, currentNode.onGrid.y);

                    // finish up the job
                    suckUpDirt();
                    useBattery();
                }


                sleep(1000);
                // WORK FINISHED


                for (Direction movingDirection : sensors.getTraversableDirections()) {
                    FloorCell cellOption = nextCell(currentNode, movingDirection);
                    FloorNode nodeOption = new FloorNode(currentNode, cellOption.location, movingDirection);

                    if (!visitedCells.contains(cellOption) && !traverseStackContains(nodeOption))
                        traverseStack.push(nodeOption);
                }

                previousNode = currentNode;
            }

            System.out.println();
            sensors.floorPlan.print(FloorPlan::printDirtAmount);
            System.out.println("DONE!");

            System.out.println("\nCharging stations found:");
            System.out.println(chargingStations);
        }
    }


    public void returnToCharger() throws InterruptedException {
        Stack<FloorNode> toChargingStation = new Stack<>();
        System.out.println("\nReturning to charging station.");
        logger.logReturnToCharger();
        FloorNode tempNode = currentNode;

        while (tempNode.parent != null) { // if the parent is null, we are at the root of the tree, aka the charging station
            toChargingStation.push(tempNode); // keep track of every step of the way so we can go back eventually
            tempNode = tempNode.parent;
            moveToAdjacentNode(tempNode);
        }

        currentState = State.CHARGING;
        charge();
        emptyBag();
        currentState = State.ON;

        while (!toChargingStation.isEmpty())
            moveToAdjacentNode(toChargingStation.pop());
    }


    public void emptyBag() throws InterruptedException {
        System.out.println("\nEmptying bag...");
        sleep(1000);
        System.out.println("Bag emptied!\n");
        logger.logBinHasBeenEmptied();
        currCapacity = 0;
    }


    public FloorCell nextCell(FloorNode current, Direction travelingDirection) {
        int x;
        int y;

        if (travelingDirection == Direction.NORTH) {
            x = current.onGrid.x - 1;
            y = current.onGrid.y;
        }
        else if (travelingDirection == Direction.EAST) {
            x = current.onGrid.x;
            y = current.onGrid.y + 1;
        }
        else if (travelingDirection == Direction.SOUTH) {
            x = current.onGrid.x + 1;
            y = current.onGrid.y;
        }
        else { // WEST
            x = current.onGrid.x;
            y = current.onGrid.y - 1;
        }

        return sensors.floorPlan.floorLayout.get(x).get(y);
    }


    private boolean traverseStackContains(FloorNode nodeOption) {
        for (FloorNode stackNode : traverseStack) {
            if (stackNode.onGrid.x == nodeOption.onGrid.x && stackNode.onGrid.y == nodeOption.onGrid.y) {
                return true;
            }
        }

        return false;
    }


    public void turnOn() {
        currentState = State.ON;
        logger.logStartTime();
        try {
            traverseFloor();
        } catch (InterruptedException | FullCapacityException | LowBatteryException e) {
            System.out.println("Clean Sweep cannot be turned on. Please contact customer support.");
            e.printStackTrace();
        }
    }
}


class LowBatteryException extends Exception {
    public LowBatteryException() { }
}


class FullCapacityException extends Exception {
    public FullCapacityException() { }
}

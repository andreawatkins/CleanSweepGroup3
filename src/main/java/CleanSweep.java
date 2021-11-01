import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Thread.sleep;

public class CleanSweep {
    public double battery;
    double currCapacity;
    double totalCapacity = 50.0;
    public FloorCell currentLocation;
    public FloorCell previousLocation;
    public SensorSimulator sensors;
    private CleanSweep cleanSweep = null;
    public State currentState;

    private Stack<FloorNode> traverseStack = new Stack<>();
    private ArrayList<FloorCell> visitedCells = new ArrayList<>();


    public CleanSweep(Double battery, double currCapacity, SensorSimulator sensors, FloorCell currentLocation, FloorCell previousLocation) {
        this.sensors = sensors;
        this.currentLocation = currentLocation;
        this.previousLocation = previousLocation;
        this.battery = battery;
        this.currCapacity = currCapacity;

    }


    public void turnOff() {
        currentState = State.OFF;
        System.out.println("CleanSweep is shutting down...");
        System.exit(0);
    }


    public boolean isOn() {
        return (currentState.equals(State.ON));
    }


    public double useBattery() {

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
            System.out.println(this.currentState);
        } else System.out.println("Battery: " + String.format("%.1f", (battery / 250) * 100) + "% left");

        return battery;
    }


    public boolean needToCharge() {
        if (battery < .20 * 250) {
            currentState = State.LOW_BATTERY;
            System.out.println("Need to charge! Low Battery!");

            return true;
        } else return false;
    }


    public void suckUpDirt() throws InterruptedException {
        if (isOn()) {
            while (currentLocation.dirtAmount > 0 && (((currCapacity / totalCapacity) * 100)<100)) {
                System.out.println("Cleaning... " + currentLocation.dirtAmount + " unit" + (currentLocation.dirtAmount == 1 ? "" : "s") + " of dirt left");
                currentLocation.dirtAmount--;
                currCapacity++;
                sleep(1000);
            }
            if (((currCapacity / totalCapacity) * 100)>= 100){
                currentState= State.AT_CAPACITY;
                System.out.println(currentState);
            }
        }
        else
        System.out.println("Clean!\n");
        System.out.println("Capacity: " + String.format("%.1f", (currCapacity / totalCapacity) * 100) + "% full");

    }


    public void moveToAdjacentNode(FloorNode destination) {
        previousLocation = currentLocation;

        if (isOn()) {
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
            System.out.format("%-15s%10s\n", "Moved north.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void moveSouth() {
        if (isOn()) {
            int x = sensors.currentLocation.x + 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            System.out.format("%-15s%10s\n", "Moved south.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void moveEast() {
        if (isOn()) {
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y + 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            System.out.format("%-15s%10s\n", "Moved east.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void moveWest() {
        if (isOn()) {
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y - 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
            System.out.printf("%-15s%10s\n", "Moved west.", String.format("(%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));
        }
    }


    public void updateCurrentCell() {
        int x = sensors.currentLocation.x;
        int y = sensors.currentLocation.y;

        currentLocation = sensors.floorPlan.floorLayout.get(x).get(y);
    }


    public void zigZag() throws InterruptedException {
        if (isOn()) {
            System.out.println();
            sensors.floorPlan.print(FloorPlan::printDirtAmount);

            Direction direction = Direction.SOUTH;
            sleep(1000);
            suckUpDirt();
            sleep(1000);
            useBattery();
            sleep(1000);

            System.out.println();
            sensors.floorPlan.print(FloorPlan::printDirtAmount);

            while (!(sensors.isEastWall() && sensors.isSouthWall())) {
                System.out.println();

                if (!sensors.isWall(direction)) {
                    moveDirection(direction);
                } else {
                    moveEast();

                    if (direction == Direction.SOUTH) {
                        direction = Direction.NORTH;
                    } else {
                        direction = Direction.SOUTH;
                    }
                }

                System.out.println(String.format("Current Location: (%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));

                sleep(1000);
                suckUpDirt();
                sleep(1000);
                useBattery();
                sleep(1000);

                System.out.println();
                sensors.floorPlan.print(FloorPlan::printDirtAmount);
            }
        }
        sensors.printVisitedLocations();
        //just a print test to confirm this works.
        //you can also stick this inside of the loop and get an updated map for each cell

        System.out.println("Has CleanSweep visited floorCell 1,1? : " + sensors.hasVisited(sensors.floorPlan.floorLayout.get(1).get(1)));

    }


    public void traverseFloor() throws InterruptedException, FullCapacityException, LowBatteryException {
        if (isOn()) {
            FloorNode startNode = new FloorNode(null, new Location(0, 0), null);
            FloorNode previousNode = startNode;

            traverseStack.push(startNode);

            while (!traverseStack.isEmpty()) {

                FloorNode currentNode = traverseStack.pop();
                if(currentState != State.LOW_BATTERY && currentState != State.AT_CAPACITY ) {

                // MOVE FROM THE PREVIOUS NODE TO THE CURRENT NODE
                // PREVIOUS NODE - last node processed
                // CURRENT NODE  - node we are backtracking to
                if (currentNode != startNode) {
                    if (currentNode.parent != previousNode) { // start node was the previous node
                        System.out.println("\nBacktracking...");
                        FloorNode intermediaryNode = previousNode.parent;
                        moveToAdjacentNode(intermediaryNode);

                        while (intermediaryNode != currentNode.parent) {
                            intermediaryNode = intermediaryNode.parent;
                            moveToAdjacentNode(intermediaryNode);
                        }
                    }

                    moveToAdjacentNode(currentNode);
                }

                // WORK DONE ON CURRENT NODE HERE:
                System.out.println();
                sensors.floorPlan.print(FloorPlan::printDirtAmount);
                System.out.println(String.format("Current Location: (%d, %d)", sensors.currentLocation.x, sensors.currentLocation.y));

                sleep(1000);
                suckUpDirt();
                useBattery();
                visitedCells.add(currentLocation);
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
                else{ returnToCharger(currentNode); break;}
            }

            System.out.println();
            sensors.floorPlan.print(FloorPlan::printDirtAmount);
            System.out.println("DONE!");
        }

    }


    public void returnToCharger(FloorNode fromNode) {

        if(currentState == State.LOW_BATTERY) {
            System.out.println("Low Battery, Returning to Charger");

        }
        if(currentState==State.AT_CAPACITY) {
            System.out.println("Capacity Full, Returning to Charger to be Emptied.");

        }

        FloorNode currentNode = fromNode;

        Stack<FloorNode> toChargingStation = new Stack<>();

        while (currentNode.parent != null) { // if the parent is null, we are at the root of the tree, aka the charging station
            toChargingStation.push(currentNode); // keep track of every step of the way so we can go back eventually
            currentNode = currentNode.parent;
            moveToAdjacentNode(currentNode);
        }

        if(currentState == State.LOW_BATTERY) currentState = State.CHARGING;

        System.out.println("At Charger!");
        System.out.println(currentLocation.rowIndex + ","+currentLocation.colIndex);



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


    private boolean canTraverseStack() {
        if (traverseStack.isEmpty()) {
            return true;
        }

        FloorNode stackNode = traverseStack.lastElement(); //Top of the traverse stack

        int diffX = Math.abs(stackNode.onGrid.x - currentLocation.rowIndex);
        int diffY = Math.abs(stackNode.onGrid.y - currentLocation.colIndex);

        return (diffX == 0 && diffY == 1) || (diffY == 0 && diffX == 1);
    }


    public void turnOn() {
        currentState = State.ON;

        try {
            traverseFloor();
        } catch (InterruptedException | FullCapacityException | LowBatteryException e) {
            System.out.println("Clean Sweep cannot be turned on. Please contact customer support.");
            e.printStackTrace();
        }
    }
}
class LowBatteryException extends Exception {
    public LowBatteryException(FloorNode e) {
        FloorNode fromNode = e;

    }

}
class FullCapacityException extends Exception{
    public FullCapacityException(FloorNode e) {FloorNode fromNode = e; }
}

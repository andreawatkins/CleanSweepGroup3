import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Thread.sleep;

public class CleanSweep {
    private double battery;
    private double currCapacity;
    private double totalCapacity = 50.0;
    public FloorCell currentLocation;
    public FloorCell previousLocation;
    public SensorSimulator sensors;
    public Location location; //starting location
    private CleanSweep cleanSweep = null;
    public State currentState;

    Stack<FloorNode> traverseStack = new Stack<>();
    ArrayList<FloorCell> visitedCells = new ArrayList<>();

    public CleanSweep(Double battery, double currCapacity, SensorSimulator sensors, FloorCell currentLocation, FloorCell previousLocation) {
        this.sensors = sensors;
        this.currentLocation = currentLocation;
        this.previousLocation = previousLocation;
        this.battery = battery;
        this.currCapacity = currCapacity;

    }

    public CleanSweep getInstance() {
        if (cleanSweep == null) {
            synchronized (CleanSweep.class) {
                if (cleanSweep == null) {
                    cleanSweep = new CleanSweep(battery, currCapacity, sensors, currentLocation, previousLocation);
                }
            }
        }
        return cleanSweep;
    }

    public void turnOff() {
        currentState = State.OFF;
        System.out.println("CleanSweep is shutting down...");
        System.exit(0);
    }

    public boolean isOn() {
        return currentState.equals(State.ON);
    }


    public double useBattery() {
        if (isOn()) {
            double batteryDec = 0;

            if (currentLocation.surfaceType == SurfaceType.BARE_FLOOR)
                batteryDec = 1;
            if (currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET)
                batteryDec = 2;
            else if (currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET)
                batteryDec = 3;

            if (previousLocation == currentLocation) {
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
                return battery;
            } else System.out.println("Battery Percent: " + (battery / 250) * 100 + "%");
        }
        return battery;
    }

    public boolean needToCharge() {
        if (battery < .20 * 250) {
            this.currentState = State.LOW_BATTERY;
            System.out.println("Need to charge! Low Battery!");

            return true;
        } else return false;
    }

    public void suckUpDirt() throws InterruptedException {
        if (isOn()) {
            while (currentLocation.dirtAmount > 0) {
                System.out.println("Cleaning... " + currentLocation.dirtAmount + " units of dirt left");
                currentLocation.dirtAmount--;
                currCapacity++;
                sleep(1000);
            }
        }


        System.out.println("FloorCell is Clean!");
        System.out.println("Capacity : " + (currCapacity / totalCapacity) * 100 + "% full");

    }


    public void move(FloorNode destination) {
        if (destination.onGrid.x > currentLocation.rowIndex) {
            moveSouth();
        }
        else if (destination.onGrid.x < currentLocation.rowIndex) {
            moveNorth();
        }

        if (destination.onGrid.y > currentLocation.colIndex) {
            moveEast();
        }
        else if (destination.onGrid.y < currentLocation.colIndex) {
            moveWest();
        }
    }

    public Direction moveDirection(Direction direction) {
        Direction moveDirection = null;

        FloorCell tempPrev = currentLocation;
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
            tempPrev = previousLocation;
        }
        return moveDirection;
    }


    public void moveNorth() {
        if (isOn()) {
            System.out.println("Move North");
            int x = sensors.currentLocation.x - 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void moveSouth() {
        if (isOn()) {
            System.out.println("Move South");
            int x = sensors.currentLocation.x + 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void moveEast() {
        if (isOn()) {
            System.out.println("Move East");
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y + 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void moveWest() {
        if (isOn()) {
            System.out.println("Move West");
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y - 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void updateCurrentCell() {
        int x = sensors.currentLocation.x;
        int y = sensors.currentLocation.y;

        currentLocation = sensors.floorPlan.floorLayout.get(x).get(y);
    }

    public void zigZag() throws InterruptedException {
        if (isOn()) {
            Direction direction = Direction.SOUTH;
            sleep(1000);
            suckUpDirt();
            sleep(1000);
            useBattery();
            sleep(1000);
            sensors.floorPlan.print();

            while (!(sensors.isEastWall() && sensors.isSouthWall())) {
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
                sleep(1000);
                suckUpDirt();
                sleep(1000);
                useBattery();
                sleep(1000);
                sensors.floorPlan.print();

                System.out.format("Current Location \n x: %d, y: %d\n", sensors.currentLocation.x, sensors.currentLocation.y);
            }
        }
    }

    public void traverseFloor() throws InterruptedException {
        FloorNode startNode = new FloorNode(null, new Location(0,0), null); //Hard code start location for now
        FloorNode previousNode = startNode;

        traverseStack.push(startNode);

        while (!traverseStack.isEmpty()) {
            FloorNode currentNode = traverseStack.pop();
            currentNode.parent = previousNode;


            for (Direction movingDirection : sensors.getTraversableDirections(currentNode.onGrid)) {
                FloorCell cellOption = nextCell(currentNode, movingDirection);
                FloorNode nodeOption = new FloorNode(currentNode, cellOption.location, movingDirection);

                if (!visitedCells.contains(cellOption) && !traverseStackContains(nodeOption)) {
                    traverseStack.push(nodeOption);
                }
            }

            move(currentNode); //Move robot to cell corresponding to the current node
            visitedCells.add(currentLocation);

            // Do work here
            suckUpDirt();

            if(currentNode != startNode) {
                while(!canTraverseStack()) {
                    currentNode = currentNode.parent;
                    move(currentNode);
                }
            }

            previousNode = currentNode;
        }

        returnToCharger();
    }

    public void returnToCharger() {
        moveWest();
    }

    public FloorCell nextCell(FloorNode parent, Direction travelingDirection) {
        int x;
        int y;

        if (travelingDirection == Direction.NORTH) {
            x = parent.onGrid.x - 1;
            y = parent.onGrid.y;
        }
        else if (travelingDirection == Direction.EAST) {
            x = parent.onGrid.x;
            y = parent.onGrid.y + 1;
        }
        else if (travelingDirection == Direction.SOUTH) {
            x = parent.onGrid.x + 1;
            y = parent.onGrid.y;
        }
        else {
            x = parent.onGrid.x;
            y = parent.onGrid.y - 1;
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
        } catch (InterruptedException e) {
            System.out.println("CleanSweep cannot be turned on. Please contact customer support.");
            e.printStackTrace();
        }
    }
}

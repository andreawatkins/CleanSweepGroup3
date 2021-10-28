import static java.lang.Thread.sleep;

public class CleanSweep {
    private double battery;
    private double currCapacity;
    private double totalCapacity = 50.0;
    public FloorCell currentLocation;
    public FloorCell previousLocation;
    public SensorSimulator sensors;
    public Location location; // starting location
    private CleanSweep cleanSweep = null;
    public State currentState;


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
            } else System.out.println("Battery: " + String.format("%.1f", (battery / 250) * 100) + "% left");
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
                System.out.println("Cleaning... " + currentLocation.dirtAmount + " unit" + (currentLocation.dirtAmount == 1 ? "" : "s") + " of dirt left");
                currentLocation.dirtAmount--;
                currCapacity++;
                sleep(1000);
            }
        }

        System.out.println("Clean!\n");
        System.out.println("Capacity: " + String.format("%.1f", (currCapacity / totalCapacity) * 100) + "% full");

    }

    public void move(Direction direction) {
        sensors.visitedCells(sensors.floorPlan);
        FloorCell tempPrev = currentLocation;
        if (isOn()) {
            if (direction == Direction.SOUTH) {
                moveSouth();
            }

            if (direction == Direction.EAST) {
                moveEast();
            }

            if (direction == Direction.NORTH) {
                moveNorth();
            }

            if (direction == Direction.WEST) {
                moveWest();
            }
            tempPrev = previousLocation;
        }
    }

    public void moveNorth() {
        if (isOn()) {
            System.out.println("Moved north.");
            int x = sensors.currentLocation.x - 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void moveSouth() {
        if (isOn()) {
            System.out.println("Moved south.");
            int x = sensors.currentLocation.x + 1;
            int y = sensors.currentLocation.y;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void moveEast() {
        if (isOn()) {
            System.out.println("Moved east.");
            int x = sensors.currentLocation.x;
            int y = sensors.currentLocation.y + 1;

            sensors.currentLocation = new Location(x, y);
            updateCurrentCell();
        }
    }

    public void moveWest() {
        if (isOn()) {
            System.out.println("Moved west.");
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
            System.out.println();
            sensors.floorPlan.print(FloorPlan::printDirtAmount);

            Direction direction = Direction.SOUTH;
            //sleep(1000);
            suckUpDirt();
            //sleep(1000);
            useBattery();
            //sleep(1000);

            System.out.println();
            sensors.floorPlan.print(FloorPlan::printDirtAmount);

            while (!(sensors.isEastWall() && sensors.isSouthWall())) {
                System.out.println();

                if (!sensors.isWall(direction)) {
                    move(direction);
                } else {
                    moveEast();

                    if (direction == Direction.SOUTH) {
                        direction = Direction.NORTH;
                    } else {
                        direction = Direction.SOUTH;
                    }
                }

                System.out.format("Current Location: (%d, %d)\n", sensors.currentLocation.x, sensors.currentLocation.y);

                //sleep(1000);
                suckUpDirt();
                //sleep(1000);
                useBattery();
                //sleep(1000);

                System.out.println();
                sensors.floorPlan.print(FloorPlan::printDirtAmount);
            }
        }
        sensors.printVisitedLocations();
        //just a print test to confirm this works.
        //you can also stick this inside of the loop and get an updated map for each cell

        System.out.println("Has CleanSweep visited floorCell 1,1? : " + sensors.hasVisited(sensors.floorPlan.floorLayout.get(1).get(1)));
        
    }

    public void turnOn() {
        currentState = State.ON;
        try {
            zigZag();
        } catch (InterruptedException e) {
            System.out.println("Clean Sweep cannot be turned on. Please contact customer support.");
            e.printStackTrace();
        }
    }
}

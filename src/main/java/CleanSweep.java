import static java.lang.Thread.sleep;

public class CleanSweep {
    //this class should have an instance of sensor simulator.
    //sensor simulator should check the current floor cell for details like surface type,
    //dirt level, whether there are walls to N, S, E, W, etc.) via some method like "report"
    private double battery;
    private double currCapacity;
    private double totalCapacity = 50.0;
    public FloorCell currentLocation;
    public FloorCell previousLocation;
    public SensorSimulator sensors;
    public Location location; //starting location
    private CleanSweep cleanSweep = null;
    public State currentState;


    public CleanSweep(Double battery, double currCapacity, SensorSimulator sensors, FloorCell currentLocation, FloorCell previousLocation) {
        this.sensors = sensors;
        this.currentLocation = currentLocation;
        this.previousLocation = previousLocation;
        this.battery = battery;
        this.currCapacity = currCapacity;

    }

    public CleanSweep getInstance() {
        //singleton for the CleanSweep - need to set some vars private later
        if (cleanSweep == null) {
            synchronized (CleanSweep.class) {
                if (cleanSweep == null) {
                    cleanSweep = new CleanSweep(battery, currCapacity, sensors, currentLocation, previousLocation);
                }
            }
        }
        return cleanSweep;
    }


    public double useBattery() {
        double batteryDec = 0;

        if (currentLocation.surfaceType == SurfaceType.BARE_FLOOR)
            batteryDec = 1;
        if (currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET)
            batteryDec = 2;
        else if (currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET)
            batteryDec = 3;

        if (previousLocation == currentLocation) {
            battery = battery - batteryDec;
        }

        else {
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
        if (needToCharge()) {System.out.println(this.currentState); return battery;}
        else System.out.println("Battery Percent: " + (battery/250)*100 + "%" ); return battery;

    }

    public boolean needToCharge() {
        if (battery < .20 * 250) {
            this.currentState = State.LOW_BATTERY;
            System.out.println("Need to charge! Low Battery!");

            return true;
        } else return false;
    }

    public void suckUpDirt() throws InterruptedException {
        while (currentLocation.dirtAmount > 0) {
            System.out.println("Cleaning... " + currentLocation.dirtAmount + " units of dirt left");
            currentLocation.dirtAmount--;
            currCapacity ++;
            sleep(1000);





        }


        System.out.println("FloorCell is Clean!");
        System.out.println("Capacity : "  + (currCapacity/totalCapacity) *100 + "% full");

    }

    public void move(Direction direction) {
        FloorCell tempPrev = currentLocation;
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

    public void moveNorth() {
        System.out.println("Move North");
        int x = sensors.currentLocation.x - 1;
        int y = sensors.currentLocation.y;

        sensors.currentLocation = new Location(x,y);
        updateCurrentCell();
    }

    public void moveSouth() {
        System.out.println("Move South");
        int x = sensors.currentLocation.x + 1;
        int y = sensors.currentLocation.y;

        sensors.currentLocation = new Location(x,y);
        updateCurrentCell();
    }

    public void moveEast() {
        System.out.println("Move East");
        int x = sensors.currentLocation.x;
        int y = sensors.currentLocation.y + 1;

        sensors.currentLocation = new Location(x,y);
        updateCurrentCell();
    }

    public void moveWest() {
        System.out.println("Move West");
        int x = sensors.currentLocation.x;
        int y = sensors.currentLocation.y - 1;

        sensors.currentLocation = new Location(x,y);
        updateCurrentCell();
    }

    public void updateCurrentCell() {
        int x = sensors.currentLocation.x;
        int y = sensors.currentLocation.y;


        currentLocation = sensors.floorPlan.floorLayout.get(x).get(y);
    }

    public void zigZag() throws InterruptedException {
        Direction direction = Direction.SOUTH;
        sleep(1000);
        suckUpDirt();
        sleep(1000);
        useBattery();
        sleep(1000);
        sensors.floorPlan.print();

        while(!(sensors.isEastWall() && sensors.isSouthWall())) {
            if(!sensors.isWall(direction)) {
                move(direction);
            }
            else {
                moveEast();

                if (direction == Direction.SOUTH) {
                    direction = Direction.NORTH;
                }
                else {
                    direction = Direction.SOUTH;
                }
            }
            sleep(1000);
            suckUpDirt();
            sleep(1000);
            useBattery();
            sleep(1000);
            sensors.floorPlan.print();

            System.out.format("Current Location \n x: %d, y: %d\n",sensors.currentLocation.x,sensors.currentLocation.y);
        }
    }
}

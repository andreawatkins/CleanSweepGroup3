import java.util.ArrayList;

public class CleanSweep {
    //this class should have an instance of sensor simulator.
    //sensor simulator should check the current floor cell for details like surface type,
    //dirt level, whether there are walls to N, S, E, W, etc.) via some method like "report"
    private int battery;
    private int currCapacity;
    private int totalCapacity;
    private FloorCell currentLocation;

    public CleanSweep(int battery, int currCapacity, int totalCapacity) {
        this.battery = battery;
        this.currCapacity = currCapacity;
        this.totalCapacity = totalCapacity;
    }

    public boolean needToCharge() {
        return battery < 10;
    }

    public String suckUpDirt() {
        while (currentLocation.dirtAmount > 0) {
            currentLocation.dirtAmount--;
        }
        return "All Clean!";

    }

    public SensorSimulator sensors;
    public Location location; //starting location
    private CleanSweep cleanSweep = null;

    public CleanSweep(SensorSimulator sensors, Location location) {
        this.sensors = sensors;
        this.location = location;

    }

    public CleanSweep getInstance() {
        //singleton for the CleanSweep - need to set some vars private later
        if (cleanSweep == null) {
            synchronized (CleanSweep.class) {
                if (cleanSweep == null) {
                    cleanSweep = new CleanSweep(sensors, location);
                }
            }
        }
        return cleanSweep;
    }

    public void move(Direction direction) {
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

    public void zigZag() {
        Direction direction = Direction.SOUTH;

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

            suckUpDirt();

            System.out.format("x: %d, y: %d\n",sensors.currentLocation.x,sensors.currentLocation.y);
        }
    }




}

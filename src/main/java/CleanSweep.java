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
}

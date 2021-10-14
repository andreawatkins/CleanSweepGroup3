public class CleanSweep {
    //this class should have an instance of sensor simulator.
    //sensor simulator should check the current floor cell for details like surface type,
    //dirt level, whether there are walls to N, S, E, W, etc.) via some method like "report"

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

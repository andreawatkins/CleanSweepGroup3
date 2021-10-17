public class CleanSweep {
    //this class should have an instance of sensor simulator.
    //sensor simulator should check the current floor cell for details like surface type,
    //dirt level, whether there are walls to N, S, E, W, etc.) via some method like "report"
    private double battery;
    private int currCapacity;
    private int totalCapacity = 60;
    private FloorCell currentLocation;
    private FloorCell previousLocation;
    public SensorSimulator sensors;
    public Location location; //starting location
    private CleanSweep cleanSweep = null;
    public State currentState;


    public CleanSweep(Double battery, int currCapacity, SensorSimulator sensors, FloorCell currentLocation, FloorCell previousLocation) {
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
        else return battery;
    }

    public boolean needToCharge() {
        if (battery < .20 * 250) {
            this.currentState = State.LOW_BATTERY;
            System.out.println("Need to charge! Low Battery!");

            return true;
        } else return false;
    }

    public void suckUpDirt() {
        while (currentLocation.dirtAmount > 0) {
            currentLocation.dirtAmount--;
            currCapacity ++;
            System.out.println("Cleaning... " + previousLocation.dirtAmount + " units of dirt left");
        }

        System.out.println("FloorCell is Clean!");
        System.out.println("Capacity at : "  + currCapacity + " units");

    }
}

public class CleanSweep {
    //this class should have an instance of sensor simulator.
    //sensor simulator should check the current floor cell for details like surface type,
    //dirt level, whether there are walls to N, S, E, W, etc.) via some method like "report"
    private double battery;
    private int currCapacity;
    private int totalCapacity;
    private FloorCell currentLocation;
    private FloorCell previousLocation;
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

    public CleanSweep(int battery, int currCapacity, int totalCapacity) {
        this.battery = battery;
        this.currCapacity = currCapacity;
        this.totalCapacity = totalCapacity;
    }

    public boolean needToCharge () {
        if( battery < .20*250) {
            System.out.println("Need to charge! Low Battery!");
            return true;
        }
        else return false;
    }

    public void suckUpDirt() {
        while (currentLocation.dirtAmount > 0) {
            currentLocation.dirtAmount --;
            System.out.println(currentLocation.dirtAmount);
        }
        System.out.println("All Clean!");

    }
    /*The Clean Sweep has a limited battery life of 250 units of charge. Each movement and vacuum
     operation requires units of charge depending on the surfaces being traversed:
     • Bare floor - 1 unit • Low-pile carpet - 2 units • High-pile carpet - 3 units
     The charge required for the Clean Sweep to move from location A to location B is the average
     of the required charge costs for the surfaces at the two locations.
     For example, if the Clean Sweep moves from a bare floor to low-pile carpet, then the charge required is 1.5 units.
     It costs the same amount of charge to clean the current location is it does to traverse that location.
     The Clean Sweep should always return to its charging station before it runs out of power.
     Homeowners that come home and discover their robotic vacuum out of power in the middle of a room
     tend to be dissatisfied customers.
     As it returns to its charging station, the Clean Sweep will not perform any cleaning.
     Upon returning to its charging station, the Clean Sweep will automatically re-charge to full capacity.
     Once re-charged, it will resume cleaning until it detects that it has visited every accessible location
     on the current floor.*/
    public double useBattery(){
        double batteryDec =0;

        if (currentLocation.surfaceType == SurfaceType.BARE_FLOOR)
            batteryDec = 1;
        if (currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET)
            batteryDec = 2;
        else if (currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET)
            batteryDec = 3;

        if(previousLocation==currentLocation) {
            this.battery = this.battery - batteryDec;
            return battery;
        }

        else{
            if((currentLocation.surfaceType == SurfaceType.BARE_FLOOR) && (previousLocation.surfaceType == SurfaceType.LOW_PILE_CARPET))
                this.battery = this.battery - ((batteryDec + 2) /2);
            if((currentLocation.surfaceType == SurfaceType.BARE_FLOOR) && (previousLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET))
                this.battery = this.battery - ((batteryDec + 3)/2);
            if((currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.BARE_FLOOR))
                this.battery = this.battery - ((batteryDec + 1) /2);
            if((currentLocation.surfaceType == SurfaceType.LOW_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET))
                this.battery = this.battery - ((batteryDec + 3) /2);
            if((currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.BARE_FLOOR))
                this.battery = this.battery - ((batteryDec + 1) /2);
            if((currentLocation.surfaceType == SurfaceType.HIGH_PILE_CARPET) && (previousLocation.surfaceType == SurfaceType.LOW_PILE_CARPET))
                this.battery = this.battery - ((batteryDec + 2) /2);
            return battery;



        }
    }


}

public class CleanSweep {
    private  int battery;
    private  int currCapacity;
    private  int totalCapacity;
    private FloorCell currentLocation;

    public CleanSweep(int battery, int currCapacity, int totalCapacity) {
        this.battery = battery;
        this.currCapacity = currCapacity;
        this.totalCapacity = totalCapacity;
    }

    public boolean needToCharge () {
        return battery < 10;
    }

    public String suckUpDirt() {
        while (currentLocation.dirtAmount > 0) {
            currentLocation.dirtAmount --;
        }
        return "All Clean!";

    }

}

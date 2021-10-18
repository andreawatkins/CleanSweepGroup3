public class Main {
    public static void main(String[] args) {
        FloorPlan floorPlan = new FloorPlan(3, 3);
        floorPlan.print();

        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(floorPlan, startingLocation);

        sensor.print();

        CleanSweep cs = new CleanSweep(250.0, 60, sensor, floorPlan.floorLayout.get(0).get(0), floorPlan.floorLayout.get(0).get(0) );
        cs.suckUpDirt();
        System.out.println(cs.useBattery());
    }
}

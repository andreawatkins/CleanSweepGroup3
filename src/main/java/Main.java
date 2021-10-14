public class Main {
    public static void main(String[] args) {
        FloorPlan floorPlan = new FloorPlan(3, 3);
        floorPlan.print();

        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(floorPlan, startingLocation);

        sensor.print();
    }
}

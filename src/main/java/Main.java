import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Direction> directions;
        FloorPlan floorPlan = new FloorPlan(3, 4);
        floorPlan.print();

        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(floorPlan, startingLocation);

        sensor.print();

        CleanSweep cs = new CleanSweep(250.0, 0, sensor, floorPlan.floorLayout.get(0).get(0), floorPlan.floorLayout.get(0).get(0));
        /*directions = sensor.getTraversableDirections(startingLocation);
        int i = 0;
        for (Direction d: directions) {
            cs.move(directions.get(i));
            i++;
            }*/
        cs.turnOn();
    }
}


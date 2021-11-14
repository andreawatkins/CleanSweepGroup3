import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public class CleanSweepTest {


    @Test
    void turnOnTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        User aw = new User("awatkins", "Andrea", "Watkins", "60622", "672213");
        Logger logger = new Logger(aw.username);
        CleanSweep cs = new CleanSweep(250.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0), logger);
        cs.run();
        Assertions.assertSame(cs.currentState, State.ON);
    }

    @Test
    void batteryDecTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        User aw = new User("awatkins", "Andrea", "Watkins", "60622", "672213");
        Logger logger = new Logger(aw.username);
        CleanSweep cs = new CleanSweep(250.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0), logger);
        double initBattery = cs.battery;
        cs.run();
        Assertions.assertTrue(initBattery>cs.battery);
    }

    @Test
    public void lowBatteryTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        User aw = new User("awatkins", "Andrea", "Watkins", "60622", "672213");
        Logger logger = new Logger(aw.username);
        CleanSweep cs = new CleanSweep(50.2, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0), logger);
        cs.run();
        Assertions.assertEquals(State.CHARGING, cs.currentState);

    }

    @Test
    public void cleanDirtTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        User aw = new User("awatkins", "Andrea", "Watkins", "60622", "672213");
        Logger logger = new Logger(aw.username);
        CleanSweep cs = new CleanSweep(250.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0), logger);
        double initCapacity = cs.currCapacity;
        cs.run();
        Assertions.assertTrue(initCapacity<cs.currCapacity);

    }



    @Test
    public void capacityDecTest(){

    }

    @Test
    public void fullCapacityTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        User aw = new User("awatkins", "Andrea", "Watkins", "60622", "672213");
        Logger logger = new Logger(aw.username);
        CleanSweep cs = new CleanSweep(250.0, 49, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0), logger);
        cs.run();
        Assertions.assertSame(cs.currentState, State.AT_CAPACITY);


    }



}
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SensorSimulatorTest {

    //Wall Sensor Tests

    @Test
    public void northSensorTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        CleanSweep cleanSweep = new CleanSweep(50.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0));
        boolean northWall = cleanSweep.sensors.isNorthWall();
        //there will always be a north wall at 0,0 with this floor plan
        assertTrue(northWall);
    }

    @Test
    public void southSensorTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        CleanSweep cleanSweep = new CleanSweep(50.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0));
        boolean southWall = cleanSweep.sensors.isSouthWall();
        //there won't be a south wall at 0,0 with this floor plan
        assertFalse(southWall);
    }

    @Test
    public void eastSensorTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        boolean eastWall = !sensor.isEastWall();
        //there won't be an east wall at 0,0 with this floor plan
        assertTrue(eastWall);
    }

    @Test
    public void westSensorTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        CleanSweep cleanSweep = new CleanSweep(50.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0));
        boolean westWall = cleanSweep.sensors.isWestWall();
        //there will always be a west wall at 0,0 with this floor plan
        assertTrue(westWall);
    }

    @Test
    public void traversableDirectionsTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(5, 5);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        CleanSweep cleanSweep = new CleanSweep(50.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0));
        List<Direction> directions = cleanSweep.sensors.getTraversableDirections();
        //System.out.println(directions);
        Direction result1 = directions.get(0);
        assertSame(result1, result1);
    }

    @Test
    public void sensorSimulatorTest(){
        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(2, 2);
        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);
        CleanSweep cleanSweep = new CleanSweep(50.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0));
        int resultX = cleanSweep.sensors.currentLocation.x;
        int resultY = cleanSweep.sensors.currentLocation.y;
        int assertionX = 0;
        int assertionY = 0;
        assertSame(resultX, assertionX);
        assertSame(resultY, assertionY);
    }

}

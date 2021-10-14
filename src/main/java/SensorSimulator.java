import java.util.ArrayList;
import java.util.List;

public class SensorSimulator {
    public FloorPlan floorPlan;
    public Location currentLocation;

    public SensorSimulator(FloorPlan floorPlan, Location currentLocation) {
        this.floorPlan = floorPlan;
        this.currentLocation = currentLocation;
    }

    public boolean isNorthWall() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).wallsPresent.contains(Wall.NORTH_WALL);

    }

    public boolean isSouthWall() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).wallsPresent.contains(Wall.SOUTH_WALL);

    }

    public boolean isWestWall() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).wallsPresent.contains(Wall.WEST_WALL);

    }

    public boolean isEastWall() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).wallsPresent.contains(Wall.EAST_WALL);

    }

    public boolean isWall(Direction direction) {
        if (direction.equals(Direction.EAST)) {
            return isEastWall();
        } else if (direction.equals(Direction.WEST)) {
            return isWestWall();
        } else if (direction.equals(Direction.SOUTH)) {
            return isSouthWall();
        } else {
            return isNorthWall();
        }
    }

    public boolean isNorthObstacle() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.OBSTACLE);

    }

    public boolean isSouthObstacle() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.OBSTACLE);

    }

    public boolean isWestObstacle() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.OBSTACLE);

    }

    public boolean isEastObstacle() {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        return floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.OBSTACLE);

    }

    public boolean isObstacle(Direction direction) {
        if (direction.equals(Direction.EAST)) {
            return isEastObstacle();
        } else if (direction.equals(Direction.WEST)) {
            return isWestObstacle();
        } else if (direction.equals(Direction.SOUTH)) {
            return isSouthObstacle();
        } else {
            return isNorthObstacle();
        }
    }

    public List<Direction> getTraversableDirections(Location currentLocation) {
        List<Direction> openDirections = new ArrayList<>();
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        FloorCell cell = floorPlan.floorLayout.get(x).get(y);
        if (!cell.wallsPresent.contains(Wall.NORTH_WALL)) {
            openDirections.add(Direction.NORTH);
        }
        if (!cell.wallsPresent.contains(Wall.EAST_WALL)) {
            openDirections.add(Direction.EAST);
        }
        if (!cell.wallsPresent.contains(Wall.SOUTH_WALL)) {
            openDirections.add(Direction.SOUTH);
        }
        if (!cell.wallsPresent.contains(Wall.WEST_WALL)) {
            openDirections.add(Direction.WEST);
        } else if (cell.wallsPresent.contains(Wall.NORTH_WALL)
                && cell.wallsPresent.contains(Wall.SOUTH_WALL)
                && cell.wallsPresent.contains(Wall.EAST_WALL)
                && cell.wallsPresent.contains(Wall.WEST_WALL)) {
            System.out.println("Error. Clean Sweep is stuck.");
        }
        return openDirections;
    }

    /*
    public Location getCurrentLocation(){

    }
     */

    /* public HashMap<Location, Integer>
    public HashMap<Location, Integer> visitedCells(){;
    //something like: while state.ON -> for every "move" from one cell to another,
    //check whether coordinates are in the map. if not,drop in the coordinates as key
    //and a 1 for visited. If the coordinates already exist, check whether
    }
     */

    public void print() {
        //testing direction-parameterized wall bool
        System.out.println("------------------");
        System.out.println("Wall to the north? " + isWall(Direction.NORTH));
        System.out.println("Wall to the south? " + isWall(Direction.SOUTH));
        System.out.println("Wall to the east? " + isWall(Direction.EAST));
        System.out.println("Wall to the west? " + isWall(Direction.WEST));

        System.out.println("------------------");
        //testing output of traversable options
        System.out.println();
        System.out.println("The following are traversable directions: " + getTraversableDirections(currentLocation));
        System.out.println();

        System.out.println("------------------");
    }

}

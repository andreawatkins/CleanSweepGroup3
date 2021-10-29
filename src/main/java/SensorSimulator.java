import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorSimulator {
    public FloorPlan floorPlan;
    public Location currentLocation;
    public HashMap<FloorCell, Integer> visited = new HashMap<>();

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
        if (!cell.wallsPresent.contains(Wall.NORTH_WALL) || isObstacle(Direction.NORTH)) {
            openDirections.add(Direction.NORTH);
        }
        if (!cell.wallsPresent.contains(Wall.EAST_WALL) || isObstacle(Direction.EAST)) {
            openDirections.add(Direction.EAST);
        }
        if (!cell.wallsPresent.contains(Wall.SOUTH_WALL) || isObstacle(Direction.SOUTH)) {
            openDirections.add(Direction.SOUTH);
        }
        if (!cell.wallsPresent.contains(Wall.WEST_WALL) || isObstacle(Direction.WEST)) {
            openDirections.add(Direction.WEST);
        } else if ((cell.wallsPresent.contains(Wall.NORTH_WALL)
                && cell.wallsPresent.contains(Wall.SOUTH_WALL)
                && cell.wallsPresent.contains(Wall.EAST_WALL)
                && cell.wallsPresent.contains(Wall.WEST_WALL))
                || (isObstacle(Direction.NORTH)
                && isObstacle(Direction.SOUTH)
                && isObstacle(Direction.EAST))
                && isObstacle(Direction.WEST)) {
            System.out.println("Error. Clean Sweep is stuck.");
        }
        return openDirections;
    }


    public HashMap<FloorCell, Integer> visitedCells(FloorPlan floorPlan) {
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        int visitCount = 0;
        FloorCell cell = floorPlan.floorLayout.get(x).get(y);
        if (!visited.containsKey(cell)) {
            visited.put(cell, ++visitCount);
        } else {
            visited.put(cell, visited.get(cell) + 1);
        }
        return visited;
    }

    public boolean hasVisited(FloorCell floorCell) {
        if (visited.get(floorCell) >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public void printVisitedLocations() {
        for (Map.Entry<FloorCell, Integer> entry : visited.entrySet()) {
            System.out.println("Location Coordinates: ");
            System.out.println("x: " + entry.getKey().location.getX() + ", "
                    + "y: " + entry.getKey().location.getY() + " - "
                    + " Times Visited: " + entry.getValue());
        }
    }


    public void print() {
        //testing direction-parameterized wall bool
        /*
        System.out.println("------------------");
        System.out.println("Wall to the north? " + isWall(Direction.NORTH));
        System.out.println("Wall to the south? " + isWall(Direction.SOUTH));
        System.out.println("Wall to the east? " + isWall(Direction.EAST));
        System.out.println("Wall to the west? " + isWall(Direction.WEST));


         */
        System.out.println("------------------");
        //testing output of traversable options
        System.out.println();
        System.out.println("The following are traversable directions: " + getTraversableDirections(currentLocation));
        System.out.println();

        System.out.println("------------------");
    }

}

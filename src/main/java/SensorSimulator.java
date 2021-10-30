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


    private boolean isUntraversableSurfaceType(int x, int y) {
        if (x < 0 || x >= floorPlan.height || y < 0 || y > floorPlan.width)
            return true;

        return floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.OBSTACLE)  ||
               floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.UNDEFINED) ||
               floorPlan.floorLayout.get(x).get(y).surfaceType.equals(SurfaceType.UNDEFINED_BORDER);
    }


    public boolean isNorthObstacle() {
        return isUntraversableSurfaceType(currentLocation.getX() - 1, currentLocation.getY());
    }


    public boolean isSouthObstacle() {
        return isUntraversableSurfaceType(currentLocation.getX() + 1, currentLocation.getY());
    }


    public boolean isWestObstacle() {
        return isUntraversableSurfaceType(currentLocation.getX(), currentLocation.getY() - 1);
    }


    public boolean isEastObstacle() {
        return isUntraversableSurfaceType(currentLocation.getX(), currentLocation.getY() + 1);
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


    public List<Direction> getTraversableDirections() {
        List<Direction> openDirections = new ArrayList<>();

        if (!isNorthWall() && !isNorthObstacle())
            openDirections.add(Direction.NORTH);

        if (!isEastWall() && !isEastObstacle())
            openDirections.add(Direction.EAST);

        if (!isSouthWall() && !isSouthObstacle())
            openDirections.add(Direction.SOUTH);

        if (!isWestWall() && !isWestObstacle())
            openDirections.add(Direction.WEST);

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
}

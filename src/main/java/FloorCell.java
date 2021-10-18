import java.util.EnumSet;

enum SurfaceType {
    BARE_FLOOR,
    LOW_PILE_CARPET,
    HIGH_PILE_CARPET,
    OBSTACLE,
    CHARGING_STATION
}


enum Wall {
    WEST_WALL,
    EAST_WALL,
    NORTH_WALL,
    SOUTH_WALL
}


public class FloorCell {
    public SurfaceType surfaceType;
    public int dirtAmount; // [0-5]; 0 is no dirt, 5 is a lot of dirt
    public EnumSet<Wall> wallsPresent;
    public int rowIndex, colIndex; // where in the FloorPlan it is
    public Location location;


    public FloorCell(SurfaceType surfaceType, int dirtAmount, EnumSet<Wall> wallsPresent, int rowIndex, int colIndex) {
        this.surfaceType = surfaceType;
        this.dirtAmount = dirtAmount;
        this.wallsPresent = wallsPresent;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.location = new Location (rowIndex, colIndex);
    }
}

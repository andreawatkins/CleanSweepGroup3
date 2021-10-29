import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;


public class FloorPlan {
    public List<List<FloorCell>> floorLayout = new ArrayList<>();
    public int width, height;


    private FloorPlan(int width, int height) { this.width = width; this.height = height; }
    public static FloorPlan oneRoomFloorPlan(int width, int height) {
        FloorPlan floorPlan = new FloorPlan(width, height);

        // randomized to either: BARE_FLOOR (0), LOW_PILE_CARPET (1), or HIGH_PILE_CARPET (2)
        SurfaceType surfaceType = SurfaceType.values()[ThreadLocalRandom.current().nextInt(0, 3)]; // random int: [0-2]

        for (int i = 0; i < height; ++i) {
            List<FloorCell> row = new ArrayList<>();

            for (int j = 0; j < width; ++j) {
                int dirtAmount = ThreadLocalRandom.current().nextInt(0, 6); // [0-5]

                EnumSet<Wall> wallsPresent = EnumSet.noneOf(Wall.class); // no walls at first

                if (i == 0) // first row
                    wallsPresent.add(Wall.NORTH_WALL);
                else if (i == height - 1) // last row
                    wallsPresent.add(Wall.SOUTH_WALL);

                if (j == 0) // first column
                    wallsPresent.add(Wall.WEST_WALL);
                else if (j == width - 1) // last column
                    wallsPresent.add(Wall.EAST_WALL);

                int rowIndex = i;
                int colIndex = j;

                row.add(new FloorCell(surfaceType, dirtAmount, wallsPresent, rowIndex, colIndex));
            }

            floorPlan.floorLayout.add(row);
        }

        // top left corner is going to be where our charging station is
        floorPlan.floorLayout.get(0).get(0).surfaceType = SurfaceType.CHARGING_STATION;
        floorPlan.floorLayout.get(0).get(0).dirtAmount = 0;

        int numberOfObstacles = ThreadLocalRandom.current().nextInt(1, 3); // [1-2]
        int obstacleStartX, obstacleStartY;

        do {
            obstacleStartX = ThreadLocalRandom.current().nextInt(0, height);
            obstacleStartY = ThreadLocalRandom.current().nextInt(0, width);

            if (!(obstacleStartX == 0 && obstacleStartY == 0)) { // don't want to overwrite the charging station, try again
                floorPlan.floorLayout.get(obstacleStartX).get(obstacleStartY).surfaceType = SurfaceType.OBSTACLE;
                floorPlan.floorLayout.get(obstacleStartX).get(obstacleStartY).dirtAmount = 0;
                --numberOfObstacles;
            }
        } while (numberOfObstacles > 0);

        return floorPlan;
    }


    public static void twoRoomsFloorPlan(int firstRoomWidth, int firstRoomHeight, int secondRoomWidth, int secondRoomHeight) {
        FloorPlan firstRoom = FloorPlan.oneRoomFloorPlan(firstRoomWidth, firstRoomHeight);
        FloorPlan secondRoom = FloorPlan.oneRoomFloorPlan(secondRoomWidth, secondRoomHeight);

        int smallerHeight = Math.min(firstRoom.height, secondRoom.height);
        int connectorX = smallerHeight / 2;
        int connectorY = firstRoomWidth - 1;

        FloorCell firstRoomConnectorCell = firstRoom.floorLayout.get(connectorX).get(connectorY);
        firstRoomConnectorCell.wallsPresent.remove(Wall.EAST_WALL);

        // append
        for (int i = 0; i < firstRoom.height && i < secondRoom.height; ++i)
            firstRoom.floorLayout.get(i).addAll(secondRoom.floorLayout.get(i));

        if (secondRoom.height > firstRoom.height) {
            firstRoom.floorLayout.addAll(secondRoom.floorLayout.subList(firstRoomHeight, secondRoomHeight));

            for (int i = firstRoomHeight; i < secondRoomHeight; ++i)
                for (int j = 0; j < firstRoomWidth; ++j) {
                    SurfaceType surfaceType = (i == firstRoomHeight ? SurfaceType.UNDEFINED_BORDER : SurfaceType.UNDEFINED);
                    firstRoom.floorLayout.get(i).add(0, new FloorCell(surfaceType, 0, EnumSet.noneOf(Wall.class), i, j));
                }
        }
        else if (firstRoom.height > secondRoomHeight) {
            List<FloorCell> undefinedRegion = firstRoom.floorLayout.get(secondRoomHeight);

            for (int i = 0; i < secondRoomWidth; ++i)
                undefinedRegion.add(new FloorCell(SurfaceType.UNDEFINED_BORDER, 0, EnumSet.noneOf(Wall.class), secondRoomHeight, i));
        }

        FloorCell secondRoomConnectorCell = firstRoom.floorLayout.get(connectorX).get(connectorY + 1);
        secondRoomConnectorCell.wallsPresent.remove(Wall.WEST_WALL);

        firstRoom.height = Math.max(firstRoomHeight, secondRoomHeight);
        firstRoom.width = firstRoomWidth + secondRoomWidth;

        firstRoom.print(FloorPlan::printDirtAmount);
    }


    public static String printDirtAmount(FloorCell floorCell) {
        switch (floorCell.surfaceType) {
            case CHARGING_STATION:
                return "C";
            case OBSTACLE:
                return "X";
            case UNDEFINED:
                return " ";
            default:
                return Integer.toString(floorCell.dirtAmount);
        }
    }


    public void print(Function<FloorCell, String> cellToStrFn) {
        for (List<FloorCell> row : floorLayout) {
            for (FloorCell cell : row) {
                if (cell.wallsPresent.contains(Wall.NORTH_WALL))
                    System.out.print("___");
                else
                    System.out.print("   ");
            }

            System.out.println();

            for (FloorCell cell : row) {
                if (cell.wallsPresent.contains(Wall.WEST_WALL))
                    System.out.print("|");
                else if (cell.surfaceType == SurfaceType.UNDEFINED_BORDER)
                    System.out.print("-");
                else
                    System.out.print(" ");


                if (cell.surfaceType == SurfaceType.UNDEFINED_BORDER)
                    System.out.print("-");
                else if (cell.surfaceType == SurfaceType.UNDEFINED)
                    System.out.print(" ");
                else
                    System.out.print(cellToStrFn.apply(cell));


                if (cell.wallsPresent.contains(Wall.EAST_WALL))
                    System.out.print("|");
                else if (cell.surfaceType == SurfaceType.UNDEFINED_BORDER)
                    System.out.print("-");
                else
                    System.out.print(" ");
            }
        }

        List<FloorCell> lastRow = floorLayout.get(floorLayout.size() - 1); // last row

        System.out.println();

        for (FloorCell cell : lastRow) {
            if (cell.wallsPresent.contains(Wall.SOUTH_WALL))
                System.out.print("---");
            else
                System.out.print("   ");
        }

        System.out.println();
    }


    public static void printKey() {
        System.out.println("\nB - bare floor");
        System.out.println("L - low pile carpet");
        System.out.println("H - high pile carpet");
        System.out.println("O - obstacle");
        System.out.println("C - charging station");
    }
}

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class FloorPlan {
    public List<List<FloorCell>> floorLayout = new ArrayList<>();
    public int width, height;


    // a square FloorPlan for now:
    public FloorPlan(int width, int height) {
        this.width = width;
        this.height = height;

        for (int i = 0; i < height; ++i) {
            List<FloorCell> row = new ArrayList<>();

            for (int j = 0; j < width; ++j) {
                // randomized to either: BARE_FLOOR (0), LOW_PILE_CARPET (1), or HIGH_PILE_CARPET (2)
                SurfaceType surfaceType = SurfaceType.values()[ThreadLocalRandom.current().nextInt(0, 3)]; // random int: [0, 2]
                int dirtAmount = ThreadLocalRandom.current().nextInt(0, 6); // [0, 5]

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

            floorLayout.add(row);
        }
    }


    public void print() {
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
                else
                    System.out.print(" ");

                System.out.print(cell.dirtAmount);

                if (cell.wallsPresent.contains(Wall.EAST_WALL))
                    System.out.print("|");
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
}

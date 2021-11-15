import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int numRooms;
        FloorPlan floorPlan = null;
        List<Integer> widths = new ArrayList<>();
        List<Integer> heights = new ArrayList<>();

        User user = User.login();

        String[] roomsSize = user.roomsAndSizeCode.split("_");
        numRooms = Integer.parseInt(roomsSize[0]);
        for (int i = 1; i < roomsSize.length; i++) {
            int s = Integer.parseInt(roomsSize[i]);
            if (i % 2 == 0) heights.add(s);
            else widths.add(s);

        }




       /* do {
            System.out.print("1, 2, or 3 rooms? ");
            numRooms = new Scanner(System.in).nextInt();
        } while (!(numRooms == 1 || numRooms == 2 || numRooms == 3));



        int currentWidth = 0, currentHeight = 0;

        for (int i = 0; i < numRooms; ++i) {
            System.out.println(String.format("\nRoom %d", i + 1));

            do {
                System.out.print("   Width? ");
                currentWidth = new Scanner(System.in).nextInt();
            } while (currentWidth <= 1);

            widths.add(currentWidth);
            currentWidth = 0;

            do {
                System.out.print("   Height? ");
                currentHeight = new Scanner(System.in).nextInt();
            } while (currentHeight <= 1);

            heights.add(currentHeight);
            currentHeight = 0;
        }*/


        if (numRooms == 1) {
            floorPlan = FloorPlan.oneRoomFloorPlan(widths.get(0), heights.get(0));
        }
        else if (numRooms == 2) {
            floorPlan = FloorPlan.twoRoomsFloorPlan(widths.get(0), heights.get(0), widths.get(1), heights.get(1));
        }
        else if (numRooms == 3) {
            floorPlan = FloorPlan.threeRoomsFloorPlan(widths.get(0), heights.get(0), widths.get(1), heights.get(1), widths.get(2), heights.get(2));
        }


        FloorPlan.printKey();
        System.out.println("\nSurface types...");
        floorPlan.print((FloorCell cell) -> cell.surfaceType.toString());

        System.out.println("\nDirt amounts...");
        floorPlan.print(FloorPlan::printDirtAmount);
        System.out.println();



        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(floorPlan, startingLocation);
        //User aw = new User("awatkins", "Andrea", "Watkins", "60622", "672213");
        Logger logger = new Logger(user.username);
        CleanSweep cs = new CleanSweep(250.0, 0, sensor, floorPlan.floorLayout.get(0).get(0), floorPlan.floorLayout.get(0).get(0), logger);

        System.out.print("Seconds until start? ");
        int delay = new Scanner(System.in).nextInt();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(cs, delay, TimeUnit.SECONDS);
    }


}

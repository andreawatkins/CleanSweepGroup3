import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        FloorPlan twoRooms = FloorPlan.twoRoomsFloorPlan(3, 2, 4, 6);
//        twoRooms.print(FloorPlan::printDirtAmount);

        // FloorPlan threeRooms = FloorPlan.threeRoomsFloorPlan(3, 2, 4, 6, 2, 4);
        FloorPlan threeRooms = FloorPlan.threeRoomsFloorPlan(4, 5, 2, 3, 2, 8);

        threeRooms.print(FloorPlan::printDirtAmount);

        /*
        int height, width;

        do {
            System.out.print("Width? ");
            width = new Scanner(System.in).nextInt();
        } while (width <= 1);

        do {
            System.out.print("Height? ");
            height = new Scanner(System.in).nextInt();
        } while (height <= 1);

        FloorPlan oneRoom = FloorPlan.oneRoomFloorPlan(width, height);

        FloorPlan.printKey();
        System.out.println("\nSurface types...");
        oneRoom.print((FloorCell cell) -> cell.surfaceType.toString());

        System.out.println("\nDirt amounts...");
        oneRoom.print(FloorPlan::printDirtAmount);
        System.out.println();

        System.out.print("Ready to start? (press ENTER) ");
        new Scanner(System.in).nextLine();

        Location startingLocation = new Location(0, 0);
        SensorSimulator sensor = new SensorSimulator(oneRoom, startingLocation);

        CleanSweep cs = new CleanSweep(250.0, 0, sensor, oneRoom.floorLayout.get(0).get(0), oneRoom.floorLayout.get(0).get(0));
        cs.turnOn();
        System.out.println("\nDONE!");
         */
    }
}


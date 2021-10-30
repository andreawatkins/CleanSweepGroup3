public class FloorNode {
    FloorNode parent;
    Location onGrid;
    Direction howWeGotHere;

    public FloorNode(FloorNode parent, Location onGrid, Direction howWeGotHere) {
        this.parent = parent;
        this.onGrid = onGrid;
        this.howWeGotHere = howWeGotHere;
    }
}

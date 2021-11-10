import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private File file;

    public Logger(String username){
        file = new File("CleanSweepGroup3/data/"+username+"_cleanSweepLog.txt");

        try{
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write("");
            //this doesn't matter; just creates file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message){
        try{
            //FileWriter fileWriter = new FileWriter(file, true);
            //fileWriter.write(message+"\n");
            //fileWriter.close();
            //file = new File(username+"_cleanSweepLog.txt");
            //FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter (file, true));
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            printWriter.print(message+"\n");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logStartTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        log("Clean Sweep has started cleaning at " + date);
    }

    public void logEndTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        log("Clean Sweep finished cleaning at " + date + "\n\n");
    }

    public void logMoveNorth(){
        log("Clean Sweep moved North");
    }

    public void logMoveSouth(){
        log("Clean Sweep moved South");
    }

    public void logMoveWest(){
        log("Clean Sweep moved West");
    }

    public void logMoveEast(){
        log("Clean Sweep moved East");
    }

    public void logAtCapacity(){
        log("Clean Sweep is at dirt capacity");
    }

    public void logCurrentCapacity(double capacity){
        log("Clean Sweep's collected dirt level is: " + capacity);
    }

    public void logReturnToCharger(){
        log("Clean Sweep is returning to charger...");
    }

    public void logCharging(){
        log("Clean Sweep is charging...");
    }

    public void logLowBattery(){
        log("Clean Sweep battery is low");
    }

    public void logCurrentLocation(Location location){
        log("Clean Sweep is at cell: " + location.getX() + ", " + location.getY());
    }

    public void logBatteryLevel(double batteryLevel){
        log("Clean Sweep's battery level is: " + batteryLevel);
    }

    public void logDirtLevel(Location location, FloorCell floorCell){
        log("Dirt level at (" + location.x +", " + location.y +") is: " + floorCell.dirtAmount);
    }

    public void logBackTracking(){
        log("Clean Sweep is backtracking...");
    }

    public void logChargingStationFound(Location location){
        log("Charging station found at: (" + location.getX() + ", " + location.getY()+") !");
    }

    public void logCellHasBeenCleaned(Location location){
        log("Floor Cell at: (" + location.getX() + ", " + location.getY() + ") has been cleaned!");
    }

    public void logBinHasBeenEmptied(){
        log("Clean Sweep's bin has been emptied! Clean Sweep is ready to continue cleaning.");
    }

    public void logCleaningOutput(FloorCell floorCell){
        log("Clean Sweep is cleaning... Dirt remaining: " + floorCell.dirtAmount);
    }

    public void logChargedBattery(){
        log("Clean Sweep's battery has been fully charged!");
    }


}

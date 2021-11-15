import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public File file;


    public Logger(String username){
        file = new File("CleanSweepGroup3/data/" + username + "CleanSweepLog.txt");

        try{
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write("");
            //this doesn't matter; just creates file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message){
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

    public String generateDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        return date;
    }

    public void logStartTime(){
        generateDateTime();

        log("Clean Sweep has started cleaning at " + generateDateTime() + "\n\n");
    }

    public void logEndTime(){

        log("Clean Sweep finished cleaning at " + generateDateTime() + "\n\n");
    }

    public void logMoveNorth(){
        log("Clean Sweep moved North at "+ generateDateTime());
    }

    public void logMoveSouth(){
        log("Clean Sweep moved South at "+ generateDateTime());
    }

    public void logMoveWest(){
        log("Clean Sweep moved West at "+ generateDateTime());
    }

    public void logMoveEast(){
        log("Clean Sweep moved East at "+ generateDateTime());
    }

    public void logAtCapacity(){
        log("Clean Sweep is at dirt capacity "+ generateDateTime());
    }

    public void logCurrentCapacity(double currCapacity, double totalCapacity){
        log("Clean Sweep's collected dirt level is: " + String.format("%.1f", (currCapacity / totalCapacity) * 100) + "% full at "+ generateDateTime());
    }

    public void logReturnToCharger(){
        log("Clean Sweep is returning to charger...at ");
    }

    public void logCharging(){
        log("Clean Sweep is charging... at "+ generateDateTime());
    }

    public void logLowBattery(){
        log("Clean Sweep battery is low at "+ generateDateTime());
    }

    public void logCurrentLocation(Location location){
        log("Clean Sweep is at cell: " + location.getX() + ", " + location.getY());
    }

    public void logBatteryLevel(String batteryLevel){
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
        log("Clean Sweep's bin has been emptied! Clean Sweep is ready to continue cleaning. "+ generateDateTime());
    }

    public void logCleaningOutput(FloorCell floorCell){
        log("Clean Sweep is cleaning... Dirt remaining: " + floorCell.dirtAmount);
    }

    public void logChargedBattery(){
        log("Clean Sweep's battery has been fully charged!");
    }
}

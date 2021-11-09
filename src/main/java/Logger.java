import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private File file;

    public Logger(){
        //pass user email or UUID as param to make file name unique
        file = new File("cleanSweepLog.txt");
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message){
        try{
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(message+"\n");
            fileWriter.close();
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
        log("Clean Sweep finished cleaning at " + System.currentTimeMillis());
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
        log("Clean Sweep capacity is: " + capacity);
    }

    public void logReturnToCharger(){
        log("Clean Sweep is returning to charger");
    }

    public void logCharging(){
        log("Clean Sweep is charging...");
    }

    public void logLowBattery(){
        log("Clean Sweep battery is low");
    }

    public void logCurrentLocation(Location location){
        log("Clean Sweep is at: " + location.getX() + ", " + location.getY());
    }

    public void logBatteryLevel(double batteryLevel){
        log("Clean Sweep battery level is: " + batteryLevel);
    }

    public void logDirtLevel(Location location, FloorCell floorCell){
        log("Dirt level at: " + location.x +", " + location.y +" is: " + floorCell.dirtAmount);
    }

    public void logBackTracking(){
        log("Clean Sweep is backtracking...");
    }

    public void chargingStationFound(Location location){
        log("Charging station found at: " + location.getX() + ", " + location.getY());
    }

    public void cellHasBeenCleaned(Location location){
        log("Floor Cell at: " + location.getX() + ", " + location.getY() + " has been cleaned!");
    }


}

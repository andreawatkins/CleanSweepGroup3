import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;


public class User{
    public String username;
    public String firstName;
    public String lastName;
    public String location;
    public String cleanSweepId;

    public User(String username, String firstName, String lastName, String location, String csID){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.cleanSweepId = csID;

        createUserAccount(username, firstName, lastName, location, csID);
    }

    public User(String username, String firstName, String lastName, String location, String csID, boolean store){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.cleanSweepId = csID;

        if(store) createUserAccount(username, firstName, lastName, location, csID);
    }


    public static User userExists(String username){
        User results = null;
        File file = new File("data/Users.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains(username)) {
                    System.out.println("User already exists.");
                    String[] attrs = line.split(",");
                    results = new User(attrs[0], attrs[1], attrs[2], attrs[3], attrs[4],false);
                }
            }
        }
        catch(FileNotFoundException e) {
        }
        return results;
    }

    public static boolean createUserAccount(String username, String firstName, String lastName, String location, String csID){
        if (userExists(username) == null){
            try{
                FileWriter myWriter = new FileWriter("data/Users.txt", true);
                BufferedWriter bw = new BufferedWriter(myWriter);
                PrintWriter out = new PrintWriter(bw);
                String userInfo = username + "," + firstName + "," + lastName + "," + location + "," + csID;
                out.println(userInfo);
                out.close();
                return true;
            }
            catch (IOException e){
                return false;
            }

        }
        else{
            System.out.println("User already exists.");
            return false;
        }

    }
}

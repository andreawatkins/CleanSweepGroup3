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
    public static User user;
    public String roomsAndSizeCode;

    public User(String username, String firstName, String lastName, String location, String csID, String roomsAndSizeCode){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.cleanSweepId = csID;
        this.roomsAndSizeCode = roomsAndSizeCode;

        createUserAccount(username, firstName, lastName, location, csID, roomsAndSizeCode);
    }

    public User(String username, String firstName, String lastName, String location, String csID, String roomsAndSizeCode, boolean store){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.cleanSweepId = csID;
        this.roomsAndSizeCode = roomsAndSizeCode;

        if(store) createUserAccount(username, firstName, lastName, location, csID, roomsAndSizeCode);
    }


    public static User userExists(String username){
        User results = null;
        File file = new File("data/Users.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains(username)) {
                    String[] attrs = line.split(",");
                    results = new User(attrs[0], attrs[1], attrs[2], attrs[3], attrs[4], attrs[5],false);
                    System.out.println("User: " + attrs[0] + "," +  attrs[1] +"," +  attrs[2] +"," +  attrs[3] +"," +  attrs[4] +","+attrs[5]);
                }
            }
        }
        catch(FileNotFoundException e) {
        }
        return results;
    }

    public static boolean createUserAccount(String username, String firstName, String lastName, String location, String csID, String roomsAndSizeCode){
        if (userExists(username) == null){
            try{
                FileWriter myWriter = new FileWriter("data/Users.txt", true);
                BufferedWriter bw = new BufferedWriter(myWriter);
                PrintWriter out = new PrintWriter(bw);
                String userInfo = username + "," + firstName + "," + lastName + "," + location + "," + csID + "," + roomsAndSizeCode;
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

    public String getroomsAndSizeCode(String username){
        if(userExists (username)!=null) {
            return userExists(username).roomsAndSizeCode;
        }
        else return null;
    }

    public static User login(){
        System.out.print("Please enter your username to login: ");
        String username = new Scanner(System.in).next();
        if(userExists(username)==null){
            System.out.print("An account does not exist with that username, press 1 to create an account, q to quit: ");
            String input = new Scanner(System.in).next();
            if (input.equals("1")) {
                System.out.println("\nPlease enter the following information");
                System.out.print("\tFirst Name: ");
                String firstName = new Scanner(System.in).next();
                System.out.print("\tLast Name: ");
                String lastName = new Scanner(System.in).next();
                System.out.print("\tZIP Code: ");
                String zipcode = new Scanner(System.in).next();
                System.out.print("\tClean Sweep 6-digit identification number: ");
                String csID = new Scanner(System.in).next();
                System.out.print("\n1, 2, or 3 rooms? ");
                String rooms = new Scanner(System.in).next();
                StringBuilder rooms_sizes = new StringBuilder(rooms);
                for (int i = 0; i < Integer.parseInt(rooms); ++i) {
                    System.out.println(String.format("\nRoom %d", i + 1));
                    System.out.print("   Width? ");
                    String width = new Scanner(System.in).next();
                    System.out.print("   Height? ");
                    String height = new Scanner(System.in).next();
                    rooms_sizes.append("_").append(width).append("_").append(height);
                }

                createUserAccount(username, firstName,lastName,zipcode,csID, rooms_sizes.toString());

            }
            if(input.equals("q")) System.exit(0);


            }
        user = userExists(username);
        return user;
        }

    }



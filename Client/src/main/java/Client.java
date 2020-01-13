import Protos.Protocol.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable {
    private Socket cs;
    private SocketManager sm;
    private BufferedReader sin;

    private String type; // Producer, Consumer

    Client(Socket cs) throws IOException {
        this.cs = cs;
        this.sm = new SocketManager(this.cs);
        this.sin = new BufferedReader(new InputStreamReader(System.in));
        this.type = "";
    }

    // check if User exists
    private boolean authentication() throws IOException{
        System.out.print("\nAuthentication\nUsername: ");
        String username = this.sin.readLine();

        System.out.print("Password: ");
        String pass = this.sin.readLine();

        // Write the message to the SocketManager
        this.sm.write(
                // build message
                Message.newBuilder()
                        // Set message type
                        .setType(Type.LOGIN)
                        // Set user credentials
                        .setUser(
                                User.newBuilder()
                                        .setUsername(username)
                                        .setPassword(pass)
                        ).build()
        );

        // Wait for the response message
        Message res = this.sm.read();
        if (res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult() &&
                res.hasUserType()
        ) {
            this.type = res.getUserType();
            return true;
        }
        return false;
    }

    private boolean registration() throws IOException{
        System.out.print("\nRegistration\nUsername: ");
        String username = this.sin.readLine();

        System.out.print("Password: ");
        String pass = this.sin.readLine();

        System.out.println("Select the option by typing its number.");
        System.out.print("Type 1: Producer, 2: Consumer: ");
        String clientType = this.sin.readLine();
        while(!clientType.equals("1") && !clientType.equals("2")){
            System.out.println("Please choose a valid option.");
            System.out.print("Type 1: Manufacturer, 2: Importer: ");
            clientType = this.sin.readLine();
        }

        switch (clientType){
            case "1": clientType = "MANUFACTURER"; break;
            case "2": clientType = "IMPORTER"; break;
        }

        this.sm.write(
                Message.newBuilder()
                        .setType(Type.REGISTER)
                        .setUserType(clientType)
                        .setUser(
                                User.newBuilder()
                                        .setUsername(username)
                                        .setPassword(pass)
                        ).build()
        );

        Message res = this.sm.read();
        return res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult();
    }

    private boolean initialMenu() throws IOException {
        while(true) {
            System.out.println("\n1) Authentication \n2) Registration \n3) Exit");
            System.out.print("-> ");

            switch (this.sin.readLine()){
                case "1":
                    if (!authentication())
                        System.out.println("\nWRONG CREDENTIALS. TRY AGAIN.\n");
                    else {
                        System.out.println("\nACCESS GRANTED.\n");
                        return true;
                    }
                    break;

                case "2":
                    if (!registration())
                        System.out.println("\nREGISTRATION ERROR.\n");
                    else System.out.println("\nREGISTRATION SUCCESSFULLY DONE.\n");
                    break;

                case "3":
                    return false;

                default:
                    System.out.println("\nOPTION NOT VALID.\n");
                    break;
            }
        }
    }

    public void run(){
        try {
            while(true) {
                try {
                    if (!initialMenu()) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // do stuff
                System.out.println("Passei o menu inicial");
            }

        } finally {
            try {
                this.cs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

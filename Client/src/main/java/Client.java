import Protos.Protocol.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.zeromq.ZMQ;


class NotificationHandler implements Runnable {
    private ZMQ.Socket sub;

    NotificationHandler(ZMQ.Socket sub) {
        this.sub = sub;
    }

    @Override
    public void run() {
        String msg;
        while (true) {
            byte[] b = sub.recv();
            msg = new String(b);
            System.out.println("NEW NOTIFICATION: " +msg);
        }
    }
}

public class Client implements Runnable {
    private Socket cs;
    private SocketManager sm;
    private BufferedReader sin;

    private String type; // Producer, Consumer
    private String username;
    private boolean notifications;

    private ZMQ.Socket sub;
    private Thread notificationsHandler;

    Client(Socket cs, int subPort) throws IOException {
        this.cs = cs;
        this.sm = new SocketManager(this.cs);
        this.sin = new BufferedReader(new InputStreamReader(System.in));

        this.type = "";
        this.username = "";
        this.notifications = true;

        ZMQ.Context context = ZMQ.context(1);
        sub = context.socket(ZMQ.SUB);
        sub.connect("tcp://localhost:"+subPort);
        notificationsHandler = new Thread(new NotificationHandler(sub));
        notificationsHandler.start();
    }

    // INITIAL MENU INTERFACE WITH OPTIONS
    private boolean initialMenu() throws IOException {
        System.out.println("------------ NeFIT ------------");
        while (true) {
            System.out.println("\n1) Authentication \n2) Registration \n3) Exit");
            System.out.print("-> ");

            switch (this.sin.readLine()) {
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
                    else {
                        System.out.println("\nREGISTRATION SUCCESSFULLY DONE.\n");
                        return true;
                    }
                    break;

                case "3":
                    return false;

                default:
                    System.out.println("\nOPTION NOT VALID.\n");
                    break;
            }
        }
    }

    // LOGIN MENU INTERFACE
    private boolean authentication() throws IOException {
        System.out.print("\nAuthentication\nUsername: ");
        String username = this.sin.readLine();

        System.out.print("Password: ");
        String pass = this.sin.readLine();

        Message msg = Message.newBuilder()
                // Set message type
                .setType(Type.LOGIN)
                // Set user credentials
                .setUser(
                        User.newBuilder()
                                .setUsername(username)
                                .setPassword(pass)
                                .build()
                ).build();

        // Write the message to the SocketManager
        this.sm.write(msg);

        // Wait for the response message
        Message res = this.sm.getMessage();
        if (res != null &&
                res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult() &&
                res.hasUserType()
        ) {
            this.type = res.getUserType();
            this.username = username;
            return true;
        } else if (res != null && res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    // REGISTER MENU INTERFACE
    private boolean registration() throws IOException {
        System.out.print("\nRegistration\nUsername: ");
        String username = this.sin.readLine();

        System.out.print("Password: ");
        String pass = this.sin.readLine();

        System.out.println("Select the option by typing its number.");
        System.out.print("Type 1: Manufacturer, 2: Importer: ");
        String clientType = this.sin.readLine();
        while (!clientType.equals("1") && !clientType.equals("2")) {
            System.out.println("Please choose a valid option.");
            System.out.print("Type 1: Manufacturer, 2: Importer: ");
            clientType = this.sin.readLine();
        }

        switch (clientType) {
            case "1":
                clientType = "MANUFACTURER";
                break;
            case "2":
                clientType = "IMPORTER";
                break;
        }

        Message msg = Message.newBuilder()
                .setType(Type.REGISTER)
                .setUserType(clientType)
                .setUser(
                        User.newBuilder()
                                .setUsername(username)
                                .setPassword(pass)
                                .build()
                ).build();

        this.sm.write(msg);

        Message res = this.sm.getMessage();
        if (res != null &&
                res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult()
        ) {
            Message login = Message.newBuilder()
                    // Set message type
                    .setType(Type.LOGIN)
                    // Set user credentials
                    .setUser(
                            User.newBuilder()
                                    .setUsername(username)
                                    .setPassword(pass)
                                    .build()
                    ).build();

            // Write the message to the SocketManager
            this.sm.write(login);

            // Wait for the response message
            Message loginRes = this.sm.getMessage();
            if (loginRes != null &&
                    loginRes.hasType() &&
                    loginRes.getType().equals(Type.RESPONSE) &&
                    loginRes.hasState() &&
                    loginRes.getState().getResult() &&
                    loginRes.hasUserType()
            ) {
                this.type = loginRes.getUserType();
                this.username = username;
                return true;
            } else if (loginRes != null && loginRes.hasState() && loginRes.getState().hasDescription()) {
                System.out.println(loginRes.getState().getDescription());
            }
        } else if (res != null && res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    // IMPORTER MENU INTERFACE
    private boolean importerMenu() throws IOException {
        while (true) {
            System.out.println("\n1) New order \n2) Subscribe manufacturer");
            System.out.println("3) Unsubscribe manufacturer \n4) Toggle order notifications \n5) Logout");
            System.out.print("-> ");

            switch (this.sin.readLine()) {
                case "1":
                    if (!newOrder())
                        System.out.println("\nSOMETHING WENT WRONG WITH YOUR ORDER\n");
                    else {
                        System.out.println("\nORDER SUCCESSFULLY POSTED.\n");
                        return true;
                    }
                    break;

                case "2":
                    if (!subscribe())
                        System.out.println("\nSOMETHING WENT WRONG WITH YOUR SUBSCRIPTION\n");
                    else {
                        System.out.println("\nSUBSCRIPTION SUCCESSFUL.\n");
                        return true;
                    }
                    break;

                case "3":
                    if (!unsubscribe())
                        System.out.println("\nSOMETHING WENT WRONG WITH YOUR SUBSCRIPTION'S CANCELLING\n");
                    else {
                        System.out.println("\nSUBSCRIPTION'S CANCELLING SUCCESSFUL.\n");
                        return true;
                    }
                    break;

                case "4":
                    if (!notifications(!this.notifications))
                        System.out.println("\nSOMETHING WENT WRONG WHILE TOGGLING NOTIFICATIONS\n");
                    else {
                        System.out.println("\nNOTIFICATIONS TOGGLING SUCCESSFUL.\n");
                        return true;
                    }
                    break;

                case "5":
                    if (!logout())
                        System.out.println("\nLOGOUT UNSUCCESSFUL\n");
                    else {
                        System.out.println("\nLOGOUT SUCCESSFUL\n");
                        return false;
                    }
                    break;

                default:
                    System.out.println("\nPLEASE SELECT A VALID OPTION.\n");
                    break;
            }
        }
    }

    // MANUFACTURER MENU INTERFACE
    private boolean manufacturerMenu() throws IOException {
        System.out.println("Welcome back " + username + "! You logged in as a manufacturer. Notifications are on!");
        while (true) {
            System.out.println("\n1) New offer \n2) Logout");
            System.out.print("-> ");

            switch (this.sin.readLine()) {
                case "1":
                    if (!newOffer())
                        System.out.println("\nSOMETHING WENT WRONG WITH YOUR OFFER\n");
                    else {
                        System.out.println("\nOFFER SUCCESSFULLY PUBLISHED.\n");
                        return true;
                    }
                    break;

                case "2":
                    if (!logout())
                        System.out.println("\nLOGOUT UNSUCCESSFUL\n");
                    else {
                        System.out.println("\nLOGOUT SUCCESSFUL\n");
                        return false;
                    }
                    break;

                default:
                    System.out.println("\nPLEASE SELECT A VALID OPTION.\n");
                    break;
            }
        }
    }

    private boolean newOffer() throws IOException {
        System.out.print("\nNew Offer\nProduct name: ");
        String name = this.sin.readLine();
        System.out.print("Minimum production amount: ");
        String min = this.sin.readLine();
        System.out.print("Maximum production amount: ");
        String max = this.sin.readLine();
        System.out.print("Minimum unit price: ");
        String price = this.sin.readLine();
        System.out.print("Order placement period: ");
        String period = this.sin.readLine();

        ItemProductionOffer offer = ItemProductionOffer.newBuilder()
                .setName(name)
                .setMinimumAmount(Integer.parseInt(min))
                .setMaximumAmount(Integer.parseInt(max))
                .setUnitPrice(Float.parseFloat(price))
                .setPeriod(Long.parseLong(period))
                .build();

        Message msg = Message.newBuilder()
                .setItemProductionOffer(offer)
                .setType(Type.ITEMPRODUCTIONOFFER)
                .build();

        this.sm.write(msg);

        Message res = this.sm.getMessage();
        if (res != null &&
                res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult()) {
            return true;
        } else if (res != null && res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    private boolean subscribe() throws IOException {
        System.out.println("Subscribe a manufacturer's orders:");

        System.out.print("Manufacturer name: ");
        String name = this.sin.readLine();

        sub.subscribe(name);
        return true;
    }

    private boolean unsubscribe() throws IOException {
        System.out.println("Unsubscribe a manufacturer's orders:");

        System.out.print("Manufacturer name: ");
        String name = this.sin.readLine();

        sub.unsubscribe(name);
        return true;
    }

    private boolean notifications(boolean value) {
        if (value) {
            System.out.println("Turning on the notifications!");
            notificationsHandler.start();
        } else {
            System.out.println("Turning off the notifications!");
            notificationsHandler.interrupt();
        }

        this.notifications = value;
        return true;
    }

    private boolean newOrder() throws IOException {
        System.out.print("\nNew Offer\nManufacturer name: ");
        String name = this.sin.readLine();
        System.out.print("Product name: ");
        String product = this.sin.readLine();
        System.out.print("Quantity: ");
        String quantity = this.sin.readLine();
        System.out.print("Unit price: ");
        String price = this.sin.readLine();

        ItemOrderOffer order = ItemOrderOffer.newBuilder()
                .setManufacturerName(name)
                .setProductName(product)
                .setQuantity(Float.parseFloat(quantity))
                .setUnitPrice(Float.parseFloat(price))
                .build();

        Message msg = Message.newBuilder()
                .setItemOrderOffer(order)
                .setType(Type.ITEMORDEROFFER)
                .build();

        this.sm.write(msg);

        Message res = this.sm.getMessage();
        if (res != null &&
                res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult()) {
            return true;
        } else if (res != null && res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    private boolean logout() throws IOException {
        Message msg = Message.newBuilder()
                .setType(Type.LOGOUT)
                .build();

        this.sm.write(msg);

        Message res = this.sm.getMessage();
        if (res != null &&
                res.hasType() &&
                res.getType().equals(Type.RESPONSE) &&
                res.hasState() &&
                res.getState().getResult()) {
            return true;
        } else if (res != null && res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    // RUN THE CLIENT
    public void run() {
        try {
            while (true) {
                try {
                    if (!initialMenu()) break;

                    if (type.equals("MANUFACTURER")) {
                        while (true) {
                            if (!manufacturerMenu()) break;
                        }
                    } else if (type.equals("IMPORTER")) {
                        while (true) {
                            if (!importerMenu()) break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

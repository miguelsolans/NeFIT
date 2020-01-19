import Protos.Protocol.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.ZMQ;
import java.util.*;

public class Client implements Runnable {
    private Socket cs;
    private SocketManager sm;
    private BufferedReader sin;

    private String type; // Producer, Consumer
    private String username;
    private boolean notifications;
    private NotificationHandler handler;
    private SubscriptionHandler subHandler;

    private ZMQ.Socket sub;

    private Queue<Message> responses = new ArrayDeque<Message>();

    Client(Socket cs, int subPort) throws IOException {
        this.cs = cs;
        this.sm = new SocketManager(this.cs);
        this.sin = new BufferedReader(new InputStreamReader(System.in));

        this.type = "";
        this.username = "";
        this.notifications = true;
        this.handler = new NotificationHandler(sm, responses, notifications);


        ZMQ.Context context = ZMQ.context(1);
        sub = context.socket(ZMQ.SUB);
        sub.connect("tcp://localhost:"+subPort);

        this.subHandler = new SubscriptionHandler(sub);

        Thread subscriptionsHandler = new Thread(subHandler);
        Thread notificationsHandler = new Thread(handler);

        subscriptionsHandler.start();
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
        Message res;
        while((res = responses.poll()) == null);
        if ( res.hasState() &&
                res.getState().getResult() &&
                res.hasUserType()
        ) {
            this.type = res.getUserType();
            this.username = username;
            return true;
        } else if (res.hasState() && res.getState().hasDescription()) {
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

        Message res;
        while((res = responses.poll()) == null);
        if ( res.hasState() &&
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
            Message loginRes;
            while((loginRes = responses.poll()) == null);
            if ( loginRes.hasState() &&
                    loginRes.getState().getResult() &&
                    loginRes.hasUserType()
            ) {
                this.type = loginRes.getUserType();
                this.username = username;
                return true;
            } else if (loginRes.hasState() && loginRes.getState().hasDescription()) {
                System.out.println(loginRes.getState().getDescription());
                System.out.println("Please try to login again.");
                return false;
            }
        } else if (res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    // IMPORTER MENU INTERFACE
    private boolean importerMenu() throws IOException {
        System.out.println("What do you want to do, " + username + "? \nNotifications are on!");
        while (true) {
            System.out.println("\n1) New order \n2) Subscribe manufacturer");
            System.out.println("3) Unsubscribe manufacturer \n4) Toggle order notifications");
            System.out.println("5) List active offers \n 6) Show transaction history \n7) Logout");
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
                    try {
                        listOffers();
                        return true;
                    } catch (Exception e) {
                        System.out.println("ERROR FETCHING THE OFFERS: "+e.getMessage());
                        return false;
                    }

                case "6":
                    try {
                        listHistory();
                        return true;
                    } catch (Exception e) {
                        System.out.println("ERROR FETCHING THE HISTORY: "+e.getMessage());
                        return false;
                    }

                case "7":
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
        System.out.println("What do you want to do, " + username + "? \nNotifications are on!");
        while (true) {
            System.out.println("\n1) New offer \n2) Toggle order notifications");
            System.out.println("3) List active offers \n 4) Show transaction history \n5) Logout");
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
                    if (!notifications(!this.notifications))
                        System.out.println("\nSOMETHING WENT WRONG WHILE TOGGLING NOTIFICATIONS\n");
                    else {
                        System.out.println("\nNOTIFICATIONS TOGGLING SUCCESSFUL.\n");
                        return true;
                    }
                    break;

                case "3":
                    try {
                        listOffers();
                        return true;
                    } catch (Exception e) {
                        System.out.println("ERROR FETCHING THE OFFERS: "+e.getMessage());
                        return false;
                    }

                case "4":
                    try {
                        listHistory();
                        return true;
                    } catch (Exception e) {
                        System.out.println("ERROR FETCHING THE HISTORY: "+e.getMessage());
                        return false;
                    }

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

        User u = User.newBuilder()
                .setUsername(this.username)
                .build();

        Message msg = Message.newBuilder()
                .setUserType(this.type)
                .setItemProductionOffer(offer)
                .setType(Type.ITEMPRODUCTIONOFFER)
                .setUser(u)
                .build();

        this.sm.write(msg);

        Message res;
        while((res = responses.poll()) == null);
        if ( res.hasState() &&
                res.getState().getResult()) {
            return true;
        } else if (res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    private boolean subscribe() throws IOException {
        System.out.println("Subscribe a manufacturer's orders:");

        System.out.print("Manufacturer name: ");
        String name = this.sin.readLine();

        sub.subscribe(name.getBytes());
        return true;
    }

    private boolean unsubscribe() throws IOException {
        System.out.println("Unsubscribe a manufacturer's orders:");

        System.out.print("Manufacturer name: ");
        String name = this.sin.readLine();

        sub.unsubscribe(name.getBytes());
        return true;
    }

    private boolean notifications(boolean value) {
        if (value) {
            System.out.println("Turning on the notifications!");
            handler.setOn(true);
            subHandler.setOn(true);
        } else {
            System.out.println("Turning off the notifications!");
            handler.setOn(false);
            subHandler.setOn(false);
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

        User u = User.newBuilder()
                .setUsername(this.username)
                .build();

        Message msg = Message.newBuilder()
                .setUserType(this.type)
                .setItemOrderOffer(order)
                .setType(Type.ITEMORDEROFFER)
                .setUser(u)
                .build();

        this.sm.write(msg);

        Message res;
        while((res = responses.poll()) == null);
        if ( res.hasState() &&
                res.getState().getResult()) {
            return true;
        } else if (res.hasState() && res.getState().hasDescription()) {
            System.out.println(res.getState().getDescription());
        }
        return false;
    }

    private static void parseJson(String jsonStr){
        JSONArray jsonarray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            System.out.println(jsonobject.toString());
        }
    }

    private static void getRequest(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int response = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder reply = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            reply.append(inputLine);
        }
        in.close();
        parseJson(reply.toString());
    }

    private static void listOffers() throws Exception {
        String path = "http://localhost:8080/negotiation/active";
        System.out.println("\nList of active offers:");
        getRequest(path);
    }


    private static void listHistory() throws Exception {
        String path = "http://localhost:8080/negotiation/";
        System.out.println("\nHistory of offers:");
        getRequest(path);
    }

    private boolean logout() throws IOException {
        Message msg = Message.newBuilder()
                .setUserType(this.type)
                .setType(Type.LOGOUT)
                .build();

        this.sm.write(msg);

        Message res;
        while((res = responses.poll()) == null);
        if ( res.hasState() &&
                res.getState().getResult()) {
            return true;
        } else if (res.hasState() && res.getState().hasDescription()) {
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

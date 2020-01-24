import java.net.Socket;


public class Main {
    public static void main(String[] args) throws Exception {
        Socket cs = new Socket("127.0.0.1", 9999);
        int subPort = 6002; // modificar para porta de subscrições dps do ZeroMQ

        Thread mt = new Thread(new Client(cs,subPort));
        mt.start();

        try{
            mt.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

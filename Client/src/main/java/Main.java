import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException, UnknownHostException {
        Socket cs = new Socket("127.0.0.1", 9999);
        int subPort = 1234; // modificar para porta de subscrições dps do ZeroMQ

        Thread mt = new Thread(new Client(cs,subPort));
        mt.start();

        try{
            mt.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

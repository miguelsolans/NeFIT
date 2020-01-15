import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException, UnknownHostException {
        Socket cs = new Socket("127.0.0.1", 9999);

        Thread mt = new Thread(new Client(cs));
        mt.start();

        try{
            mt.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

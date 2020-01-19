import org.zeromq.ZMQ;

public class SubscriptionHandler implements Runnable {
    private ZMQ.Socket sub;

    SubscriptionHandler(ZMQ.Socket sub) {
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
import org.zeromq.ZMQ;

public class SubscriptionHandler implements Runnable {
    private ZMQ.Socket sub;
    private boolean on;

    SubscriptionHandler(ZMQ.Socket sub) {
        this.sub = sub;
    }

    void setOn(boolean value) {
        on = value;
    }
  
    @Override
    public void run() {
        String msg;
        while (true) {
            byte[] b = sub.recv();
            msg = new String(b);
            if (on) System.out.println("NEW NOTIFICATION: " +msg);
        }
    }
}
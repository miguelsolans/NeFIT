import java.util.Queue;
import Protos.Protocol.*;

public class NotificationHandler implements Runnable {
    private SocketManager sm;
    private Queue<Message> responses;
    private boolean on;
    private boolean active = true;

    NotificationHandler(SocketManager sm, Queue<Message> responses, boolean notifications) {
        this.sm = sm;
        this.responses = responses;
        this.on = notifications;
    }

    void setOn(boolean value) {
        on = value;
    }

    void shutdown(){this.active = false;}

    @Override
    public void run() {
        while(this.active){
            Message msg = sm.getMessage();

            if (on && msg != null &&
                    msg.hasType() &&
                    msg.getType().equals(Type.NOTIFICATION))
                System.out.println("\nNOTIFICATION:\n" + msg.getSale().toString());

            else if (msg != null && msg.hasType() && msg.getType().equals(Type.RESPONSE))
                responses.add(msg);
            else if(msg == null)
                shutdown();
        }
    }
}

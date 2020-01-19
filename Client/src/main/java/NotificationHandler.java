import java.util.Queue;
import Protos.Protocol.*;

public class NotificationHandler implements Runnable {
    private SocketManager sm;
    private Queue<Message> responses;
    private boolean on;

    NotificationHandler(SocketManager sm, Queue<Message> responses, boolean notifications) {
        this.sm = sm;
        this.responses = responses;
        this.on = notifications;
    }

    void setOn(boolean value) {
        on = value;
    }

    @Override
    public void run() {
        while(true){
            Message msg = sm.getMessage();

            if (on && msg != null &&
                    msg.hasType() &&
                    msg.getType().equals(Type.NOTIFICATION) &&
                    msg.hasState() && msg.getState().getDescription())
                System.out.println("NOTIFICATION: "+ msg.getState().getDescription());

            else if (msg != null && msg.hasType() && msg.getType().equals(Type.RESPONSE))
                responses.add(msg);
        }
    }
}

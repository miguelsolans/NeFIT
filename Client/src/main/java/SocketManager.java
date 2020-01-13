import Protos.Protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class SocketManager {
    private Socket cs;
    private OutputStream os;
    private InputStream in;

    SocketManager(Socket cs) throws IOException {
        this.cs = cs;
        this.os = cs.getOutputStream();
        this.in = cs.getInputStream();
    }

    synchronized void write(Message msg) throws IOException {
        byte[] msgByteArr = msg.toByteArray();

        // send msg size
        this.os.write(msgByteArr);
    }

    Message read() {
        try {
            byte[] len = new byte[4096];
            int count = 0;
            count = in.read(len);
            byte[] temp = new byte[count];
            System.arraycopy(len, 0, temp, 0, count);
            return Message.parseFrom(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

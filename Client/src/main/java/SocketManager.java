import Protos.Protocol;
import Protos.Protocol.*;
import com.google.protobuf.InvalidProtocolBufferException;

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

    byte[] read(){
        try {
            byte len[] = new byte[4096];
            int count = 0;
            count = this.in.read(len);

            if (count < 0){
                System.out.println("Server failed.");
                return null;
            }

            byte[] temp = new byte[count];
            for (int i = 0; i < count; i++) {
                temp[i] = len[i];
            }
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Message getMessage(){

        Message message = null;
        try {
            byte[] bytes = read();

            if (bytes != null) {
                message = Message.parseFrom(bytes);
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return message;
    }

}

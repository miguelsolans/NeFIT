package teste;

import Protos.Protocol;
import org.zeromq.ZMQ;

public class TestConsumer3 {

    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket push = context.socket(ZMQ.PUSH);
        push.connect("tcp://localhost:12345");

        Protocol.User user = Protocol.User.newBuilder().
                setUsername("Armindo").
                build();



        Protocol.ItemOrderOffer itemOrderOffer = Protocol.ItemOrderOffer.newBuilder().
                                                setQuantity(20).
                                                setUnitPrice((float)1.5).
                                                setManufacturerName("Tifany").
                                                setProductName("Bananas").
                                                build();
        Protocol.Message message = Protocol.Message.newBuilder().
                                    setUser(user).
                                    setUserType("Consumer").
                                    setItemOrderOffer(itemOrderOffer).
                                    setType(Protocol.Type.REGISTER).
                                    build();

        push.send(message.toByteArray());

        //Notificação
     //   byte[] b = pull.recv();
     //   System.out.println(new String(b));
    }
}

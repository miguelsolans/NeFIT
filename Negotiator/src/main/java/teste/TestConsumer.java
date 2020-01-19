package teste;

import org.zeromq.ZMQ;
import Protos.Protocol;

public class TestConsumer {

    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket push = context.socket(ZMQ.PUSH);

        push.connect("tcp://localhost:12345");

        Protocol.User user = Protocol.User.newBuilder().
                setUsername("miguelsolans").
                build();

        Protocol.ItemOrderOffer itemOrderOffer = Protocol.ItemOrderOffer.newBuilder().
                                                setQuantity(15).
                                                setUnitPrice((float)2.5).
                                                setManufacturerName("Peugeot").
                                                setProductName("3008").
                                                build();
        Protocol.Message message = Protocol.Message.newBuilder().
                                    setUser(user).
                                    setUserType("IMPORTER").
                                    setItemOrderOffer(itemOrderOffer).
                                    setType(Protocol.Type.REGISTER).
                                    build();

        push.send(message.toByteArray());
    }
}

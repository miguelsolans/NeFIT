package teste;

import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;
import Protos.Protocol;

public class TestProducer {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket push = context.socket(ZMQ.PUSH);

        push.connect("tcp://localhost:12345");

        ZMQ.Socket pull = context.socket(ZMQ.PULL);
        pull.bind("tcp://*:3000");

        Protocol.User user = Protocol.User.newBuilder().
                                            setUsername("Peugeot").
                                            build();


        Protocol.ItemProductionOffer productionOffer = Protocol.ItemProductionOffer.newBuilder().
                                                                setName("3008").
                                                                setPeriod(30000).
                                                                setMaximumAmount(30).
                                                                setMinimumAmount(5).
                                                                setUnitPrice((float)0.9).
                                                                build();
        Protocol.Message message = Protocol.Message.newBuilder().
                                            setUser(user).
                                            setUserType("MANUFACTURER").
                                            setItemProductionOffer(productionOffer).
                                            setType(Protocol.Type.REGISTER).
                                            build();

        push.send(message.toByteArray());

        while(true) {
            //Notification
            byte[] b = pull.recv();
            Protocol.Message message1 = Protocol.Message.parseFrom(b);
            System.out.println(message1);
        }
    }


}

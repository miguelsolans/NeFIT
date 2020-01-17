package teste;

import org.zeromq.ZMQ;

import Protos.Protocol;

public class TestConsumer {

    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket push = context.socket(ZMQ.PUSH);
        push.connect("tcp://localhost:12346");



        Protocol.ItemOrderOffer itemOrderOffer = Protocol.ItemOrderOffer.newBuilder().
                                                setQuantity(15).
                                                setUnitPrice((float)2.5).
                                                setManufactureName("Tifany").
                                                setProductName("Bananas").
                                                build();
        Protocol.Message message = Protocol.Message.newBuilder().
                                    setUserType("Consumer").
                                    setItemOrderOffer(itemOrderOffer).
                                    setType(Protocol.Type.REGISTER).
                                    build();

        push.send(message.toByteArray());
    }
}

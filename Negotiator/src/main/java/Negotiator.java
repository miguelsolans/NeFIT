import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;

import Protos.Protocol;


public class Negotiator {
    private ProductionOffers productionMap; // key : fabricantName; Value: (key:Artigo;value:production
    private ZMQ.Socket push;
    private ZMQ.Socket pull;
    private ZMQ.Socket pub;
    private String port;
    private int id;

    public Negotiator(ZMQ.Socket push, ZMQ.Socket pull, ZMQ.Socket pub, String port, int id) {
        this.push = push;
        this.pull = pull;
        this.pub = pub;
        this.port = port;
        this.id = id;
        this.productionMap = new ProductionOffers();
    }

    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(1);


        String portPUSH = "12345", portPULL="12346",portPUB="12347";

        if(args.length==3){
            portPUSH = args[0];
            portPULL = args[1];
            portPUB = args[2];
        }
        //Sender side
        ZMQ.Socket push = context.socket(ZMQ.PUSH);
        push.connect("tcp://localhost:"+portPUSH);

        //Receiving part
        ZMQ.Socket pull = context.socket(ZMQ.PULL);
        pull.bind("tcp://*:"+portPULL);

        //Sender part
        ZMQ.Socket pub = context.socket(ZMQ.PUB);
        pub.bind("tcp://*:"+portPUB);


        Negotiator negotiator = new Negotiator(push,pull,pub, portPULL,1);

        while(true){
            //Receiving message from server front-end
            Protocol.Message message = negotiator.receive();

            switch (message.getUserType()){

                case "Producer":
                    negotiator.addProducionOffer(message,push);
                    break;
                case "Consumer":
                    negotiator.addOffer(message);
                    break;
            }
        }

    }


    private  Protocol.Message receive(){
        try {
            byte[] received = this.pull.recv();
            Protocol.Message message = null;
            message = Protocol.Message.parseFrom(received);
            return message;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    //synchronized?
    private boolean addOffer(Protocol.Message message){
        Protocol.ItemOrderOffer itemOrderOffer = message.getItemOrderOffer();
        Offer offer = new Offer(itemOrderOffer.getManufactureName(),itemOrderOffer.getProductName(),itemOrderOffer.getQuantity(),itemOrderOffer.getUnitPrice(),true);
        return (this.productionMap.insertItemOrderOffer(offer));
    }

    //synchronized?
    private boolean addProducionOffer(Protocol.Message message,ZMQ.Socket socket){
        Protocol.ItemProductionOffer itemProductionOffer = message.getItemProductionOffer();
        ProductionOffer productionOffer = new ProductionOffer(itemProductionOffer.getManufacturer(),itemProductionOffer.getArticleName(),itemProductionOffer.getMinimumAmount(),itemProductionOffer.getMaximumAmount(),itemProductionOffer.getUnitPrice(),itemProductionOffer.getPeriod(),true,socket);
       return (this.productionMap.insertProductionOffert(productionOffer));
    }


    
}

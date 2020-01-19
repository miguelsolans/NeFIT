import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;
import Protos.Protocol;

public class Negotiator {
    private ProductionOffers productionMap; // key : fabricantName; Value: (key:Artigo;value:production
    private ZMQ.Socket push;
    private ZMQ.Socket pull;


    public Negotiator(ZMQ.Socket push, ZMQ.Socket pull) {
        this.push = push;
        this.pull = pull;
        this.productionMap = new ProductionOffers();
    }

    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(1);
        Sender sender = new Sender();

        String portPUSH = "3000", portPULL="12345";


        if(args.length==1){ portPULL = args[0]; }
        //Sender side
        ZMQ.Socket push = context.socket(ZMQ.PUSH);
        push.connect("tcp://localhost:"+portPUSH);

        //Receiving part
        ZMQ.Socket pull = context.socket(ZMQ.PULL);
        pull.bind("tcp://*:"+portPULL);

        Negotiator negotiator = new Negotiator(push,pull);

        while(true){
            //Receiving message from server front-end
            Protocol.Message message = negotiator.receive();
            boolean result = true;

            switch (message.getUserType()){

                case "Producer":
                    result =negotiator.addProducionOffer(message,push);
                    if (result) {
                        sender.sendProductionOffer(message.getUser().getUsername(),message.getItemProductionOffer().getArticleName(),
                                message.getItemProductionOffer().getUnitPrice(),
                                message.getItemProductionOffer().getMinimumAmount(),
                                message.getItemProductionOffer().getMaximumAmount(),(int) message.getItemProductionOffer().getPeriod());
                    }
                    break;
                case "Consumer":
                    result = negotiator.addOffer(message);
                    if (result){
                        sender.sendItemOrderOffer(message.getUser().getUsername(),message.getItemOrderOffer().getManufactureName(),
                                                    message.getItemOrderOffer().getProductName(),message.getItemOrderOffer().getQuantity(),
                                                    message.getItemOrderOffer().getUnitPrice());
                    }
                    break;
                case "Consumer":
                    result = negotiator.addOffer(message);
                    break;
            }
            //Send message with the result of the offer (true or false)
            Protocol.User user = Protocol.User.newBuilder().
                                            setUsername(message.getUser().getUsername()).
                                            build();
            Protocol.Sale sale = Protocol.Sale.newBuilder().
                                            setMessage(Boolean.toString(result)).
                                            build();
            Protocol.Message messageS = Protocol.Message.newBuilder().
                                            setUserType(message.getUserType()).
                                            setType(Protocol.Type.RESPONSE).
                                            setUser(user).
                                            setSale(sale).
                                            build();
            push.send(messageS.toByteArray());
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


    private boolean addOffer(Protocol.Message message){
        Protocol.User user = message.getUser();
        Protocol.ItemOrderOffer itemOrderOffer = message.getItemOrderOffer();
        Offer offer = new Offer(itemOrderOffer.getManufactureName(),itemOrderOffer.getProductName(),itemOrderOffer.getQuantity(),itemOrderOffer.getUnitPrice(),user.getUsername());
        Offer offer = new Offer(itemOrderOffer.getManufacturerName(),itemOrderOffer.getProductName(),itemOrderOffer.getQuantity(),itemOrderOffer.getUnitPrice(),user.getUsername());
        return (this.productionMap.insertItemOrderOffer(offer));
    }

    private boolean addProducionOffer(Protocol.Message message,ZMQ.Socket socket){
        Protocol.ItemProductionOffer itemProductionOffer = message.getItemProductionOffer();
        Protocol.User user = message.getUser();
        ProductionOffer productionOffer = new ProductionOffer(user.getUsername(),itemProductionOffer.getArticleName(),itemProductionOffer.getMinimumAmount(),itemProductionOffer.getMaximumAmount(),itemProductionOffer.getUnitPrice(),(int)itemProductionOffer.getPeriod(),true,socket);
        ProductionOffer productionOffer = new ProductionOffer(user.getUsername(),itemProductionOffer.getName(),itemProductionOffer.getMinimumAmount(),itemProductionOffer.getMaximumAmount(),itemProductionOffer.getUnitPrice(),(int)itemProductionOffer.getPeriod(),true,socket);
       return (this.productionMap.insertProductionOffert(productionOffer));
    }


    
}

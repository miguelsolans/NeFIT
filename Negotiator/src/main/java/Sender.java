import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Sender {

    public void sendProductionOffer(String name,String productName,float unitPrice,float minimumAmount,float maximumAmount,int period) {
        //post
        try{
            String urlProduction = "http://localhost:8080/product/"+name+"?productName="+productName+"&unitPrice="+unitPrice+"&minimumAmount="+minimumAmount+"&maximumAmount="+maximumAmount+"&period="+period;
            this.request("POST",urlProduction);
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void sendItemOrderOffer(String nameImporter,String manufacturer,String productName,float quantity, float unitPrice)  {
        //post
        try{
            String urlImporter = "http://localhost:8080/importer/"+nameImporter+"?manufacturer="+manufacturer+"&product="+productName+"&quantity="+quantity+"&price="+unitPrice;
            this.request("POST",urlImporter);
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void sendWinnerOffer(String name,int id){
        try{
            String urlWinner = "http://localhost:8080/negotiation/winner?user="+name+"&id="+id;
            this.request("PUT",urlWinner);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void request(String type, String urlString) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(type);

        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;

        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        System.out.println(result.toString());
    }

}

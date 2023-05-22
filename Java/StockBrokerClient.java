import io.nats.client.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class StockBrokerClient {
    private String portfolio;
    private String strategy;
    private ArrayList<String> subs = new ArrayList<String>();

    public StockBrokerClient(String pfile, String sfile){
        try {
            String filename = "Clients/" + pfile;
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String port = "";
            while ((line = br.readLine()) != null) {
                port += line;
            }

            String fs = "Clients/" + sfile;
            File f = new File(fs);
            BufferedReader br2 = new BufferedReader(new FileReader(f));
            String l;
            String strat = "";
            while ((l = br2.readLine()) != null) {
                strat += line;
            }
            this.portfolio = port;
            this.strategy = strat;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        String pfile = "portfolio-1.xml";
        String sfile = "strategy-1.xml";
        StockBrokerClient sc = new StockBrokerClient(pfile, sfile);
        sc.run();
    }

    public void run() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new InputSource(new StringReader(this.portfolio)));
            Element root = doc.getDocumentElement();
            String[] find = portfolio.split("\"");
            String [] temp = new String[30];
            for(int i = 1; i < find.length; i+=2) {
                subs.add(find[i]);
                temp[(i-1)/2] = find[i];
            }
            String[] stock = new String[subs.size()];
            for(int i = 0; i < subs.size(); i++) {
                stock[i] = temp[i] + ", " + root.getElementsByTagName("stock").item(i).getTextContent();
            }
            
            for(int i = 0; i < stock.length; i++){
                System.out.println(stock[i]);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
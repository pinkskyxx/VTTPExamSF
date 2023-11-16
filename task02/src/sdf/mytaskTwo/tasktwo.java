package sdf.mytaskTwo;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
  
public class tasktwo {
    static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?"); // check if pattern numeric for delete section
 
    static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
 
    static Map<String, List<Items>> Items_receive;
    // static List<Integer> my_prod_id;
    // static List<Integer> my_prod_id;
    // static List<Integer> my_prod_id;
    // static List<Integer> my_prod_id;
 
    // private static Items my_new_items;
 
    public static void main(String[] args) throws Exception {
        System.out.println("tasktwo");
        // request_id: 01HFBANPZE69HBM5J09WESZPEY
        // item_count: 14
        // budget: 1902.00
 
        String request_id = "";
        int item_count = 0;
        float budget = 0;
 
        Integer port = 3000;
        String Server_Name = "localhost";
        List<String> Items_receive_seq = new ArrayList<>();
 
        switch (args.length) {
            case 0:
                port = 3000;
                break;
            case 1:
                boolean chk = isNumeric(args[0]);
                if (chk == true) {
                    port = Integer.parseInt(args[0]);
                } else {
                    System.out.println("Port number Argument error");
                    System.exit(1);
                }
                break;
            case 2:
                // cookieFile = args[0];
                Server_Name = args[0];
                boolean chk1 = isNumeric(args[1]);
                if (chk1 == true) {
                    port = Integer.parseInt(args[1]);
                } else {
                    System.out.println("Port number Argument error");
                    System.exit(1);
                }
                break;
            default:
                System.err.println("Argument error");
                System.exit(1);
		break;
        } // end switch
 
        Socket socket = new Socket(Server_Name, port);
 
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
 
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        System.out.println("Client Receive");
 
        // request_id: 01HFBANPZE69HBM5J09WESZPEY
        // item_count: 14
        // budget: 1902.00
        // prod_list
        // prod_start
        // prod_id: 17
        // title: Rain Jacket Women Windbreaker Striped Climbing Raincoats
        // price: 39.99
        // rating: 3.80
        // prod_end
 
        boolean receiveing = true;
        while (receiveing) {
            String line = br.readLine();
            System.out.println(line);
 
            String userCommand = line.trim().replaceAll(":", " ");// .replaceAll("\\s+", " ");
            String[] terms = userCommand.split("\\s+");
            switch (terms[0]) {
                case "request_id":
                    request_id = terms[1];
                    break;
 
                case "item_count":
                    item_count = Integer.parseInt(terms[1]);
                    break;
 
                case "budget":
                    budget = Float.parseFloat(terms[1]);
                    break;
                case "prod_list":
                    receiveing = false;
                    break;
                default:
                    break;
            }
        }
 
        receiveing = true;
        boolean started = false;
        int prod_id;
        String title;
        float price;
        float rating;
        while (receiveing) {
            String line = br.readLine();
            System.out.println(line);
 
            String userCommand = line.trim().replaceAll(":", " ");// .replaceAll("\\s+", " ");
            String[] terms = userCommand.split("\\s+");
            // prod_start
            // prod_id: 17
            // title: Rain Jacket Women Windbreaker Striped Climbing Raincoats
            // price: 39.99
            // rating: 3.80
            // prod_end
            switch (terms[0]) {
                case "prod_start":
                    started = true;
                    break;
                case "prod_id":
                    prod_id = Integer.parseInt(terms[1]);
                    break;
 
                case "title":
                    title = terms[1];
                    break;
                case "price":
                    price = Float.parseFloat(terms[1]);
                    break;
                case "rating":
                    rating = Float.parseFloat(terms[1]);
                    break;
 
                case "prod_end":
 
                    // if (started == true) {
                    // }
                    
                    //Items_receiveA.put(prod_id,nn);
                        started = false;
                    break;
                default:
                    break;
            }
        }
 
        for (int i = 0; i < 200; i++) {
            String line = br.readLine();
            System.out.println(line);
        }
        is.close();
        os.close();
        socket.close();
        System.out.println("Client end");
    }
}
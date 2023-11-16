package sdf.mytaskTwo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    static Map<Integer, List<Items>> Items_receive;

    public static void main(String[] args) throws Exception {
        System.out.println("tasktwo");

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
        }

        Socket socket = new Socket(Server_Name, port);

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        System.out.println("Client Receive");

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
        String prod_id = "";
        String title = "";
        String price = "";
        String rating = "";
        String fname = "myItems.db";
        try (Writer fileWriter = new FileWriter(fname, false)) {
            fileWriter.append("");
        }
        int total_collect = 0;
        while (receiveing) {
            String line = br.readLine();
            System.out.println(line);

            String userCommand = line.trim().replaceAll(":", " ");// .replaceAll("\\s+", " ");
            String[] terms = userCommand.split("\\s+");

            switch (terms[0]) {
                case "prod_start":
                    total_collect++;
                    started = true;
                    break;
                case "prod_id":
                    prod_id = terms[1];
                    break;

                case "title":
                    title = terms[1];
                    break;
                case "price":
                    price = terms[1];
                    break;
                case "rating":
                    rating = terms[1];
                    break;

                case "prod_end":

                    if (started == true) {
                        if (total_collect >= item_count)
                            receiveing = false;
                        try (Writer fileWriter = new FileWriter(fname, true)) {
                            fileWriter.append(prod_id + "," + title + "," + price + "," + rating + ",");
                            fileWriter.append(System.getProperty("line.separator"));
                        }
                    }
                    started = false;
                    break;
                default:
                    System.out.println("end of items list");
                    receiveing = false;
                    break;
            }
        }
        System.out.println("Preparing ");
        // Preparing items list
        try (FileReader fr = new FileReader(fname)) {
            BufferedReader br1 = new BufferedReader(fr);

            Items_receive = br1.lines()

                    .map(row -> row.trim().split(","))

                    .map(fields -> new Items(Integer.parseInt(fields[0]), fields[1], Float.parseFloat(fields[2]),
                            Float.parseFloat(fields[3])))

                    .collect(Collectors.groupingBy(Category -> Category.getProd_id()));
        }

        System.out.println("Size of items: " + Items_receive.size());
        System.out.println();
        for (Integer items_ID : Items_receive.keySet()) {
            List<Items> aa = Items_receive.get(items_ID);
            for (Items its : aa) {
                System.out.println(items_ID + " ID : " + its.getProd_id());
                System.out.println(items_ID + " Title: " + its.getTitle());
                System.out.println(items_ID + " Price: " + its.getPrice());
                System.out.println(items_ID + " Rating: " + its.getRating());
            }

        }

        is.close();
        os.close();
        socket.close();
        System.out.println("Client end");
    }
}

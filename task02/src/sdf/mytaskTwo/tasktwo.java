package sdf.mytaskTwo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class tasktwo {
    static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?"); // check if pattern numeric for delete section

    static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    static Map<Integer, List<Items>> Items_receive = new HashMap<>();

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void printMap(Map<Integer, Float> sorted_Rate) {
        for (Entry<Integer, Float> entry : sorted_Rate.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
    }

    private static Map<Integer, Float> sortByComparatorF(Map<Integer, Float> unsortMap, final boolean order) {

        List<Entry<Integer, Float>> list = new LinkedList<Entry<Integer, Float>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Integer, Float>>() {
            public int compare(Entry<Integer, Float> o1,
                    Entry<Integer, Float> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Float> sortedMap = new LinkedHashMap<Integer, Float>();
        for (Entry<Integer, Float> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    static Map<Integer, Float> MItems_rating = new HashMap<>();
    static Map<Integer, Float> MItems_Pricing = new HashMap<>();
    static Map<Integer, Float> MItems_toSubmit = new HashMap<>();

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
            // prod_start
            // prod_id: 17
            // title: Rain Jacket Women Windbreaker Striped Climbing Raincoats
            // price: 39.99
            // rating: 3.80
            // prod_end
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
                        Items newItem = new Items(Integer.parseInt(prod_id), title, Float.parseFloat(price),
                                Float.parseFloat(rating));
                        List<Items> myList = new ArrayList<>();
                        myList.add(newItem);
                        Items_receive.put(Integer.parseInt(prod_id), myList);
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

        System.out.println("Size of items: " + Items_receive.size());
        System.out.println();
        for (Integer items_ID : Items_receive.keySet()) {
            List<Items> aa = Items_receive.get(items_ID);
            for (Items its : aa) {
                System.out.println(items_ID + " ID : " + its.getProd_id());
                System.out.println(items_ID + " Title: " + its.getTitle());
                System.out.println(items_ID + " Price: " + its.getPrice());
                System.out.println(items_ID + " Rating: " + its.getRating());
                MItems_rating.put(its.getProd_id(), its.getRating());
                MItems_Pricing.put(its.getProd_id(), its.getPrice());
            }
        }

        // Get sorted rate
        Map<Integer, Float> Sorted_Rate = sortByComparatorF(MItems_rating, false);
        printMap(Sorted_Rate);

        // Calculate the budget
        float myCalBudget = 0;
        StringBuilder myItems_tosend = new StringBuilder();

        for (Integer its : Sorted_Rate.keySet()) {
            float myAmt = MItems_Pricing.get(its);
            myCalBudget += myAmt;
            System.out.println(" ID : " + its + " Amt:" + myAmt + " Rating: " + Sorted_Rate.get(its));
            if (myCalBudget > budget) {
                myCalBudget -= myAmt;
            } else {
                myItems_tosend.append(its + " ");
            }
        }
        String myItems_tosend_String = myItems_tosend.toString().trim().replaceAll(" ", ",");

        System.out.println("Budget: " + budget + " , My calBudget = " + myCalBudget);
        float leftAmt = budget - myCalBudget;

        bw.write("request_id: " + request_id + "\n");
        bw.flush();
        bw.write("name: " + "apryl" + "\n");
        bw.flush();
        bw.write("email: " + "apryllimjob@gmail.com" + "\n");
        bw.flush();
        bw.write("items: " + myItems_tosend_String + "\n");
        bw.flush();
        bw.write("spent: " + String.valueOf(myCalBudget) + "\n");
        bw.flush();
        bw.write("remaining: " + String.valueOf(leftAmt) + "\n");
        bw.flush();
        bw.write("client_end" + "\n");
        bw.flush();
        bw.write(request_id + "\n");

        String line3 = br.readLine();
        System.out.println(line3);
        is.close();
        os.close();
        socket.close();
        System.out.println("client_end");
    }
}
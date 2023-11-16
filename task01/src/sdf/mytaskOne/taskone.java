package sdf.mytaskOne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class taskone {
    static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?"); // check if pattern numeric for delete section

    static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static final int COL_APP = 0;
    public static final int COL_CATEGORY = 1;
    public static final int COL_RATING = 2;

    static HashMap<String, Float> MapHighest = new HashMap<>();
    static HashMap<String, Float> MapLowest = new HashMap<>();
    static HashMap<String, String> MapHighestName = new HashMap<>();
    static HashMap<String, String> MapLowestName = new HashMap<>();
    static HashMap<String, Float> MapAverage = new HashMap<>();
    static HashMap<String, Integer> MapCount = new HashMap<>();
    static HashMap<String, Integer> MapDiscarded = new HashMap<>();
    static Map<String, List<googleplaystore>> classfied;

    public static void main(String[] args) throws Exception {
        System.out.println("taskone");
        String file_name = "";
        if (args.length > 0) {
            System.out.println(args[0]);
            file_name = args[0];
        }

        if (args.length <= 0) {
            // file_name = "googleplaystore.csv";
            System.err.println("Missing CSV");
            System.exit(1);
        }

        System.out.printf("Processing %s\n", file_name);

        try (FileReader fr = new FileReader(file_name)) {
            BufferedReader br = new BufferedReader(fr);

            classfied = br.lines()
                    .skip(1)

                    .map(row -> row.trim().split(","))

                    .map(fields -> new googleplaystore(fields[COL_APP], fields[COL_CATEGORY].toLowerCase(),
                            fields[COL_RATING]))

                    .collect(Collectors.groupingBy(Category -> Category.getCategory()));

            int total_all_line = 0;
            for (String Category : classfied.keySet()) {
                List<googleplaystore> google = classfied.get(Category);

                MapCount.put(Category, google.size());
                Integer total_size = google.size();
                float Highest = 0;
                float Lowest = 0;
                String Highest_name = "";
                String Lowest_name = "";
                float total_rating = 0;
                Integer discarded = 0;

                boolean first = true;
                for (googleplaystore Google : google) {
                    String val = Google.getRating();
                    if (first == true) {
                        if (isNumeric(val) == true) {
                            Highest = Float.parseFloat(val);
                            Lowest = Float.parseFloat(val);
                            Highest_name = Google.getApp();
                            Lowest_name = Google.getApp();
                            first = false;
                            total_rating += Float.parseFloat(val);
                        } else {
                            discarded++;
                            total_size--;
                        }
                    } else {

                        if (isNumeric(val) == true) {
                            Float temp = Float.parseFloat(val);
                            total_rating += temp;
                            if (temp > Highest) {
                                Highest = temp;
                                Highest_name = Google.getApp();
                            }
                            if (temp < Lowest) {
                                Lowest = temp;
                                Lowest_name = Google.getApp();
                            }
                        } else {
                            discarded++;
                            total_size--;
                        }
                    }

                }
                MapHighest.put(Category, Highest);
                MapLowest.put(Category, Lowest);
                MapHighestName.put(Category, Highest_name);
                MapLowestName.put(Category, Lowest_name);
                MapLowest.put(Category, Lowest);
                MapAverage.put(Category, total_rating / total_size);
                MapDiscarded.put(Category, discarded);
            }
            for (String Category : classfied.keySet()) {
                System.out.println("Category: " + Category);
                System.out.println("Highest: " + MapHighestName.get(Category) + ", " + MapHighest.get(Category));
                System.out.println("Lowest: " + MapLowestName.get(Category) + ", " + MapLowest.get(Category));
                System.out.println("Average: " + MapAverage.get(Category));
                System.out.println("Count: " + MapCount.get(Category));
                System.out.println("Discarded: " + MapDiscarded.get(Category));
                System.out.println();
                total_all_line += MapCount.get(Category);
            }

            System.out.println("Total lines in file: " + total_all_line);
        }

    }
}
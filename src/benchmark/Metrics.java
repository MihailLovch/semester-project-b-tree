package benchmark;

import dataStructure.BTree;
import dataset.GenerateInput;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Metrics {
    private static final int INPUT_AMOUNT = 5;
    private static BTree tree;
    private static final int FILE_AMOUNT = 21;
    /*
    1 - откуда
    2 - куда
    3 - команда
    4 - набор
    5 - кол-во раз
     */
    public static void main(String[] args) throws IOException {
        if (!validateInput(args)) {
            System.out.println("Invalid input");
        } else {
            ArrayList<Long> averageTime = new ArrayList<>(5);
            int iterations = Integer.parseInt(args[4]);
            for (int i = 0; i < iterations; i++) {
                GenerateInput.generateData(args[0]);
                String[] arg = new String[args.length];
                for (int j =0; j < args.length; j++){
                    arg[j] = args[j];
                }
                arg[2] = "insert";
                switch (args[2]) {
                    case "search":
                    case "remove":
                        testMethod(arg);
                        averageTime.addAll(Arrays.asList(testMethod(args)));
                        break;
                    case "insert":
                        averageTime.addAll(Arrays.asList(testMethod(args)));
                        break;
                    default:
                        System.out.println("Ты дурак");
                }
            }
            writeMetrics(args, countAverage(averageTime,iterations));

        }
    }

    public static long[] countAverage(ArrayList<Long> time, int iterations) {
        long[] average = new long[FILE_AMOUNT];
        for (int i = 0; i < time.size(); i++) {
            average[i % FILE_AMOUNT] += time.get(i);
        }
        for (int i =0 ; i < FILE_AMOUNT; i++){
            average[i] /= iterations;
        }
        average = Arrays.stream(average).sorted().toArray();
        return average;
    }

    public static void writeMetrics(String[] args, long[] time) throws IOException {
        try (FileWriter writer = new FileWriter(Paths.get(args[0]).resolve("metrics").resolve(args[2] + ".txt").toString())) {
            for (long el : time) {
                writer.write(el + "\n");
            }
            writer.flush();
        }
    }
    public static Long[] testMethod(String[] args) throws IOException {
        Path path = Paths.get(args[0]).resolve("methodsData").resolve(args[3]);
        String command = args[2];
        if (command.equals("insert")) {
            tree = new BTree(6);
        }
        Long[] time = new Long[FILE_AMOUNT];
        long start;
        long end;
        int c = 0;


        for (File file : new File(path.toString()).listFiles()){
            try (FileReader reader = new FileReader(file)){
                start = System.currentTimeMillis();
                int num;
                while ((num = reader.read()) != -1){
                    switch (command){
                        case "insert":
                            tree.insert(num);
                            break;
                        case "remove":
                            tree.remove(num);
                            break;
                        case "search":
                            tree.search(num);
                            break;
                    }
                }
                end = System.currentTimeMillis();
                time[c++] = end - start;
            }
        }
        return time;
    }

    public static boolean validateInput(String[] args) {
        if (args.length != INPUT_AMOUNT) {
            return false;
        }
        return new File(args[0]).exists();
    }
}

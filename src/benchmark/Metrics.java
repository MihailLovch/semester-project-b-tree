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
                GenerateInput.generateData();
                switch (args[2]) {
                    case "search":
                        testInsert(args);
                        averageTime.addAll(Arrays.asList(testSearch(args, tree)));
                        break;
                    case "remove":
                        testInsert(args);
                        averageTime.addAll(Arrays.asList(testRemove(args, tree)));
                        break;
                    case "insert":
                        averageTime.addAll(Arrays.asList(testInsert(args)));
                        break;
                    default:
                        System.out.println("Ты дурак");
                }
            }
            writeMetrics(args, countAverage(averageTime));
            FileReader reader = new FileReader(new File("C:\\Users\\MihailLovch\\IdeaProjects\\semester-project-b-tree\\dataset\\metrics\\insert.txt"));
            for (int i = 0; i < 100; i++) {
                System.out.println(reader.read());
            }
        }
    }

    public static Long[] countAverage(ArrayList<Long> time) {
        Long[] average = new Long[5];
        for (int i = 0; i < time.size(); i++) {
            average[i % 5] += time.get(i);
        }
        return average;
    }

    public static void writeMetrics(String[] args, Long[] time) throws IOException {
        try (FileWriter writer = new FileWriter(Paths.get(args[0]).resolve("metrics").resolve(args[2] + ".txt").toString())) {
            for (Long el : time) {
                writer.write(el + "\n");
            }
            writer.flush();
        }
    }

    public static Long[] testSearch(String[] args, BTree bTree) throws IOException {
        Path path = Paths.get(args[0]).resolve("search").resolve(args[3]);
        Long[] time = new Long[5];
        long start;
        long end;
        int c = 0;

        for (int i = 100; i <= 1000000; i *= 10) {
            try (FileReader reader = new FileReader(new File(path.resolve(i + ".txt").toString()))) {
                start = System.currentTimeMillis();
                for (int k = 0; k < i; k++) {
                    bTree.search(bTree.root, reader.read());
                }
                end = System.currentTimeMillis();
                time[c++] = end - start;
            }
        }
        return time;
    }

    public static Long[] testRemove(String[] args, BTree bTree) throws IOException {
        Path path = Paths.get(args[0]).resolve("remove").resolve(args[3]);
        Long[] time = new Long[5];
        long start;
        long end;
        int c = 0;

        for (int i = 100; i <= 1000000; i *= 10) {
            try (FileReader reader = new FileReader(new File(path.resolve(i + ".txt").toString()))) {
                start = System.currentTimeMillis();
                for (int k = 0; k < i; k++) {
                    bTree.remove(bTree.root, reader.read());
                }
                end = System.currentTimeMillis();
                time[c++] = end - start;
            }
        }
        return time;
    }

    public static Long[] testInsert(String[] args) throws IOException {
        Path path = Paths.get(args[0]).resolve("insert").resolve(args[3]);
        tree = new BTree(6);
        Long[] time = new Long[5];
        long start;
        long end;
        int c = 0;

        for (int i = 100; i <= 1000000; i *= 10) {
            try (FileReader reader = new FileReader(new File(path.resolve(i + ".txt").toString()))) {
                start = System.currentTimeMillis();
                for (int k = 0; k < i; k++) {
                    tree.insert(reader.read());
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

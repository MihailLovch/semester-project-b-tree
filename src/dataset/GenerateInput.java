package dataset;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class GenerateInput {

    public static void main(String[] args) throws IOException {
        generateData(args[0]);
    }

    public static void generateData(String path) throws IOException {
        GenerateInput generateInput = new GenerateInput();
        generateInput.fillPackage(path);
    }

    public void fillPackage(String path) throws IOException {
//        for (int i = 1; i <= 5; i++){
//            for (int j = 100; j <= 1000000; j*=10){
//                FileWriter fileWriter = new FileWriter(path.resolve(Integer.toString(i)).resolve(j + ".txt").toFile());
//                Random random = new Random();
//                for (int k = 0; k < j; k++){
//                    int num = random.nextInt(10*i);
//                    fileWriter.write(num);
//                    fileWriter.write("\n");
//                }
//                fileWriter.close();
//            }
//        }
        File[] dataset = new File(path).listFiles();
        for (File packagee : dataset){
            if (!packagee.getName().equals("metrics")){
                File[] nabori = packagee.listFiles();
                for (File files : nabori){
                    int index = Integer.parseInt(files.getName());
                    for (File file : files.listFiles()) {
                        Random random = new Random();
                        int amount = Integer.parseInt(file.getName().substring(0, file.getName().indexOf('.')));
                        try (FileWriter fileWriter = new FileWriter(file)) {
                            for (int k = 0; k < amount; k++) {
                                fileWriter.write(random.nextInt(1000*index));
                                fileWriter.write("\n");
                            }
                            fileWriter.flush();
                        }
                    }
                }
            }
        }
    }
}

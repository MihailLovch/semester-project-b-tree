package dataset;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class GenerateInput {
    private static final Path PATH = Paths.get("C:\\Users\\MihailLovch\\IdeaProjects\\semester-project-b-tree\\dataset");

    public static void main(String[] args) throws IOException {
        GenerateInput generateInput = new GenerateInput();
        generateInput.fillPackage(PATH.resolve("insert"));
        generateInput.fillPackage(PATH.resolve("remove"));
        generateInput.fillPackage(PATH.resolve("search"));
//        FileReader fileReader = new FileReader(new File(PATH.resolve("insert").resolve("1").resolve("100.txt").toString()));
//        for (int i = 0; i < 100; i++){
//            System.out.println(fileReader.read());
//        }
    }
    public void generateData() throws IOException{
        GenerateInput generateInput = new GenerateInput();
        generateInput.fillPackage(PATH.resolve("insert"));
        generateInput.fillPackage(PATH.resolve("remove"));
        generateInput.fillPackage(PATH.resolve("search"));
    }
    public void fillPackage(Path path) throws IOException {
        for (int i = 1; i <= 5; i++){
            for (int j = 100; j <= 1000000; j*=10){
                FileWriter fileWriter = new FileWriter(path.resolve(Integer.toString(i)).resolve(j + ".txt").toFile());
                Random random = new Random();
                for (int k = 0; k < j; k++){
                    int num = random.nextInt(10*i);
                    fileWriter.write(num);
                    fileWriter.write("\n");
                }
                fileWriter.close();
            }
        }
    }
}

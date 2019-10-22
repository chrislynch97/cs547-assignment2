import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

abstract class FileHelper {

  static ArrayList<Test> readTestsFromFile(final String url) {
    ArrayList<Test> tests = new ArrayList<>();
    Path path = Paths.get(url);

    try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {
      String line = br.readLine();
      while (line != null) {
        String[] testDetails = line.split(",");
        Test test = createTest(testDetails);
        tests.add(test);
        line = br.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return tests;
  }

  private static Test createTest(String[] testDetails) {
    String name = testDetails[0];
    ArrayList<Integer> faults = new ArrayList<>();
    for (int i = 1; i < testDetails.length; i++) {
      faults.add(Integer.valueOf(testDetails[i]));
    }
    return new Test(name, faults);
  }

  static void saveRunToFile(final int tournamentSize, final double mutationRate,
                            final double crossoverRate, final int[] generations) {
    StringBuilder stringBuilder = new StringBuilder(
        tournamentSize + "," + mutationRate + "," + crossoverRate);

    for (int generation : generations) {
      stringBuilder.append(",").append(generation);
    }

    String line = stringBuilder.toString();

    System.out.println(line);

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter("out.txt", true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    assert fileWriter != null;
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.println(line);
    printWriter.close();

  }
}

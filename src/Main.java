import java.util.ArrayList;
import java.util.Collections;

public class Main {

  private final int populationSize = 100;
  private final int numberTestsToRun = 5;
  private final double mutationRate = 0.01;
  private final int tournamentSize = 4;
  private final double crossoverRate = 0.5;
  private final String file = "smallfaultmatrix.txt";

  public static void main(String[] args) {
    new Main().run();
  }

  private void run() {
    ArrayList<Test> tests = FileHelper.readTestsFromFile(file);
    Population population = new Population(populationSize);

    do {
      Individual individual = new Individual(randomizeTests(tests), numberTestsToRun);
      population.addIndividual(individual);
    } while (population.size() < populationSize);

    int generation = 1;

    while (population.getFittest().getFitness() < 1) {
      population = evolve(population);
      generation++;
    }

    System.out.println("GA iterations to find solution: " + generation);
    System.out.println("Fitness: " + population.getFittest().getFitness());
    System.out.println("Tests to run:");
    for (int i = 0; i < numberTestsToRun; i++) {
      System.out.println(population.getFittest().getTest(i));
    }
  }

  private ArrayList<Test> randomizeTests(ArrayList<Test> tests) {
    ArrayList<Test> newTests = new ArrayList<>(tests);
    Collections.shuffle(newTests);
    return newTests;
  }

  private Population evolve(Population population) {
    Population newPopulation = new Population(populationSize);
    do {
      Individual i1 = tournament(population);
      Individual i2 = tournament(population);

      Individual[] individuals = crossover(i1, i2);

      newPopulation.addIndividuals(individuals);
    } while (newPopulation.size() < populationSize);

    for (int i = 0; i < newPopulation.size(); i++) {
      newPopulation.setIndividual(i, mutate(newPopulation.getIndividual(i)));
    }

    return newPopulation;
  }

  private Individual tournament(Population population) {
    Population tournamentPopulation = new Population(populationSize);
    for (int i = 0; i < tournamentSize; i++) {
      int randomId = (int) (Math.random() * population.size());
      tournamentPopulation.addIndividual(population.getIndividual(randomId));
    }

    return tournamentPopulation.getFittest();
  }

  private Individual[] crossover(Individual i1, Individual i2) {

    if (Math.random() < crossoverRate) {
      return new Individual[] {i1, i2};
    }

    ArrayList<Test> testList1 = new ArrayList<>();
    ArrayList<Test> testList2 = new ArrayList<>();

    int randomId = (int) (Math.random() * i1.size());

    for (int i = 0; i < randomId; i++) {
      testList1.add(i1.getTest(i));
      testList2.add(i2.getTest(i));
    }
//
//    for (int i = 0; i < i2.size(); i++) {
//      Test t = i2.getTest(i);
//      boolean inList = false;
//      for (Test test : testList1) {
//        if (test.getName().equals(t.getName())) {
//          inList = true;
//          break;
//        }
//      }
//      if (!inList) {
//        testList1.add(t);
//      }
//    }

    for (int i = 0; i < i1.size(); i++) {
      Test test = i2.getTest(i);
      boolean inList = testList1.contains(test);
      if (!inList) {
        testList1.add(test);
      }


      test = i1.getTest(i);
      inList = testList2.contains(test);
      if (!inList) {
        testList2.add(test);
      }
    }
//
//    for (int i = 0; i < i1.size(); i++) {
//      Test t = i1.getTest(i);
//      boolean inList = false;
//      for (Test test : testList2) {
//        if (test.getName().equals(t.getName())) {
//          inList = true;
//          break;
//        }
//      }
//      if (!inList) {
//        testList2.add(t);
//      }
//    }

    return new Individual[] {
        new Individual(testList1, numberTestsToRun),
        new Individual(testList2, numberTestsToRun)};
  }

  private Individual mutate(Individual individual) {
    int randomId1 = (int) (Math.random() * individual.size());
    int randomId2 = (int) (Math.random() * individual.size());

    if (Math.random() < mutationRate) {
      individual.swap(randomId1, randomId2);
    }

    return individual;
  }

}

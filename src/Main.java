import java.util.ArrayList;
import java.util.Collections;

// Christopher Lynch
// JJB19182

/*
    out.txt was created by using the run() method that is commented out
    with the small matrix to find optimal values for tournamentSize,
    mutationRate and crossoverRate

    out.xlsx is a spreadsheet made from out.txt to easily visualise the output

    evaluation.xlsx is the results of running each of the methods 10 times with
    both small and big matrix
 */

public class Main {

  private final int populationSize = 100;
  private final int numberTestsToRun = 40;    // small 5 big 40

  private int tournamentSize = 5;
  private double mutationRate = 0.9;
  private double crossoverRate = 0.9;
  private final double maxFitness = 0.7631578947368421; // small 1 big 0.7631578947368421

  private final String file = "bigfaultmatrix.txt";

  public static void main(String[] args) {
    new Main().run();
  }

  //  private void run() {
  //    for (tournamentSize = 2; tournamentSize < 6; tournamentSize++) {
  //      for (mutationRate = 0.1; mutationRate < 1; mutationRate += 0.1) {
  //        for (crossoverRate = 0.1; crossoverRate < 1; crossoverRate += 0.1) {
  //
  //          int generation = 1;
  //          int[] generations = new int[10];
  //
  //          for (int runs = 1; runs <= 10; runs++) {
  //            ArrayList<Test> tests = FileHelper.readTestsFromFile(file);
  //            Population population = new Population(populationSize);
  //
  //            do {
  //              Individual individual = new Individual(randomizeTests(tests), numberTestsToRun);
  //              population.addIndividual(individual);
  //            } while (population.size() < populationSize);
  //
  //            generation = 1;
  //
  //            while (population.getFittest().getFitness() < 1) {
  //              population = evolve(population);
  //              generation++;
  //            }
  //
  ////            System.out.println("GA iterations to find solution: " + generation);
  ////            System.out.println("Fitness: " + population.getFittest().getFitness());
  ////            System.out.println("Tests to run:");
  ////            for (int i = 0; i < numberTestsToRun; i++) {
  ////              System.out.println(population.getFittest().getTest(i));
  ////            }
  //
  //            generations[runs-1] = generation;
  //
  //          }
  //
  //          FileHelper.saveRunToFile(tournamentSize, mutationRate, crossoverRate, generations);
  //
  //        }
  //      }
  //    }
  //  }

  private void run() {
    ArrayList<Test> tests = FileHelper.readTestsFromFile(file);
    Population population = new Population(populationSize);

    do {
      Individual individual = new Individual(randomizeTests(tests), numberTestsToRun);
      population.addIndividual(individual);
    } while (population.size() < populationSize);

    int generation = 1;

    while (population.getFittest().getFitness() < maxFitness) {
      population = evolve(population);
      generation++;

    }

    System.out.println("GA iterations to find solution: " + generation);
    //    System.out.println("Fitness: " + population.getFittest().getFitness());
    //    System.out.println("Tests to run:");
    //    for (int i = 0; i < numberTestsToRun; i++) {
    //      System.out.println(population.getFittest().getTest(i));
    //    }

    randomSearch();

    hillclimb();
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

    if (Math.random() > crossoverRate) {
      return new Individual[] {i1, i2};
    }

    ArrayList<Test> testList1 = new ArrayList<>();
    ArrayList<Test> testList2 = new ArrayList<>();

    int randomId = (int) (Math.random() * i1.size());

    for (int i = 0; i < randomId; i++) {
      testList1.add(i1.getTest(i));
      testList2.add(i2.getTest(i));
    }

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
    return new Individual[] {
        new Individual(testList1, numberTestsToRun),
        new Individual(testList2, numberTestsToRun)};
  }

  private Individual mutate(Individual individual) {
    if (Math.random() <= mutationRate) {
      int randomId1 = (int) (Math.random() * individual.size());
      int randomId2 = (int) (Math.random() * individual.size());
      individual.swap(randomId1, randomId2);
    }

    return individual;
  }

  private void randomSearch() {
    ArrayList<Test> tests = FileHelper.readTestsFromFile(file);
    Population population = new Population(populationSize);

    do {
      Individual individual = new Individual(randomizeTests(tests), numberTestsToRun);
      population.addIndividual(individual);
    } while (population.size() < populationSize);

    Individual individual = population.getIndividual(0);

    ArrayList<Integer> faults = new ArrayList<>();
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);

    Test test = new Test(individual.getTest(0).getName(), faults);

    individual.setTest(0, test);

    population.setIndividual(0, individual);

    int randomId;
    int counter = 1;
    Individual randomIndividual;

    do {
      randomId = (int) (Math.random() * population.size());
      randomIndividual = population.getIndividual(randomId);
      counter++;

    } while (randomIndividual.getFitness() < maxFitness);

    System.out.println("Random search iterations to find solution: " + counter);
  }

  private void hillclimb() {
    ArrayList<Test> tests = FileHelper.readTestsFromFile(file);
    Population population = new Population(populationSize);

    do {
      Individual individual = new Individual(randomizeTests(tests), numberTestsToRun);
      population.addIndividual(individual);
    } while (population.size() < populationSize);

    Individual individual = population.getIndividual(0);

    ArrayList<Integer> faults = new ArrayList<>();
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);
    faults.add(1);

    Test test = new Test(individual.getTest(0).getName(), faults);

    individual.setTest(0, test);

    population.setIndividual(0, individual);

    int randomId = (int) (Math.random() * population.size());
    Individual peakIndividual = population.getIndividual(randomId);
    int counter = 1;

    do {
      boolean peak = false;
      individual = population.getIndividual(randomId);

      do {
        int leftId = population.getIndexOf(individual) - 1;
        if (leftId < 0) {
          leftId = population.size() - 1;
        }

        int rightId = population.getIndexOf(individual) + 1;
        if (rightId == population.size()) {
          rightId = 0;
        }

        double leftDifference = population.getIndividual(
            leftId).getFitness() - individual.getFitness();

        double rightDifference = population.getIndividual(
            rightId).getFitness() - individual.getFitness();

        if (leftDifference > 0 && leftDifference > rightDifference) {
          individual = population.getIndividual(leftId);
        } else if (rightDifference > 0 && rightDifference > leftDifference) {
          individual = population.getIndividual(rightId);
        } else {
          peak = true;
        }

        counter++;
      } while (!peak);

      if (individual.getFitness() > peakIndividual.getFitness()) {
        peakIndividual = individual;
      }

      randomId = (int) (Math.random() * population.size());

    } while (peakIndividual.getFitness() < maxFitness);

    System.out.println("Hill climber iterations to find solution: " + counter);
  }
}

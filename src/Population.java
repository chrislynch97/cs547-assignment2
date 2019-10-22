import java.util.ArrayList;

class Population {

  private ArrayList<Individual> individuals;
  private final int populationSize;

  Population(final int populationSize) {
    this.populationSize = populationSize;
    individuals = new ArrayList<>();
  }

  void addIndividual(Individual newIndividual) {
    boolean add = true;
    for (Individual individual : individuals) {
      if (newIndividual.equals(individual)) {
        add = false;
        break;
      }
    }
    if (add) {
      if (individuals.size() < populationSize) {
        individuals.add(newIndividual);
      }
    }
  }

  void addIndividuals(Individual[] newIndividuals) {
    for (Individual newIndividual : newIndividuals) {
      addIndividual(newIndividual);
    }
  }

  Individual getIndividual(int i) {
    return individuals.get(i);
  }

  Individual getFittest() {
    Individual fittest = individuals.get(0);
    for (Individual individual : individuals) {
      if (individual.getFitness() > fittest.getFitness()) {
        fittest = individual;
      }
    }
    return fittest;
  }

  int size() {
    return individuals.size();
  }

  void setIndividual(int id, Individual individual) {
    individuals.set(id, individual);
  }

  int getIndexOf(Individual individual) {
    return individuals.indexOf(individual);
  }

}

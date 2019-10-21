import java.util.ArrayList;
import java.util.Collections;

class Individual {

  private final ArrayList<Test> tests;
  private final int numberTestsToRun;

  Individual(final ArrayList<Test> tests, final int numberTestsToRun) {
    this.tests = tests;
    this.numberTestsToRun = numberTestsToRun;
  }

  double getFitness() {
    ArrayList<Integer> faults = new ArrayList<>();

    for (int i = 0; i < tests.get(0).numberFaults(); i++) {
      boolean one = false;
      for (int j = 0; j < numberTestsToRun; j++) {
        if (tests.get(j).getFault(i) == 1) {
          one = true;
        }
      }
      if (one) {
        faults.add(1);
      } else {
        faults.add(0);
      }
    }

    int noFaults = Collections.frequency(faults, 1);

    return (double) noFaults / (double) faults.size();
  }

  int size() {
    return tests.size();
  }

  Test getTest(int id) {
    return tests.get(id);
  }

  void swap(int i1, int i2) {
    Collections.swap(tests, i1, i2);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Individual)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    Individual individual = (Individual) obj;

    for (int i = 0; i < tests.size(); i++) {
      if (!tests.get(i).getName().equals(individual.getTest(i).getName())) {
        return false;
      }
    }

    return true;
  }
}

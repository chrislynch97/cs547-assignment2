import java.util.ArrayList;

class Test {

  private final String name;
  private final ArrayList<Integer> faults;

  Test(String name, ArrayList<Integer> faults) {
    this.name = name;
    this.faults = faults;
  }

  String getName() {
    return name;
  }

  int getFault(int id) {
    return faults.get(id);
  }

  int numberFaults() {
    return faults.size();
  }

  @Override
  public String toString() {
    return "Test{"
        + "name='" + name + '\''
        + ", faults=" + faults
        + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Test)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    Test test = (Test) obj;

    return test.getName().equals(this.getName());

  }
}

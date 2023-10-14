import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FizzBuzz {
  public static void main(String[] args) {
    int start = 1;
    int end   = 35;
    try {
      end = Integer.parseInt(args[0]);
    } catch(IndexOutOfBoundsException | NumberFormatException ignored) {}

    CheckList checkList = new CheckList();
    checkList.add("Fizz", 3);
    checkList.add("Buzz", 5);
    // checkList.add("Xezz", 7);
    // checkList.add("Tozz", 10);
    // checkList.add(""    , 11);
    FB fb = new FB(start, checkList.get());

    for(int i = 0; i <= end - start; i++) {
      System.out.println(fb.get());
      fb.inc();
    }
  }

  public record WordAndNumber (
    String word,
    int    number
  ) {}

  public static class CheckList {
    private List<WordAndNumber> checkList = new ArrayList<WordAndNumber>();

    public void add(String word, int number) {
      WordAndNumber wn = new WordAndNumber(word, number);
      checkList.add(wn);
    }

    public List<WordAndNumber> get() {
      return checkList;
    }
  }

  public static class FB {
    private int                 counter;
    private List<WordAndNumber> checkList;

    public void inc() {
      this.counter++;
    }

    public void setCount(int count) {
      this.counter = count;
    }

    public void setCheckList(List<WordAndNumber> checkList) {
      this.checkList = checkList;
    }

    FB(int start, List<WordAndNumber> checkList) {
      this.setCount(start);
      this.setCheckList(checkList);
    }

    private Stream<String> check(WordAndNumber wn) {
      if (this.counter % wn.number == 0) {
        return Stream.of(wn.word);
      } else {
        return Stream.empty();
      }
    }

    public String get() {
      return this.checkList.stream()
        .flatMap(wn -> this.check(wn))
        .reduce(String::concat)
        .orElse(Integer.valueOf(this.counter).toString());
    }
  }
}

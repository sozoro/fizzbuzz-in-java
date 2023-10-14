import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FizzBuzz {
  public static void main(String[] args) {
    final CheckList checkList = new CheckList();
    checkList.add("Fizz", n -> n % 3 == 0);
    checkList.add("Buzz", n -> n % 5 == 0);
    // checkList.add("Xezz", n -> n % 7 == 0);

    IntStream.rangeClosed(1, 100)
             .mapToObj(checkList::check)
             .forEach(System.out::println);
  }
}

record WordAndPredicate (
  String       word,
  IntPredicate pred
) {}

class CheckList {
  private List<WordAndPredicate> checkList = new ArrayList<WordAndPredicate>();

  public void add(String word, IntPredicate pred) {
    WordAndPredicate wp = new WordAndPredicate(word, pred);
    checkList.add(wp);
  };

  public String check(int n) {
    return this.checkList.stream()
      .flatMap(wp -> wp.pred().test(n) ? Stream.of(wp.word()) : Stream.empty())
      .collect(OptBuilder::new, OptBuilder::accumulate, OptBuilder::combine)
      .get().map(StringBuilder::toString)
      .orElse(String.valueOf(n));
  }
}

class OptBuilder {
  private Optional<StringBuilder> optBuilder = Optional.empty();

  public Optional<StringBuilder> get() {
    return this.optBuilder;
  }

  public OptBuilder accumulate(String str) {
    this.optBuilder = this.optBuilder
      .of(this.optBuilder.orElse(new StringBuilder()).append(str));
    return this;
  }

  public OptBuilder combine(OptBuilder ob2) {
    Optional<StringBuilder> optBuilder2 = ob2.get();
    this.optBuilder = this.optBuilder
      .map(builder -> optBuilder2.map(builder::append).orElse(builder))
      .or(() -> optBuilder2);
    return this;
  }
}

/*
class OptBuilder {
  private StringBuilder builder;
  private Boolean isEmpty = true;

  public Optional<StringBuilder> get() {
    return this.isEmpty ? Optional.empty() : Optional.of(this.builder);
  }

  public OptBuilder accumulate(String str) {
    if (this.isEmpty) {
      this.builder = (new StringBuilder()).append(str);
      this.isEmpty = false;
    } else {
      this.builder.append(str);
    }
    return this;
  }

  public OptBuilder combine(OptBuilder ob2) {
    final Optional<StringBuilder> optBuilder2 = ob2.get();
    if (this.isEmpty) {
      if (optBuilder2.isPresent()) {
        this.builder = optBuilder2.get();
        this.isEmpty = false;
      }
    } else {
      optBuilder2.map(this.builder::append);
    }
    return this;
  }
}
*/

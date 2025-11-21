import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class LogProcessorSequential {

    public static void main(String[] args) throws IOException {

        String folder = "logs";
        String[] keywords = {"ERROR", "WARN", "INFO"};

        Map<String, Integer> map = new HashMap<>();

        long start = System.currentTimeMillis();

        Files.list(Paths.get(folder))
             .filter(Files::isRegularFile)
             .forEach(file -> {
                 try {
                     Files.lines(file)
                          .flatMap(line -> java.util.stream.Stream.of(line.split("\\s+")))
                          .forEach(word -> {
                              for (String key : keywords) {
                                  if (word.equalsIgnoreCase(key)) {
                                      map.merge(key, 1, Integer::sum);
                                  }
                              }
                          });
                 } catch (IOException e) {
                     throw new RuntimeException(e);
                 }
             });

        long end = System.currentTimeMillis();

        System.out.println("===== SEQUENTIAL SUMMARY =====");
        map.forEach((k, v) -> System.out.println(k + " → " + v));

        System.out.println("Execution Time (Sequential): " 
                            + (end - start) + " ms");
    }
}

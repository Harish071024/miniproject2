import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LogWorker implements java.util.concurrent.Callable<Map<String, Integer>> {

    private final Path filePath;
    private final String[] keywords;

    public LogWorker(Path filePath, String[] keywords) {
        this.filePath = filePath;
        this.keywords = keywords;
    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> localMap = new HashMap<>();
        try {
            Files.lines(filePath)
                 .flatMap(line -> java.util.stream.Stream.of(line.split("\\s+")))
                 .forEach(word -> {
                     for (String key : keywords) {
                         if (word.equalsIgnoreCase(key)) {
                             localMap.merge(key, 1, Integer::sum);
                         }
                     }
                 });
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return localMap;
    }
}

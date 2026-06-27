package pvz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class SaveData {
    private static final Path SAVE_PATH = Paths.get("pvz-save.properties");

    private SaveData() {
    }

    public static int loadNextLevelIndex() {
        if (!Files.exists(SAVE_PATH)) {
            return 0;
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(SAVE_PATH)) {
            properties.load(input);
            int savedLevel = Integer.parseInt(properties.getProperty("nextLevelIndex", "0"));
            return Math.max(0, Math.min(49, savedLevel));
        } catch (IOException | NumberFormatException ignored) {
            return 0;
        }
    }

    public static void saveNextLevelIndex(int nextLevelIndex) {
        Properties properties = new Properties();
        properties.setProperty("nextLevelIndex", Integer.toString(Math.max(0, Math.min(49, nextLevelIndex))));
        try (OutputStream output = Files.newOutputStream(SAVE_PATH)) {
            properties.store(output, "Plants vs Zombies Java save");
        } catch (IOException ignored) {
        }
    }
}

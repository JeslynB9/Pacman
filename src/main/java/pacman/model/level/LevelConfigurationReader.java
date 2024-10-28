package pacman.model.level;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to read JSONObject to retrieve level configuration details
 */
public class LevelConfigurationReader {

    private final JSONObject levelConfiguration;
    private final int TICKS_PER_SECOND = 30;

    public LevelConfigurationReader(JSONObject levelConfiguration) {
        this.levelConfiguration = levelConfiguration;
    }

    /**
     * Retrieves the player's speed for the level
     *
     * @return the player's speed for the level
     */
    public double getPlayerSpeed() {
        return ((Number) levelConfiguration.get("pacmanSpeed")).doubleValue();
    }

    /**
     * Retrieves the lengths of the ghost modes in seconds
     *
     * @return the lengths of the ghost modes in seconds
     */
    public Map<String, Integer> getGhostModeLengths() {
        Map<String, Integer> ghostModeLengths = new HashMap<>();
        JSONObject modeLengthsObject = (JSONObject) levelConfiguration.get("modeLengths");

        // Ensure keys match with state class names in LevelImpl
        ghostModeLengths.put("ChaseMode", ((Number) modeLengthsObject.get("chase")).intValue() * TICKS_PER_SECOND);
        ghostModeLengths.put("ScatterMode", ((Number) modeLengthsObject.get("scatter")).intValue() * TICKS_PER_SECOND);
        ghostModeLengths.put("FrightenedMode", ((Number) modeLengthsObject.get("frightened")).intValue() * TICKS_PER_SECOND);

        return ghostModeLengths;
    }

    /**
     * Retrieves the speeds of the ghosts for each ghost mode
     *
     * @return the speeds of the ghosts for each ghost mode
     */
    public Map<String, Double> getGhostSpeeds() {
        Map<String, Double> ghostSpeeds = new HashMap<>();
        JSONObject ghostSpeed = (JSONObject) levelConfiguration.get("ghostSpeed");

        // Check if ghostSpeed is not null
        if (ghostSpeed != null) {
            // Ensure keys exist before trying to access them
            if (ghostSpeed.containsKey("chase")) {
                ghostSpeeds.put("CHASE", ((Number) ghostSpeed.get("chase")).doubleValue());
            } else {
                throw new IllegalArgumentException("Missing ghost speed for 'chase'");
            }

            if (ghostSpeed.containsKey("scatter")) {
                ghostSpeeds.put("SCATTER", ((Number) ghostSpeed.get("scatter")).doubleValue());
            } else {
                throw new IllegalArgumentException("Missing ghost speed for 'scatter'");
            }

            if (ghostSpeed.containsKey("frightened")) {
                ghostSpeeds.put("FRIGHTENED", ((Number) ghostSpeed.get("frightened")).doubleValue());
            } else {
                throw new IllegalArgumentException("Missing ghost speed for 'frightened'");
            }
        } else {
            throw new IllegalArgumentException("ghostSpeed configuration is null");
        }

        System.out.println("Retrieved ghost speeds: " + ghostSpeeds);

        return ghostSpeeds;
    }

}

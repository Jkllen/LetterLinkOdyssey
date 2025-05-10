package letterlinkodyssey;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;

public class ModelFX {
    private List<DialogueEntry> dialogues;
    private int currentDialogueIndex;
    private String playerName;

    public ModelFX() {
        dialogues = new ArrayList<>();
        currentDialogueIndex = 0;
        loadDialoguesFromDatabase();
    }
    
    public void setDialogueIndex(int index) {
        this.currentDialogueIndex = index;
    }

    public int getDialogueIndex() {
        System.out.println("Getting current dialogue index: " + currentDialogueIndex);
        return currentDialogueIndex;
    }
    
    public int getDialogueIndexById(int dialogueId) {
        System.out.println("Searching for dialogue with ID: " + dialogueId);
        for (int i = 0; i < dialogues.size(); i++) {
            if (dialogues.get(i).getId() == dialogueId) {
                System.out.println("Found dialogue at index " + i);
                return i;
            }
        }
        System.out.println("Dialogue with ID " + dialogueId + " not found.");
        return -1;
    }
    
    public DialogueEntry getCurrentDialogue(int index) {
        if (index >= 0 && index < dialogues.size()) {
            System.out.println("Returning dialogue at index " + index);
            return dialogues.get(index);
        }
        System.out.println("Invalid index: " + index);
        return null;
    }
    
    public void loadDialoguesFromDatabase() {
        System.out.println("Loading dialogues from database...");
        dialogues.clear();
        String sql = "SELECT id, speaker, text, character_image, background_image, sfx, type, next_dialogue_id FROM dialogues ORDER BY id";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dialogues.add(new DialogueEntry(
                        rs.getInt("id"),
                        rs.getString("speaker"),
                        rs.getString("text"),
                        rs.getString("character_image"),
                        rs.getString("background_image"),
                        rs.getString("sfx"),
                        rs.getString("type"),
                        rs.getInt("next_dialogue_id")
                ));
                System.out.println("Loaded dialogue ID: " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading dialogues:");
            e.printStackTrace();
            initializeDatabase(); // Create tables if they don't exist
            loadDialoguesFromDatabase(); // Try loading again
        }
        System.out.println("Loaded " + dialogues.size() + " dialogues");
    }
    
    public void saveGame(SaveEntry saveEntry) {
        System.out.println("Saving game to slot: " + saveEntry.getSlotId());
        // SQLite uses INSERT OR REPLACE instead of MySQL's REPLACE INTO
        String sql = "INSERT OR REPLACE INTO saves (slot_id, player_name, dialogue_index, screenshot_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saveEntry.getSlotId());
            stmt.setString(2, saveEntry.getPlayerName());
            stmt.setInt(3, saveEntry.getDialogueIndex());
            stmt.setString(4, saveEntry.getScreenshotPath());
            stmt.executeUpdate();
            System.out.println("Game saved to slot " + saveEntry.getSlotId());
        } catch (SQLException e) {
            System.err.println("Error saving game:");
            e.printStackTrace();
        }
    }

    public void saveGameWithScreenshot(int slot, Scene scene, int dialogueIndex) {
        String screenshotFile = "screenshots/slot_" + slot + ".png";
        saveSceneAsImage(scene, screenshotFile);
        System.out.println("Saving screenshot to: " + screenshotFile);

        SaveEntry saveEntry = new SaveEntry(slot, playerName, dialogueIndex, screenshotFile);
        saveGame(saveEntry);
    }

    private void saveSceneAsImage(Scene scene, String path) {
        try {
            File file = new File(path);
            File dir = file.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }

            WritableImage image = scene.snapshot(null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Screenshot saved to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving screenshot:");
            e.printStackTrace();
        }
    }
    
    public void saveSceneSnapshot(Scene scene) {
        String screenshotFile = "screenshots/slot_preview_temp.png";
        saveSceneAsImage(scene, screenshotFile);
        System.out.println("Scene snapshot saved to: " + screenshotFile);
    }
    
    public boolean loadGame(int slot) {
        System.out.println("Loading game from slot: " + slot);
        String sql = "SELECT player_name, dialogue_index FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                playerName = rs.getString("player_name");
                currentDialogueIndex = rs.getInt("dialogue_index");
                System.out.println("Loaded game from slot " + slot);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error loading game:");
            e.printStackTrace();
        }
        System.out.println("No save found in slot " + slot);
        return false;
    }
    
    public void continueGameFromSlot(int slot, ControllerFX controller) {
        System.out.println("Continuing game from slot " + slot);
        if (loadGame(slot)) {
            ChapterFX chapter = new ChapterFX(controller.getStage(), controller, this, controller.getCurrentChapterNumber());
            chapter.setDialogueIndex(currentDialogueIndex);
            chapter.display();
            System.out.println("Game continued from dialogue index: " + currentDialogueIndex);
        } else {
            System.out.println("❌ Failed to load game from slot " + slot);
        }
    }

    public int getDialogueIndexFromDB(int slot) {
        System.out.println("Fetching dialogue index from DB for slot " + slot);
        String sql = "SELECT dialogue_index FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("dialogue_index");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching dialogue index:");
            e.printStackTrace();
        }
        System.out.println("Dialogue index not found for slot " + slot);
        return 0;
    }

    public void deleteSave(int slot) {
        System.out.println("Deleting save data from slot " + slot);
        String sql = "DELETE FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            stmt.executeUpdate();
            System.out.println("Deleted slot " + slot + " from database.");
        } catch (SQLException e) {
            System.err.println("Error deleting save:");
            e.printStackTrace();
        }

        File screenshot = new File("screenshots/slot_" + slot + ".png");
        if (screenshot.exists() && !screenshot.delete()) {
            System.err.println("Failed to delete screenshot for slot " + slot);
        }
    }

    public void savePlayerNameToDatabase() {
        System.out.println("Saving player name to database: " + playerName);
        String sql = "INSERT INTO players (player_Name) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.executeUpdate();
            System.out.println("✅ Player name inserted: " + playerName);
        } catch (SQLException e) {
            System.err.println("Error saving player name:");
            e.printStackTrace();
        }
    }

    public int getTotalDialogues() {
        return dialogues.size();
    }

    public void setPlayerName(String name) {
        this.playerName = name;
        System.out.println("Player name set to: " + playerName);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void startNewGame() {
        currentDialogueIndex = 0;
        System.out.println("Starting new game, resetting dialogue index to 0");
    }

    public String getBackgroundImagePathByName(String backgroundName) {
        System.out.println("Fetching background image for: " + backgroundName);
        String sql = "SELECT image_path FROM backgrounds WHERE name = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, backgroundName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("image_path");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching background:");
            e.printStackTrace();
        }
        System.out.println("No background found for: " + backgroundName);
        return null;
    }

    public List<ChoiceEntry> getChoicesForDialogue(int dialogueId) {
        System.out.println("Fetching choices for dialogue ID: " + dialogueId);
        List<ChoiceEntry> choices = new ArrayList<>();
        String sql = "SELECT choice_text, next_dialogue_id FROM choices WHERE dialogue_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dialogueId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                choices.add(new ChoiceEntry(rs.getString("choice_text"), rs.getInt("next_dialogue_id")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching choices:");
            e.printStackTrace();
        }

        System.out.println("Found " + choices.size() + " choices for dialogue ID: " + dialogueId);
        return choices;
    }

    public String getPlayerNameForSlot(int slot) {
        System.out.println("Fetching player name for slot " + slot);
        String sql = "SELECT player_name FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("player_name");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching player name:");
            e.printStackTrace();
        }
        return "";
    }

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver not found.");
        }

        // Adjusted path: database is located under src/data
        String dbPath = "src/data/mygame.db";
        File dbFile = new File(dbPath);

        // Create parent directory if it doesn't exist
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }

        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }


    private void initializeDatabase() {
        System.out.println("Initializing database tables...");
        String[] createTables = {
            // Dialogues table
            "CREATE TABLE IF NOT EXISTS dialogues (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "speaker TEXT," +
            "text TEXT," +
            "character_image TEXT," +
            "background_image TEXT," +
            "sfx TEXT," +
            "type TEXT," +
            "next_dialogue_id INTEGER)",
            
            // Saves table
            "CREATE TABLE IF NOT EXISTS saves (" +
            "slot_id INTEGER PRIMARY KEY," +
            "player_name TEXT," +
            "dialogue_index INTEGER," +
            "screenshot_path TEXT)",
            
            // Players table
            "CREATE TABLE IF NOT EXISTS players (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "player_name TEXT)",
            
            // Backgrounds table
            "CREATE TABLE IF NOT EXISTS backgrounds (" +
            "name TEXT PRIMARY KEY," +
            "image_path TEXT)",
            
            // Choices table
            "CREATE TABLE IF NOT EXISTS choices (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dialogue_id INTEGER," +
            "choice_text TEXT," +
            "next_dialogue_id INTEGER)"
        };
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
            System.out.println("Database tables initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database:");
            e.printStackTrace();
        }
    }
}
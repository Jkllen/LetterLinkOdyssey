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
        System.out.println("Getting current dialogue index: " + currentDialogueIndex);  // Debugging
        return currentDialogueIndex;
    }
    
    public int getDialogueIndexById(int dialogueId) {
        System.out.println("Searching for dialogue with ID: " + dialogueId);  // Debugging
        for (int i = 0; i < dialogues.size(); i++) {
            if (dialogues.get(i).getId() == dialogueId) {
                System.out.println("Found dialogue at index " + i);  // Debugging
                return i;
            }
        }
        System.out.println("Dialogue with ID " + dialogueId + " not found.");  // Debugging
        return -1;
    }
    
    public DialogueEntry getCurrentDialogue(int index) {
        if (index >= 0 && index < dialogues.size()) {
            System.out.println("Returning dialogue at index " + index);  // Debugging
            return dialogues.get(index);
        }
        System.out.println("Invalid index: " + index);  // Debugging
        return null;
    }
    
    public void loadDialoguesFromDatabase() {
        System.out.println("Loading dialogues from database...");  // Debugging
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
                System.out.println("Loaded dialogue ID: " + rs.getInt("id"));  // Debugging
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded dialogues:");
        for (DialogueEntry d : dialogues) {
            System.out.println("ID: " + d.getId() + " | " + d.getText());
        }
    }
    
    // Method that directly saves game data
    public void saveGame(SaveEntry saveEntry) {
        System.out.println("SaveEntry Details: ");
        System.out.println("Slot: " + saveEntry.getSlotId());
        System.out.println("Player Name: " + saveEntry.getPlayerName());
        System.out.println("Dialogue Index: " + saveEntry.getDialogueIndex());
        System.out.println("Screenshot Path: " + saveEntry.getScreenshotPath());
        String sql = "REPLACE INTO saves (slot_id, player_name, dialogue_index, screenshot_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saveEntry.getSlotId());
            stmt.setString(2, saveEntry.getPlayerName());
            stmt.setInt(3, saveEntry.getDialogueIndex());
            stmt.setString(4, saveEntry.getScreenshotPath());
            stmt.executeUpdate();
            System.out.println("Game + Screenshot saved to slot " + saveEntry.getSlotId());  // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to save the game with a screenshot
    public void saveGameWithScreenshot(int slot, Scene scene, int dialogueIndex) {
        String screenshotFile = "screenshots/slot_" + slot + ".png";
        saveSceneAsImage(scene, screenshotFile);  // Save image to disk
        System.out.println("Saving screenshot to: " + screenshotFile);  // Debugging

        SaveEntry saveEntry = new SaveEntry(slot, playerName, dialogueIndex, screenshotFile);
        saveGame(saveEntry);  // Call saveGame method to save the game and the screenshot
    }

    private void saveSceneAsImage(Scene scene, String path) {
        try {
            File file = new File(path);
            File dir = file.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();  // create directories if not exist
                System.out.println("Created directories for screenshot path: " + path);  // Debugging
            }

            WritableImage image = scene.snapshot(null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Screenshot saved to: " + path);  // Debugging
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveSceneSnapshot(Scene scene) {
        String screenshotFile = "screenshots/slot_preview_temp.png";
        saveSceneAsImage(scene, screenshotFile); // Reuse your method
        System.out.println("Scene snapshot saved to: " + screenshotFile);  // Debugging
    }
    
    public boolean loadGame(int slot) {
        System.out.println("Loading game from slot: " + slot);  // Debugging
        String sql = "SELECT player_name, dialogue_index FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                playerName = rs.getString("player_name");
                currentDialogueIndex = rs.getInt("dialogue_index");
                System.out.println("Loaded game from slot " + slot);  // Debugging
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Failed to load game from slot " + slot);  // Debugging
        return false;
    }
    
    public void continueGameFromSlot(int slot, ControllerFX controller) {
        System.out.println("Continuing game from slot " + slot);  // Debugging
        if (loadGame(slot)) {
            ChapterFX chapter = new ChapterFX(controller.getStage(), controller, this, controller.getCurrentChapterNumber());
            chapter.setDialogueIndex(currentDialogueIndex);  // You need to implement this setter
            chapter.display();
            System.out.println("Game continued from dialogue index: " + currentDialogueIndex);  // Debugging
        } else {
            System.out.println("❌ Failed to load game from slot " + slot);  // Debugging
        }
    }

    public int getDialogueIndexFromDB(int slot) {
        System.out.println("Fetching dialogue index from DB for slot " + slot);  // Debugging
        String sql = "SELECT dialogue_index FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("dialogue_index");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Dialogue index not found for slot " + slot);  // Debugging
        return 0;
    }

    public void deleteSave(int slot) {
        System.out.println("Deleting save data from slot " + slot);  // Debugging
        String sql = "DELETE FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            stmt.executeUpdate();
            System.out.println("Deleted slot " + slot + " from database.");  // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
        }

        File screenshot = new File("screenshots/slot_" + slot + ".png");
        if (screenshot.exists()) {
            if (screenshot.delete()) {
                System.out.println("Deleted screenshot for slot " + slot);  // Debugging
            } else {
                System.err.println("Failed to delete screenshot for slot " + slot);  // Debugging
            }
        }
    }

    public void savePlayerNameToDatabase() {
        System.out.println("Saving player name to database: " + playerName);  // Debugging
        String sql = "INSERT INTO players (player_Name) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.executeUpdate();
            System.out.println("✅ Player name inserted: " + playerName);  // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalDialogues() {
        System.out.println("Total dialogues count: " + dialogues.size());  // Debugging
        return dialogues.size();
    }

    public void setPlayerName(String name) {
        this.playerName = name;
        System.out.println("Player name set to: " + playerName);  // Debugging
    }

    public String getPlayerName() {
        return playerName;
    }

    public void startNewGame() {
        currentDialogueIndex = 0;
        System.out.println("Starting new game, resetting dialogue index to: " + currentDialogueIndex);  // Debugging
    }

    public String getBackgroundImagePathByName(String backgroundName) {
        System.out.println("Fetching background image for: " + backgroundName);  // Debugging
        String sql = "SELECT image_path FROM backgrounds WHERE name = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, backgroundName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("image_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("No background found for: " + backgroundName);  // Debugging
        return null;
    }

    public List<ChoiceEntry> getChoicesForDialogue(int dialogueId) {
        System.out.println("Fetching choices for dialogue ID: " + dialogueId);  // Debugging
        List<ChoiceEntry> choices = new ArrayList<>();
        String sql = "SELECT choice_text, next_dialogue_id FROM choices WHERE dialogue_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dialogueId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                choices.add(new ChoiceEntry(rs.getString("choice_text"), rs.getInt("next_dialogue_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Fetched " + choices.size() + " choices for dialogue ID: " + dialogueId);  // Debugging
        return choices;
    }

    public String getPlayerNameForSlot(int slot) {
        System.out.println("Fetching player name for slot " + slot);  // Debugging
        String sql = "SELECT player_name FROM saves WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("player_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Connection getConnection() throws SQLException {
        System.out.println("Establishing database connection...");  // Debugging
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.");
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mygame?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "password");
    }
}

package letterlinkodyssey;

import java.sql.*;

import javafx.scene.Scene;

public class GameSettings {

    private static GameSettings instance;

    private boolean skipUnseenText = false;
    private double textSpeed = 1.0;
    private double autoForwardTime = 3.0;
    private String rollbackMode = "Disable";

    private Scene previousScene;

    private GameSettings() {
        loadSettingsFromDatabase(); // Auto-load on init
    }

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public Scene getPreviousScene() {
        return previousScene;
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    
    private boolean accessedFromMainMenu = false;

    public boolean isAccessedFromMainMenu() {
        return accessedFromMainMenu;
    }

    public void setAccessedFromMainMenu(boolean accessedFromMainMenu) {
        this.accessedFromMainMenu = accessedFromMainMenu;
    }


   
    // --- Getters and Setters ---
    public boolean isSkipUnseenText() {
        return skipUnseenText;
    }

    public void setSkipUnseenText(boolean value) {
        this.skipUnseenText = value;
        saveSettingsToDatabase();
    }

    public double getTextSpeed() {
        return textSpeed;
    }

    public void setTextSpeed(double value) {
        this.textSpeed = value;
        saveSettingsToDatabase();
    }

    public double getAutoForwardTime() {
        return autoForwardTime;
    }

    public void setAutoForwardTime(double value) {
        this.autoForwardTime = value;
        saveSettingsToDatabase();
    }

    public String getRollbackMode() {
        return rollbackMode;
    }

    public void setRollbackMode(String mode) {
        this.rollbackMode = mode;
        saveSettingsToDatabase();
    }

    public double getAutoSpeed() {
        return autoForwardTime;
    }

    public long getAutoForwardDelay() {
        double inverse = 6.0 - autoForwardTime;
        return (long) (inverse * 500);
    }

    // --- Database Integration ---
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mygame?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "password");
    }

    public void loadSettingsFromDatabase() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM settings LIMIT 1")) {

            if (rs.next()) {
                skipUnseenText = rs.getBoolean("skip_unseen");
                textSpeed = rs.getDouble("text_speed");
                autoForwardTime = rs.getDouble("auto_forward");
                rollbackMode = rs.getString("rollback_mode");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSettingsToDatabase() {
        try (Connection conn = connect()) {
            String query = "REPLACE INTO settings (id, skip_unseen, text_speed, auto_forward, rollback_mode) VALUES (1, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setBoolean(1, skipUnseenText);
                stmt.setDouble(2, textSpeed);
                stmt.setDouble(3, autoForwardTime);
                stmt.setString(4, rollbackMode);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

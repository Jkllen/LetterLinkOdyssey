package letterlinkodyssey;

public class SaveEntry {
    private int slotId;
    private String playerName;
    private int currentDialogueIndex;
    private String screenshotPath;

    public SaveEntry(int slotId, String playerName, int currentDialogueIndex, String screenshotPath) {
        this.slotId = slotId;
        this.playerName = playerName;
        this.currentDialogueIndex = currentDialogueIndex;
        this.screenshotPath = screenshotPath;

    }

    public int getSlotId() {
        return slotId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getDialogueIndex() {
        return currentDialogueIndex;
    }
    
    public String getScreenshotPath() {
        return screenshotPath;
    }
}

package letterlinkodyssey;

public class ChoiceEntry {
    private String choiceText;
    private int nextDialogueId;

    public ChoiceEntry(String choiceText, int nextDialogueId) {
        this.choiceText = choiceText;
        this.nextDialogueId = nextDialogueId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public int getNextDialogueId() {
        return nextDialogueId;
    }
}


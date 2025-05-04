package letterlinkodyssey;

public class DialogueEntry {
    private String speaker;
    private String text;
    private String characterImage;
    private String backgroundImage;
    private int id;
    private String type;
    private String sfx;
    private int nextDialogueId;

    public DialogueEntry(int id, String speaker, String text, String characterImage, String backgroundImage, String sfx, String type, int nextDialogueId) {
        this.id = id;
        this.speaker = speaker;
        this.text = text;
        this.characterImage = characterImage;
        this.backgroundImage = backgroundImage;
        this.sfx = sfx;
        this.type = type;
        this.nextDialogueId  = nextDialogueId;
        
    }
    
    public int getId(){
        return id;
    }
    
    public String getType(){
        return type;
    }
    
    public int getNextDialogueId(){
        return nextDialogueId;
    }
    
    public String getSpeaker() { 
        return speaker; 
    }
    public String getText() { 
        return text; 
    }
    
    public String getSfx() {
        return sfx;
    }

    public void setSfx(String sfx) {
        this.sfx = sfx;
    }

    
    public String getCharacterImg() { 
        return characterImage; 
    }
    public String getBackgroundImg() { 
        return backgroundImage; 
    }
}

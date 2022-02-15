package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    private int id;
    private String name;
    private String note;
    private ArrayList<PlayerGroup> playerGroupList;
    

    public Group() {
        super();
        playerGroupList = new ArrayList<PlayerGroup>();
    }

    public Group(int id, String name, String note, ArrayList<PlayerGroup> playerGroupList) {
        super();
        this.id = id;
        this.name = name;
        this.note = note;
        this.playerGroupList = playerGroupList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<PlayerGroup> getPlayerGroupList() {
        return playerGroupList;
    }

    public void setPlayerGroupList(ArrayList<PlayerGroup> playerGroupList) {
        this.playerGroupList = playerGroupList;
    }
    
}

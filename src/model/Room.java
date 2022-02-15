/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author fake1
 */
public class Room implements Serializable{
    private static final long serialVersionUID = 411363153754540L;
    public static final int WAITING =1;
    public static final int PLAYING =2;
    public static final int TURN_SECS = 3;  
    
    private int id;
    private int matchId;
    private int status;
    private ArrayList<PlayingPlayer> playingplayerList;
    private ArrayList<String> drawpile;
    private ArrayList<String> discardpile;
    private int runner;		// -1 +1 reverse or not
    private ArrayList<Turn> turnList;
    private Turn currentTurn;
    private PlayingPlayer winner;
    
    public Room() {
        this.status = RoomOld.WAITING;
        this.playingplayerList = new ArrayList<PlayingPlayer>();
        this.drawpile = new ArrayList<String>();
        this.discardpile = new ArrayList<String>();
        this.runner = 1;
        this.turnList = new ArrayList<Turn>();
        this.matchId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<PlayingPlayer> getPlayingplayerList() {
        return playingplayerList;
    }

    public void setPlayingplayerList(ArrayList<PlayingPlayer> playingplayerList) {
        this.playingplayerList = playingplayerList;
    }

    public ArrayList<String> getDrawpile() {
        return drawpile;
    }

    public void setDrawpile(ArrayList<String> drawpile) {
        this.drawpile = drawpile;
    }

    public ArrayList<String> getDiscardpile() {
        return discardpile;
    }

    public void setDiscardpile(ArrayList<String> discardpile) {
        this.discardpile = discardpile;
    }

    public int getRunner() {
        return runner;
    }

    public void setRunner(int runner) {
        this.runner = runner;
    }

    public ArrayList<Turn> getTurnList() {
        return turnList;
    }

    public void setTurnList(ArrayList<Turn> turnList) {
        this.turnList = turnList;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public PlayingPlayer getWinner() {
        return winner;
    }

    public void setWinner(PlayingPlayer winner) {
        this.winner = winner;
    }
    
    public void addPlayingPlayer(PlayingPlayer pp){
        this.playingplayerList.add(pp);
    }
    
    public void removePlayingPlayer(int ppid){
        for (PlayingPlayer p: this.playingplayerList){
            if (p.getPlayer().getId()==ppid){
                this.playingplayerList.remove(p);
                break;
            }
        }
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
    
    
    
}

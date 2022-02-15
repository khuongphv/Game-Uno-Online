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
public class MatchHistoryDetail implements Serializable{
    private ArrayList<String> playerList;
    private ArrayList<Turn> turnList;

    public MatchHistoryDetail() {
    }

    public MatchHistoryDetail(ArrayList<String> playerList, ArrayList<Turn> turnList) {
        this.playerList = playerList;
        this.turnList = turnList;
    }

    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<String> playerList) {
        this.playerList = playerList;
    }

    public ArrayList<Turn> getTurnList() {
        return turnList;
    }

    public void setTurnList(ArrayList<Turn> turnList) {
        this.turnList = turnList;
    }

}

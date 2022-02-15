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
 * @author NTK
 */
public class PlayingPlayer  implements Serializable{
    private static final long serialVersionUID = 411365453754540L;
    public static final int PLAYING =0;
    public static final int WIN =1;
    public static final int LOSE =2;
    
    private Player player;
    private ArrayList<String> listcard;
    public PlayingPlayer() {
            super();
            listcard = new ArrayList<String>();
            // TODO Auto-generated constructor stub
    }
    public PlayingPlayer( Player player, ArrayList<String> listcard) {
            super();
            this.player = player;
            this.listcard = listcard;
    }
    public Player getPlayer() {
            return player;
    }
    public void setPlayer(Player player) {
            this.player = player;
    }
    public ArrayList<String> getListcard() {
            return listcard;
    }
    public void setListcard(ArrayList<String> listcard) {
            this.listcard = listcard;
    }

    public void addCard(String card){
        listcard.add(card);
    }

    public void removeCard(int index){
        listcard.remove(index);
    }
        
    
    
	
}

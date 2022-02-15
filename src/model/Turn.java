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
public class Turn implements Serializable{
    private PlayingPlayer pp;
    private int pplindex;
    private String discard;

    public Turn() {
    }
    
    public Turn(PlayingPlayer ppl, int pplindex) {
        this.pp = ppl;
        this.pplindex = pplindex;
    }

    public Turn(PlayingPlayer pp, int pplindex, String discard) {
        this.pp = pp;
        this.pplindex = pplindex;
        this.discard = discard;
    }

    
    public PlayingPlayer getPp() {
        return pp;
    }

    public void setPp(PlayingPlayer pp) {
        this.pp = pp;
    }

    public int getPplindex() {
        return pplindex;
    }

    public void setPplindex(int pplindex) {
        this.pplindex = pplindex;
    }

    public String getDiscard() {
        return discard;
    }

    public void setDiscard(String discard) {
        this.discard = discard;
    }
    
    public String getName() {
        return this.pp.getPlayer().getUsername();
    }
    
}

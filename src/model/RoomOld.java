/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author fake1
 */
public class RoomOld implements Serializable{
    private static final long serialVersionUID = 411363153754540L;
    public static final int WAITING =1;
    public static final int PLAYING =2;
    public static final int TURN_SECS = 3;  
    
    private int id;
    private int status;
    private ArrayList<PlayingPlayer> playingplayerList;
    private ArrayList<String> drawpile;
    private ArrayList<String> discardpile;
    private int runner;		// -1 +1 reverse or not
    private int currentplayer;		// index of player

    public RoomOld() {
        this.status = RoomOld.WAITING;
        this.playingplayerList = new ArrayList<PlayingPlayer>();
        this.drawpile = new ArrayList<String>();
        this.discardpile = new ArrayList<String>();
        this.runner = 1;
        this.currentplayer = 0;
  
    }

    public RoomOld(int id, int status, ArrayList<PlayingPlayer> playingplayerList, ArrayList<String> drawpile, ArrayList<String> discardpile, int runner, int currentplayer) {
        this.id = id;
        this.status = status;
        this.playingplayerList = playingplayerList;
        this.drawpile = drawpile;
        this.discardpile = discardpile;
        this.runner = runner;
        this.currentplayer = currentplayer;
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

    public int getCurrentplayer() {
        return currentplayer;
    }

    public void setCurrentplayer(int currentplayer) {
        this.currentplayer = currentplayer;
    }
    
    public PlayingPlayer getCurrentPlayerInfo(){
        return this.playingplayerList.get(this.currentplayer);
    }
    
    public void setCurrentPlayerInfo(PlayingPlayer pp){
        this.playingplayerList.set(this.currentplayer, pp );
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
    
    public void starter(){
        // khởi tạo bộ bài mới
        this.status = PLAYING;
    
        this.drawpile = new ArrayList(Arrays.asList(UnoPack.pack));
        // xáo bài
        Collections.shuffle(this.drawpile);

        // chia mỗi người 7 lá
        for (int i=0; i<7; i++){
            for (PlayingPlayer pp: this.playingplayerList){
                pp.addCard(this.drawpile.get(0));
                this.drawpile.remove(0);
            }
        }
        
        // bốc 1 lá không chức năng và bỏ vào chồng bài bỏ
        for (String c: this.drawpile){
            if (c.charAt(2) =='0'){
                this.discardpile.add(c);
                this.drawpile.remove(c);
                break;
            }
        }
        
    }
    
    public void drawCard(){
        String card;
        // nếu chồng bài bốc đã hết thì xáo trồng bài bỏ và cho vào chồng bài bốc, để lại lá cuối cùng (là lá vừa đánh) 
        if (this.drawpile.isEmpty()){
            String c = this.discardpile.get( this.discardpile.size()-1);
            this.discardpile.remove(this.discardpile.size()-1);
            Collections.shuffle(this.discardpile);
            this.drawpile.addAll(this.discardpile);
            this.discardpile = new ArrayList<String>();
            this.discardpile.add(c);
        }
        
        // bốc một lá bài trên cùng
        card= this.drawpile.get(0);
        this.drawpile.remove(0);
        
        this.playingplayerList.get(this.currentplayer).addCard(card);
        
    }
    
    public boolean discardCard(int cardindex){
        boolean victory = false;
        
        PlayingPlayer pp = getCurrentPlayerInfo();
        String card = pp.getListcard().get(cardindex);
        pp.removeCard(cardindex);
        if (pp.getListcard().size()==0){
            victory = true;
            return victory;
        }
        
        
        setCurrentPlayerInfo(pp);
        
        newPlayerTurn();
        switch (card.charAt(2)){
            case '0': //    none ability            
                break;

            case '1': //    skip
                newPlayerTurn();
                break;
                
            case '2': //    reverse
                this.runner = this.runner*(-1);
                break;
                
            case '3': //    +2
                drawCard();
                drawCard();
                break;
            
            case '4': //    wild card
                break;
                
            case '5': //    +4
                for (int i=0; i<4; i++)
                    drawCard();
                break;
            
        }
        this.discardpile.add(card);
        
        return victory;
    }
    
    public void addPPCard(int playerid, String card){
        for (PlayingPlayer pp: this.playingplayerList)
            if (pp.getPlayer().getId()== playerid){
                pp.addCard(card);
            }
    }
   
    public void newPlayerTurn(){
        this.currentplayer = (this.currentplayer + this.runner + this.playingplayerList.size()) %this.playingplayerList.size();
    }
    
    public boolean checkPlayable(String card){
        if (this.discardpile.isEmpty() == false){
        String dcard = this.discardpile.get(this.discardpile.size()-1);
        if (card.charAt(2)=='4' || card.charAt(2)=='5' || dcard.charAt(2)=='4' || dcard.charAt(2)=='5' 
                    || card.charAt(0)==dcard.charAt(0) || (card.charAt(1)==dcard.charAt(1) && card.charAt(2)==dcard.charAt(2)) ){
            return true;
        }
        }
        return false;
    }
    
    public boolean autoPlay(){
        for (int i=0; i< getCurrentPlayerInfo().getListcard().size(); i++){
            if (checkPlayable(getCurrentPlayerInfo().getListcard().get(i))){
                return discardCard(i);
                
            }
        }
        
        while (true){
            drawCard();
            if (checkPlayable( getCurrentPlayerInfo().getListcard().get( getCurrentPlayerInfo().getListcard().size()-1 ) )){
                return discardCard( getCurrentPlayerInfo().getListcard().size()-1 );
                
            }
        }
        
    }
    
    public void endRoom(){
        this.status = RoomOld.WAITING;
        this.drawpile = new ArrayList<String>();
        this.discardpile = new ArrayList<String>();
        this.runner = 1;
        this.currentplayer = 0;
        for (PlayingPlayer pp: this.playingplayerList)
            pp.setListcard(new ArrayList<String>());
        Iterator itr = this.playingplayerList.iterator();
        while (itr.hasNext()){
            PlayingPlayer pp = (PlayingPlayer)itr.next();
            if (pp.getPlayer().getStatus() == Player.OFFLINE) itr.remove();
        }
        System.out.println(this.getPlayingplayerList().size());
    }
    
}

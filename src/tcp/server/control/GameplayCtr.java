/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp.server.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import model.Player;
import model.PlayingPlayer;
import static model.RoomOld.PLAYING;
import model.Room;
import model.Turn;
import model.UnoPack;

/**
 *
 * @author fake1
 */
public class GameplayCtr {

    public GameplayCtr() {
    }
    
    public void startGame(Room room){
        room.setStatus(Room.PLAYING);
        
        ArrayList<String> drawpile = new ArrayList(Arrays.asList(UnoPack.pack));
        Collections.shuffle(drawpile);
        
        ArrayList<PlayingPlayer> ppl = room.getPlayingplayerList();
        for (PlayingPlayer pp: ppl)
            pp.setListcard(new ArrayList<String>());
        for (int i=0; i<7; i++){
            for (PlayingPlayer pp: ppl){
                pp.addCard(drawpile.get(0));
                drawpile.remove(0);
            }
        }
        
        ArrayList<String> discardpile = new ArrayList<String>();
        for (String c: drawpile){
            if (c.charAt(2) =='0'){
                discardpile.add(c);
                drawpile.remove(c);
                break;
            }
        }
        
        room.setDrawpile(drawpile);
        room.setPlayingplayerList(ppl);
        room.setDiscardpile(discardpile);
        room.setCurrentTurn(new Turn(ppl.get(0),0));
        
    }
    
    public void drawCard(Room room){
        String card;
        ArrayList<String> drawpile = room.getDrawpile();
        ArrayList<String> discardpile = room.getDiscardpile();
        ArrayList<PlayingPlayer> ppl = room.getPlayingplayerList();
        
        // nếu chồng bài bốc đã hết thì xáo trồng bài bỏ và cho vào chồng bài bốc, để lại lá cuối cùng (là lá vừa đánh) 
        if (drawpile.isEmpty()){
            String c = discardpile.get( discardpile.size()-1);
            discardpile.remove(discardpile.size()-1);
            Collections.shuffle(discardpile);
            drawpile.addAll(discardpile);
            discardpile = new ArrayList<String>();
            discardpile.add(c);
        }
        
        // bốc một lá bài trên cùng
        card= drawpile.get(0);
        drawpile.remove(0);
        
        
        int i = room.getCurrentTurn().getPplindex();
        ppl.get(i).addCard(card);
        
        room.setDrawpile(drawpile);
        room.setPlayingplayerList(ppl);
        room.setDiscardpile(discardpile);
    }
    
    
    public boolean discardCard(int cardindex, Room room){
        boolean victory = false;
        
        ArrayList<PlayingPlayer> ppl = room.getPlayingplayerList();
        Turn t = room.getCurrentTurn();
        int i = t.getPplindex();
        
        PlayingPlayer pp = ppl.get(i);
        String card = pp.getListcard().get(cardindex);
        pp.removeCard(cardindex);
        if (pp.getListcard().size()==0){
            victory = true;
            room.setWinner(pp);
            return victory;
        }
        t.setDiscard(card);
        room.setCurrentTurn(t);
        
        ppl.set(i, pp);
        room.setPlayingplayerList(ppl);
       
        newPlayerTurn(room);
        switch (card.charAt(2)){
            case '0': //    none ability            
                break;

            case '1': //    skip
                newPlayerTurn(room);
                break;
                
            case '2': //    reverse
                room.setRunner(room.getRunner()*(-1));
                break;
                
            case '3': //    +2
                drawCard(room);
                drawCard(room);
                break;
            
            case '4': //    wild card
                break;
                
            case '5': //    +4
                for (int j=0; j<4; j++)
                    drawCard(room);
                break;
            
        }
        ArrayList<String> discardpile = room.getDiscardpile();
        discardpile.add(card);
        room.setDiscardpile(discardpile);
     
        return victory;
    }
//    
//    public void addPPCard(int playerid, String card){
//        for (PlayingPlayer pp: this.playingplayerList)
//            if (pp.getPlayer().getId()== playerid){
//                pp.addCard(card);
//            }
//    }
//   
    public void newPlayerTurn(Room room){
        if (room.getCurrentTurn().getDiscard() != null){
            ArrayList<Turn> turnList = room.getTurnList();
            turnList.add(room.getCurrentTurn());
            room.setTurnList(turnList);
        }
        int i = room.getCurrentTurn().getPplindex(), n = room.getPlayingplayerList().size();
        i = (i+ room.getRunner() + n)%n;
        new Turn(room.getPlayingplayerList().get(i), i);
        room.setCurrentTurn(new Turn(room.getPlayingplayerList().get(i), i));
    }

    public boolean checkPlayable(String card, Room room){
        ArrayList<String> discardpile = room.getDiscardpile();
        if (discardpile.isEmpty() == false){
        String dcard = discardpile.get(discardpile.size()-1);
        if (card.charAt(2)=='4' || card.charAt(2)=='5' || dcard.charAt(2)=='4' || dcard.charAt(2)=='5' 
                    || card.charAt(0)==dcard.charAt(0) || (card.charAt(1)==dcard.charAt(1) && card.charAt(2)==dcard.charAt(2)) )
            {
                return true;
            }
        }
        return false;
    }

    public boolean autoPlay(Room room){
        Turn t = room.getCurrentTurn();
        int i = t.getPplindex();
        PlayingPlayer pp = room.getPlayingplayerList().get(i);
        for (int j=0; j< pp.getListcard().size(); j++){
            if (checkPlayable(pp.getListcard().get(j),room)){
                return discardCard(j, room);
            }
        }
        
        while (true){
            drawCard(room);
            int j = pp.getListcard().size()-1;
            if (checkPlayable( pp.getListcard().get(j), room )){
                return discardCard(j, room);
            }
        }
        
    }
    
//    public void endGame(Room room){
//        Room r = new Room();
//        ArrayList<PlayingPlayer> ppl = room.getPlayingplayerList();
//        for (PlayingPlayer pp: ppl)
//            pp.setListcard(new ArrayList<String>());
////        this.status = RoomOld.WAITING;
////        this.drawpile = new ArrayList<String>();
////        this.discardpile = new ArrayList<String>();
////        this.runner = 1;
////        this.currentplayer = 0;
////        for (PlayingPlayer pp: this.playingplayerList)
////            pp.setListcard(new ArrayList<String>());
//    }
    
}

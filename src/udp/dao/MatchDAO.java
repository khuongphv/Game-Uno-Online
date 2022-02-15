/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp.dao;

import static udp.dao.DAO.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.MatchHistory;
import model.MatchHistoryDetail;
import model.Player;
import model.PlayingPlayer;
import model.Room;
import model.RoomOld;
import model.Turn;

/**
 *
 * @author fake1
 */
public class MatchDAO extends DAO{
    
    public int getWinMatch(int playerid){
	String sql = "SELECT COUNT(playerid) FROM tblpp WHERE playerid=? AND status=?";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playerid);
            ps.setInt(2, PlayingPlayer.WIN);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
	            return rs.getInt("COUNT(playerid)");
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
    	return 0;
    }
    
    public int getTotalMatch(int playerid){
        String sql = "SELECT COUNT(playerid) FROM tblpp WHERE playerid=? ";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playerid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
	            return rs.getInt("COUNT(playerid)");
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
    	return 0;
    }
    
    public boolean startMatch(Room room){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int matchid =0;
        String sql = "INSERT INTO tblmatch(begin) VALUES(?)";
        try {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, date);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    matchid = rs.getInt(1);
                }

        }catch(Exception e) {
                e.printStackTrace();
                return false;
        }
        if (matchid == 0) return false;
        room.setMatchId(matchid);
        
        for (PlayingPlayer pp: room.getPlayingplayerList()){
                sql = "INSERT INTO tblpp(playerid,matchid,status) VALUES(?,?,?)";
                try {
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, pp.getPlayer().getId());
                        ps.setInt(2, matchid);
                        ps.setInt(3, PlayingPlayer.PLAYING);
                        ps.executeUpdate();
                }catch(Exception e) {
                        e.printStackTrace();
                        return false;
                }
            }
        
        return true;
    }
    
    public boolean updateMatch(Room room){
        int mid = room.getMatchId();
        int winnerid = room.getWinner().getPlayer().getId();
        
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String sql = "UPDATE tblmatch SET end=? WHERE id=?";
        try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, date);
                ps.setInt(2, mid);
                ps.executeUpdate();
        }catch(Exception e) {
                e.printStackTrace();
                return false;
        }
        
        for (PlayingPlayer pp: room.getPlayingplayerList())
            if (pp.getPlayer().getId() != winnerid){
        
                sql = "UPDATE tblpp SET status=? WHERE playerid=? AND matchid=?";
                try {
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, PlayingPlayer.LOSE);
                        ps.setInt(2, pp.getPlayer().getId());
                        ps.setInt(3, mid);
                        ps.executeUpdate();
                }catch(Exception e) {
                        e.printStackTrace();
                        return false;
                }
            }
        sql = "UPDATE tblpp SET status=? WHERE playerid=? AND matchid=?";
        try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, PlayingPlayer.WIN);
                ps.setInt(2, winnerid);
                ps.setInt(3, mid);
                ps.executeUpdate();
        }catch(Exception e) {
                e.printStackTrace();
                return false;
        }
        
        // lưu nước đi
        ArrayList<Turn> turnList = room.getTurnList();
        for (int i=0; i< turnList.size(); i++){
            sql = "INSERT INTO tblturn(matchid,turnid,playerid,card) VALUES(?,?,?,?)";
            try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, mid);
                    ps.setInt(2, i+1);
                    ps.setInt(3, turnList.get(i).getPp().getPlayer().getId());
                    ps.setString(4, turnList.get(i).getDiscard());
                    ps.executeUpdate();
            }catch(Exception e) {
                    e.printStackTrace();
                    return false;
            }
        }

        return true; 
    }
    
    public ArrayList<MatchHistory> getMatchHistory(int playerid){
        ArrayList<MatchHistory> mhl = new ArrayList<MatchHistory>();
        String sql = "SELECT tblpp.matchid, tblpp.status, tblmatch.begin, tblmatch.end FROM tblpp JOIN tblmatch ON tblpp.matchid = tblmatch.id and tblpp.playerid=?";
        try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, playerid);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        int status = rs.getInt("status");
                        int matchid = rs.getInt("matchid");
                        String begin = rs.getString("begin"), end = rs.getString("end");
                        mhl.add(new MatchHistory(matchid, status, begin, end));
                    }
                    
            }catch(Exception e) {
                    e.printStackTrace();
            }
        return mhl;
    }
    
    public MatchHistoryDetail getMatchHistoryDetai(int matchid){
        ArrayList<Turn> tl = new ArrayList<Turn>();
        String sql = "SELECT playerid,card FROM tblturn WHERE matchid = ?";
        try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, matchid);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        int playerid = rs.getInt("playerid");
                        String card = rs.getString("card");
                        PlayingPlayer pp = new PlayingPlayer();
                        pp.setPlayer(new PlayerDAO().getPlayerById(playerid));
                        Turn t = new Turn();
                        t.setPp(pp);
                        t.setDiscard(card);
                        
                        tl.add(t);
                    }
                    
            }catch(Exception e) {
                    e.printStackTrace();
            }
        
        ArrayList<String> pl = new ArrayList<String>();
        sql = "SELECT playerid,status FROM tblpp WHERE matchid = ?";
        try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, matchid);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        int playerid = rs.getInt("playerid");
                        int status = rs.getInt("status");
                        String name = new PlayerDAO().getPlayerById(playerid).getUsername();
                        System.out.println("nạbakbajàbjkầvbjBJK");
                        if (status == PlayingPlayer.WIN){
                            name += " WIN";
                        } else {
                            name += " LOSE";
                        }
                        pl.add(name);
                    }
                    
            }catch(Exception e) {
                    e.printStackTrace();
            }
        
        return new MatchHistoryDetail(pl, tl);
    }
    
}

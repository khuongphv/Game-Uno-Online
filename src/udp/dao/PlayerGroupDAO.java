/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp.dao;

import static udp.dao.DAO.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Group;
import model.PlayerGroup;

/**
 *
 * @author fake1
 */
public class PlayerGroupDAO extends DAO {
    public boolean addPlayerGroup(PlayerGroup pg){
        boolean result = false;
        String sql = "INSERT INTO tblplayergroup(playerid, groupid, role) VALUES(?,?,?)";
        try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, pg.getPlayer().getId());
                ps.setInt(2, pg.getGroup().getId());
                ps.setInt(3, pg.getRole());
                ps.executeUpdate();
                result= true;
        }catch(Exception e) {
                e.printStackTrace();
        }
        return result; 
    }
    
    public boolean editPlayerGroup(){
        
        return true;
    }
    
    public boolean deletePlayerGroup(PlayerGroup pg){
        boolean res = false;
        String sql = "DELETE from tblplayergroup WHERE playerid = ? AND groupid = ? AND role = 2";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pg.getPlayer().getId());
            ps.setInt(2, pg.getGroup().getId());
            ps.executeUpdate();
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public ArrayList<Integer> getGroupIdList(int playerid){
        ArrayList<Integer> groupIdList = new ArrayList<Integer>();
        String sql = "SELECT groupid FROM tblplayergroup WHERE playerid =? AND (role = ? OR role = ?) ";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playerid);
            ps.setInt(2, PlayerGroup.MEM);
            ps.setInt(3, PlayerGroup.MOD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                groupIdList.add(rs.getInt("groupid"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        return groupIdList;
    }
    
    public ArrayList<PlayerGroup> getPlayerGroupList(Group group){
        ArrayList<PlayerGroup> playergroupList = new ArrayList<PlayerGroup>();
        
        String sql = "SELECT* FROM tblplayergroup WHERE groupid =? AND (role = ? OR role = ?)";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, group.getId());
            ps.setInt(2, PlayerGroup.MEM);
            ps.setInt(3, PlayerGroup.MOD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PlayerGroup playergroup = new PlayerGroup();
//	        playergroup.setGroupId(rs.getInt("groupid"));
                playergroup.setGroup(group);
                playergroup.setRole(rs.getInt("role"));
                playergroup.setPlayer(new PlayerDAO().getPlayerById(rs.getInt("playerid")));
                playergroupList.add(playergroup);
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        return playergroupList;
    }
    
    public boolean updateRole(PlayerGroup pg){
        boolean result = false;
		String sql= "UPDATE tblplayergroup SET role=? WHERE playerid= ? AND groupid= ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, pg.getRole());	
			ps.setInt(2, pg.getPlayer().getId());
			ps.setInt(3, pg.getGroup().getId());
			ps.executeUpdate();
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
    }
    
    public boolean checkGroupRequest(PlayerGroup pg){
        boolean result = true;   
        String sql= "SELECT * FROM tblplayergroup where playerid = ? AND groupid = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pg.getPlayer().getId());
            ps.setInt(2, pg.getGroup().getId());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                result = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean addGroupRequest(PlayerGroup pg){
        boolean result = false;   
        String sql= "INSERT INTO tblplayergroup(playerid,groupid,role) VALUES(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pg.getPlayer().getId());
            ps.setInt(2, pg.getGroup().getId());
            ps.setInt(3, 3);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean acceptGroupRequest(PlayerGroup pg){
         boolean result = false;   
        String sql= "UPDATE  tblplayergroup SET role = 2 WHERE playerid = ? AND groupid = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pg.getPlayer().getId());
            ps.setInt(2, pg.getGroup().getId());
            ps.executeUpdate();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;   
    }
    
    public boolean rejectGroupRequest(PlayerGroup pg){
        boolean result = false;   
        String sql= "DELETE from  tblplayergroup  WHERE playerid = ? AND groupid = ? AND role = 3";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pg.getPlayer().getId());
            ps.setInt(2, pg.getGroup().getId());
            ps.executeUpdate();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
}

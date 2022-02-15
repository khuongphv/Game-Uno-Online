/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.Group;

/**
 *
 * @author fake1
 */
public class GroupDAO extends DAO {
    public int add(Group gr){
        int groupid=0;
        String check = "SELECT * FROM tblgroup WHERE name = ?";
        try {
            PreparedStatement ps = con.prepareStatement(check);
            ps.setString(1, gr.getName());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "Insert INTO tblgroup(name,note) values(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, gr.getName());
            ps.setString(2, gr.getNote());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                groupid = rs.getInt(1);
            }
            return groupid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public void edit(){
        
    }
    
    public void delete(){
        
    }
    
    public Group getGroupByName(String key){
        Group group = new Group();
        String sql = "SELECT * FROM tblgroup WHERE name =?";
         try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, key);
        
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setNote(rs.getString("note"));
                int id = group.getId();
                group.setPlayerGroupList(new PlayerGroupDAO().getPlayerGroupList(group));
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        return group;
    }
    
    public ArrayList<Group> searchGroupbyName(String key){
        ArrayList<Group> grouplist = new ArrayList<Group>();
        String sql = "SELECT * FROM tblgroup WHERE name LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ key+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Group gr = new Group();
                gr.setId(rs.getInt("id"));
                gr.setName(rs.getString("name"));
                gr.setNote(rs.getString("note"));
                grouplist.add(gr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grouplist;
    }
    
    public ArrayList<Group> getGroupPlayerNotInList(int playerid){
        ArrayList<Group> groupList = new ArrayList<Group>();
        String sql = "SELECT * FROM tblgroup Where id not in (select groupid from uno.tblplayergroup where playerid = ?);";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playerid);
        
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setNote(rs.getString("note"));
                //group.setPlayerGroupList(new PlayerGroupDAO().getPlayerGroupList(id));
                groupList.add(group);
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        return groupList;
    }
    
    public Group getGroupById(int id){
        Group group = new Group();
        String sql = "SELECT* FROM tblgroup WHERE id =?";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
        
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setNote(rs.getString("note"));
                group.setPlayerGroupList(new PlayerGroupDAO().getPlayerGroupList(group));
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        return group;
    }
    
}

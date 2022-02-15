package udp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Group;

import model.Player;
import model.PlayerGroup;

public class PlayerDAO extends DAO{

	public boolean checkLogin(Player player) {
		boolean result = false;
		String sql = "SELECT  * FROM tblplayer WHERE username = ? AND password = ?";
		
		try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getPassword());
             
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                player.setId(rs.getInt("id"));
                player.setNickname(rs.getString("nickname"));
                player.setWinmatch(new MatchDAO().getWinMatch(player.getId()));
                player.setTotalmatch(new MatchDAO().getTotalMatch(player.getId()));
                player.setFriendList(getFriendlist(player.getId()));
                player.setFriendRequestList(getRequestList(player.getId()));
                player.setRequestGroupList(getGroupRequestList(player.getId()));
                player.setGroupList(getGroupList(player.getId()));
                player.setStatus(Player.ONLINE);                
                result = true;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
	}
	
	public Player getPlayerById(int id) {
    	Player player = new Player();
		String sql = "SELECT* FROM tblplayer WHERE id =?";
        
        try{            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
        
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
	            player.setId(rs.getInt("id"));
	            player.setUsername(rs.getString("username"));
//	            player.setPassword(rs.getString("password")); // cân nhắc bỏ dòng này
	            player.setNickname(rs.getString("nickname"));            
	            player.setWinmatch(new MatchDAO().getWinMatch(player.getId()));
                player.setTotalmatch(new MatchDAO().getTotalMatch(player.getId()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
    	return player;
    }
	
	public boolean addPlayer(Player player) {
		boolean result= false;
		// kiểm tra username đã tồn tại chưa
		String sql = "SELECT id FROM tblplayer WHERE username = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, player.getUsername());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return false;
			} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		sql = "INSERT INTO tblplayer(username, password, nickname) VALUES(?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, player.getUsername());
			ps.setString(2, player.getPassword());
			ps.setString(3, player.getNickname());
			ps.executeUpdate();
			result= true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result; 
	}
	
	public boolean editPlayerInfo(Player player) {
		boolean result = false;
		String sql = "UPDATE tblplayer SET password=?, nickname=? WHERE id=?";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, player.getPassword());
			ps.setString(2, player.getNickname());
			ps.setInt(3, player.getId());
			ps.executeUpdate();
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// tìm danh sách bạn bè
	public ArrayList<Player> getFriendlist(int id){
		ArrayList<Player> friendlist = new ArrayList<Player>();
		String sql= "SELECT playerid1,playerid2 FROM tblfriendship WHERE (playerid1=? OR playerid2=?) AND status ='befriend'";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id1= rs.getInt("playerid1");
				int id2= rs.getInt("playerid2");
				if (id ==id1) {
					Player player = new PlayerDAO().getPlayerById(id2);
					friendlist.add(player);
				} else {
					Player player = new PlayerDAO().getPlayerById(id1);
					friendlist.add(player);
				}			
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return friendlist;
	}

	// lấy danh sách lời mời kết bạn
	public ArrayList<Player> getRequestList(int id){
		ArrayList<Player> invitelist = new ArrayList<Player>();
		String sql= "SELECT playerid1 FROM tblfriendship WHERE playerid2=? AND status ='request'";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Player player = new PlayerDAO().getPlayerById(rs.getInt("playerid1"));
				invitelist.add(player);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return invitelist;
	}
	
        public ArrayList<Group> getGroupList(int id){
            ArrayList<Group> groupList = new ArrayList<Group>();
            ArrayList<Integer> groupIdList = new PlayerGroupDAO().getGroupIdList(id);
            GroupDAO gd = new GroupDAO();
            for (int groupId: groupIdList){
                groupList.add(gd.getGroupById(groupId));
            }
            
            return groupList;
        }
        
	
        public boolean updateMatch(Player player){
            boolean result = false;
		String sql = "UPDATE tblplayer SET winmatch=?, totalmatch=? WHERE id=?";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, player.getWinmatch());
			ps.setInt(2, player.getTotalmatch());
			ps.setInt(3, player.getId());
			ps.executeUpdate();
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
            
        }
        
        
        
        
    public ArrayList<Player> getPlayerWantJoinGroup(int groupid){
        ArrayList<Player> result = new ArrayList<Player>();
            String sql = "SELECT playerid FROM tblplayergroup WHERE groupid = ? AND role = ?";
		
		try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, groupid);
                    ps.setInt(2, PlayerGroup.WANT_JOIN);
                    
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Player player = getPlayerById(rs.getInt("playerid"));
                        result.add(player);
                    }
			
		}catch(Exception e){
			e.printStackTrace();
		}
            return result;
    } 
    
    public ArrayList<Player> getAllPlayer(){
            ArrayList<Player> res = new ArrayList<Player>();
            String sql = "SELECT * FROM tblplayer";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Player player = new PlayerDAO().getPlayerById(rs.getInt("id"));
//                                System.out.println("Name: "+player.getUsername() + " totalmatch: "+player.getTotalmatch());
                                if(player.getTotalmatch() != 0){
                                    double winmatch = (double)player.getWinmatch();
                                    double totalmatch = (double)player.getTotalmatch();
                                    player.setWinrate((winmatch*100.0/totalmatch));
                                }
                                else{
                                    player.setWinrate(0);
                                }
				res.add(player);
			}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
    }
    
    public ArrayList<Group> getGroupRequestList(int id){
        String sql = "SELECT * FROM tblplayergroup WHERE playerid= ?  AND role = 3";
        ArrayList<Group> grouprequestlist = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int groupid = rs.getInt("groupid");
                Group g = new GroupDAO().getGroupById(groupid);
                grouprequestlist.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grouprequestlist;
    }
    
    
}

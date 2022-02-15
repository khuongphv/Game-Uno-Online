package udp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FriendshipDAO extends DAO {
	// id1 gửi lời mời kết bạn id2
	public boolean addFriendRequest(int id1, int id2) {
		boolean result = false;   
		String sql= "INSERT INTO tblfriendship(playerid1,playerid2,status) VALUES(?,?,?)";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id1);	
			ps.setInt(2, id2);
			ps.setString(3, "request");
			ps.executeUpdate();
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
            return result;
            
	}
		
	// chấp nhận lời mời kết bạn
	public boolean acceptFriendRequest(int id1, int id2) {
		boolean result = false;
		String sql= "UPDATE tblfriendship SET status='befriend' WHERE (playerid1=? AND playerid2=?) OR (playerid1=? AND playerid2=?)";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id1);	
			ps.setInt(2, id2);
			ps.setInt(3, id2);	
			ps.setInt(4, id1);
			ps.executeUpdate();
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	// xóa bạn
	public boolean rejectFriendRequest(int id1, int id2) {
		boolean result = false;
		String sql= "DELETE FROM tblfriendship WHERE (playerid1=? AND playerid2=?) OR (playerid1=? AND playerid2=?) ";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id1);	
			ps.setInt(2, id2);
			ps.setInt(3, id2);	
			ps.setInt(4, id1);
			ps.executeUpdate();
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
        
        // khi server nhận đc request(id1 rq id2) kiểm tra lại xem request(id2 to id1) có tồn tại không
        public boolean checkRequest(int id1, int id2){
            boolean result = false;
            String sql= "SELECT status FROM tblfriendship WHERE playerid1=? AND playerid2=? ";
            try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id2);
                        ps.setInt(2, id1);
			ResultSet rs = ps.executeQuery();
                        if (rs.next()){
                            result= true;
                        }
		}catch(Exception e) {
			e.printStackTrace();
		}
            return result;
        }
}

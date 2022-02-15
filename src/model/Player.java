package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class Player implements Serializable {
	private static final long serialVersionUID = 4118473963153754540L;
	public static final int ONLINE =1;
	public static final int OFFLINE =2;
	public static final int PLAYING =3;
	
	private int id;
        private String username;
        private String password;
        private String nickname;
        private int winmatch;
	private int totalmatch;
	private int globalpoint;
	private int localpoint;
	private ArrayList<Player> friendList;
	private ArrayList<Player> friendRequestList;
        private ArrayList<Group> groupList; // (groupname, members[])
        private ArrayList<Group> requestGroupList;
        private int status=2; // defaut is offline
	
	

	public Player() {
		super();
		friendList = new ArrayList<Player>();
                friendRequestList = new ArrayList<Player>();
                groupList = new ArrayList<Group>();  
		// TODO Auto-generated constructor stub
	}
	
	public Player(int id, String username, String password, String nickname, int winmatch, int totalmatch,
			int globalpoint, int localpoint, ArrayList<Player> listfriend, ArrayList<Player> requestList,ArrayList<Group> groupList, int status) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.winmatch = winmatch;
		this.totalmatch = totalmatch;
		this.globalpoint = globalpoint;
		this.localpoint = localpoint;
		this.friendList = listfriend;
		this.friendRequestList = requestList;
                this.groupList = groupList;
                this.status = status;
                
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getWinmatch() {
		return winmatch;
	}

	public void setWinmatch(int winmatch) {
		this.winmatch = winmatch;
	}

	public int getTotalmatch() {
		return totalmatch;
	}

	public void setTotalmatch(int totalmatch) {
		this.totalmatch = totalmatch;
	}

	public int getGlobalpoint() {
		return globalpoint;
	}

	public void setGlobalpoint(int globalpoint) {
		this.globalpoint = globalpoint;
	}

	public int getLocalpoint() {
		return localpoint;
	}

	public void setLocalpoint(int localpoint) {
		this.localpoint = localpoint;
	}

	public ArrayList<Player> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<Player> friendList) {
		this.friendList = friendList;
	}
    
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public ArrayList<Player> getFriendRequestList() {
        return friendRequestList;
    }

    public void setFriendRequestList(ArrayList<Player> friendRequestList) {
        this.friendRequestList = friendRequestList;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }
    
    public boolean searchFriendbyId(int id){
        if (id == this.id) return true;
        for(Player p : friendList){
            if(p.getId() == id){
                return true;
            }
        }
        return false;
    }
    
    public void updateGroupInfo(Group gr){
        for (int i=0; i<this.groupList.size(); i++){
            if (this.groupList.get(i).getId() == gr.getId()){
                this.groupList.set(i, gr);
                return;
            }
        }
        this.groupList.add(gr);
        return;
    }
    
    

    public ArrayList<Group> getRequestGroupList() {
        return requestGroupList;
    }

    public void setRequestGroupList(ArrayList<Group> requestGroupList) {
        this.requestGroupList = requestGroupList;
    }
    
    
    private double winrate = winmatch*100.0 / totalmatch;

    public double getWinrate() {
        return winrate;
    }

    public void setWinrate(double winrate) {
        this.winrate = winrate;
    }
    
    
    public static Comparator<Player> sortbyWinrate= new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                double winrate1 = o1.getWinrate();
                double winrate2 = o2.getWinrate();
                return (int)(winrate2 - winrate1);
            }
    };

     public static Comparator<Player> sortByStatus = new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.getStatus() - o1.getStatus();
            }
     };
     public Object[] toObject(){
         String s;
         if(status == 1){
             s = "Offline";
         }
         else if(status == 2){
             s = "Online";
         }
         else{
             s = "Playing";
         }
         return new Object[]{username,0,s};
     }
     public Object[] toObject_Rank(){
         
         return new Object[]{username,winrate,totalmatch};
     }
}

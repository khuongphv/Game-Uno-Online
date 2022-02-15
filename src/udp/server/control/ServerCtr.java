package udp.server.control;
 
import udp.dao.FriendshipDAO;
import udp.dao.GroupDAO;
import udp.dao.PlayerDAO;
import udp.dao.PlayerGroupDAO;
import udp.dao.MatchDAO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
 

import model.*;
import model.IPAddress;
import model.ObjectWrapper;
import udp.server.view.ServerMainFrm;
 
 
public class ServerCtr {
    private ServerMainFrm view;
    private DatagramSocket myServer;    
    private IPAddress myAddress = new IPAddress("localhost", 5555); //default server address
    private UDPListening myListening;
    private Map<String,Player> playerMap;
     
    public ServerCtr(ServerMainFrm view){
        this.view = view;     
        playerMap = new HashMap<String,Player>();
    }
     
    public ServerCtr(ServerMainFrm view, int port){
        super();
        this.view = view;
        myAddress.setPort(port);
        playerMap = new HashMap<String,Player>();
        
    }
     
     
    public boolean open(){
        try {
            myServer = new DatagramSocket(myAddress.getPort());
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showServerInfo(myAddress);
            myListening = new UDPListening();
            myListening.start();
            view.showMessage("UDP server is running at the host: " + myAddress.getHost() + ", port: " + myAddress.getPort());
        }catch(Exception e) {
            e.printStackTrace();
            view.showMessage("Error to open the datagram socket!");
            return false;
        }
        return true;
    }
     
    public boolean close(){
        try {
            myListening.stop();
            myServer.close();
        }catch(Exception e) {
            e.printStackTrace();
            view.showMessage("Error to close the datagram socket!");
            return false;
        }
        return true;
    }
     
    class UDPListening extends Thread{
        public UDPListening() {
             
        }
         
        public void run() {
            while(true) {               
                try {   
                    //prepare the buffer and fetch the received data into the buffer
                    byte[] receiveData = new byte[16384];
                    DatagramPacket receivePacket = new  DatagramPacket(receiveData, receiveData.length);
                    myServer.receive(receivePacket);
                     
                    //read incoming data from the buffer 
                    ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    ObjectWrapper receivedData = (ObjectWrapper)ois.readObject();
                     
                    //processing
                    ObjectWrapper resultData = new ObjectWrapper();
                    switch(receivedData.getPerformative()) {
                        
                        
                        case ObjectWrapper.LOGIN_PLAYER:
                            Player player = (Player)receivedData.getData();
                            PlayerDAO pd = new PlayerDAO();
	                    view.showMessage("login :"+player.getUsername()+" "+player.getPassword()+" ");
	                    resultData.setPerformative(ObjectWrapper.REPLY_LOGIN_PLAYER);
                            if( pd.checkLogin(player) ){
	                        playerMap.put(player.getUsername(), player );  
                                resultData.setData(player);
	                    } else {
                                resultData.setData("error");
                            }	
                            break;
                        
                        case ObjectWrapper.REGISTER_PLAYER:
                            player = (Player)receivedData.getData();
                            pd = new PlayerDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_REGISTER_PLAYER);
                            if (pd.addPlayer(player)) {
                                resultData.setData("ok");
                                view.showMessage("register "+player.getUsername()+" "+player.getPassword()+" ok");
                            }
                            else {
                                resultData.setData("false");
                                view.showMessage("register "+player.getUsername()+" "+player.getPassword() +" false");
                            }
                            break;
                            
                        case ObjectWrapper.GET_PLAYER_INFO:
                            int id = (Integer)receivedData.getData();
                            pd = new PlayerDAO();
                            // get all info
                            Player plr = pd.getPlayerById(id);
                            plr.setFriendList(pd.getFriendlist(id));
                            plr.setFriendRequestList(pd.getRequestList(id));
                            plr.setGroupList(pd.getGroupList(id));
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_PLAYER_INFO);
                            //
                            resultData.setData(plr);
                            
                            break;
                            
                            
                        case ObjectWrapper.FRIEND_REQUEST:
                            Request  rq = (Request)receivedData.getData();
                            resultData.setPerformative(ObjectWrapper.REPLY_FRIEND_REQUEST);
                            FriendshipDAO fd = new FriendshipDAO();
                                if (fd.addFriendRequest(rq.getSendId(),rq.getReceiveID())){
                                    resultData.setData("ok");
                                } else {
                                    resultData.setData("false");
                                }

                            break;

                        case ObjectWrapper.ACCEPT_FRIEND_REQUEST:
                            view.showMessage("ACCEPT_FRIEND_REQUEST");         
                            rq = (Request)receivedData.getData();
                            fd = new FriendshipDAO();
                            pd = new PlayerDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_ACCEPT_FRIEND);
                            if (fd.acceptFriendRequest(rq.getSendId(), rq.getReceiveID())){
                                plr = pd.getPlayerById(rq.getSendId());
                                resultData.setData((plr));
                            } else {
                                resultData.setData("false");
                            }
                            break;

                        case ObjectWrapper.REJECT_FRIEND_REQUEST:
                            rq = (Request)receivedData.getData();
                            fd = new FriendshipDAO();
                            pd = new PlayerDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_REJECT_FRIEND_REQUEST);
                            if (fd.rejectFriendRequest(rq.getSendId(), rq.getReceiveID())){
                                plr = pd.getPlayerById(rq.getSendId());
                                resultData.setData((plr));
                            } else {
                                resultData.setData("false");
                            }
                            break;    
                            
                        case ObjectWrapper.GET_RANK:
                            pd = new PlayerDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_RANK);
                            resultData.setData(pd.getAllPlayer());
                            break;   
                        
                        case ObjectWrapper.GET_GROUP_PLAYER_NOT_IN:
                            int playerid = (Integer)receivedData.getData();
                            GroupDAO gd = new GroupDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_GROUP_PLAYER_NOT_IN);
                            resultData.setData(gd.getGroupPlayerNotInList(playerid));
                            break;
                            
                        case ObjectWrapper.JOIN_GROUP:
                            PlayerGroup pg = (PlayerGroup)receivedData.getData();
                            resultData.setPerformative(ObjectWrapper.REPLY_JOIN_GROUP);
                            PlayerGroupDAO pgd = new PlayerGroupDAO();
//                            if (pgd.addPlayerGroup(rq.getSendId(), rq.getReceiveID(), PlayerGroup.WANT_JOIN)){
                            if (pgd.addPlayerGroup(pg)){    
                                resultData.setData("ok");
                            } else {
                                resultData.setData("false");
                            }
                               
                            break;
                        
                        case ObjectWrapper.GET_PLAYERS_WANT_JOIN_GROUP:
                            int groupid = (Integer)receivedData.getData();
                            pd = new PlayerDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_PLAYERS_WANT_JOIN_GROUP);
                            resultData.setData(pd.getPlayerWantJoinGroup(groupid));
                            break;
                            
                        case ObjectWrapper.ACCEPT_JOIN_REQUEST:
                            pg = (PlayerGroup)receivedData.getData();
                            pgd = new PlayerGroupDAO();
                            gd = new GroupDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_ACCEPT_JOIN_REQUEST);
                            if (pgd.updateRole(pg)){
                                
                                resultData.setData(gd.getGroupById(pg.getGroup().getId()));
                            } else {
                                resultData.setData("false");
                            }
                            break;
                        
                        case ObjectWrapper.ACCEPT_GROUP_REQUEST:
                        PlayerGroup plg = (PlayerGroup) receivedData.getData();
                        System.out.println("ACCEPT GR RE");
                        resultData.setPerformative(ObjectWrapper.REPLY_ACCPET_GROUP_REQUEST);
                        if (new PlayerGroupDAO().acceptGroupRequest(plg)){
                            Group group = new GroupDAO().getGroupById(plg.getGroup().getId());
                            resultData.setData(group);
                        } else {
                            resultData.setData("false");
                        }
                        
                        break;
                        
                        case ObjectWrapper.REJECT_GROUP_REQUEST:
                            plg = (PlayerGroup) receivedData.getData();
                           System.out.println("REJECT GR RE");
                           resultData.setPerformative(ObjectWrapper.REPLY_REJECT_GROUP_REQUEST);
                           if(new PlayerGroupDAO().rejectGroupRequest(plg)){
                               resultData.setData(plg.getGroup());
                           }
                           else
                               resultData.setData("false");
                           break;

                        case ObjectWrapper.GROUP_REQUEST:
                            plg = (PlayerGroup) receivedData.getData();
                            System.out.println("GROUP REQUEST");
                            resultData.setPerformative(ObjectWrapper.REPLY_GROUP_REQUEST);
                            if(new PlayerGroupDAO().checkGroupRequest(plg)){
                                boolean res = new PlayerGroupDAO().addGroupRequest(plg);
                                resultData.setData(plg);     
                            }else{
                                resultData.setData("false");  
                            }

                            break;
                        case ObjectWrapper.SEARCH_GROUP:
                            String key = (String) receivedData.getData();
                            resultData.setPerformative(ObjectWrapper.REPLY_SEARCH_GROUP);
                            resultData.setData(new GroupDAO().searchGroupbyName(key));
                            break;
                        case ObjectWrapper.GET_GROUP:
                            Group group = (Group) receivedData.getData();
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_GROUP);
                            group.setPlayerGroupList(new GroupDAO().getGroupById(group.getId()).getPlayerGroupList());
                            resultData.setData(group);
                            System.out.println("GET GROUP");
                            //System.out.println(group.getPlayerGroupList().size());
                            break;
                        case ObjectWrapper.GET_GROUP_BY_ID:
                            id  = (int) receivedData.getData();
                            group = new GroupDAO().getGroupById(id);
                            resultData.setData(group);
                            System.out.println("GET GROUP By id");
                            break;
                        
                        
                        
                        
                        case ObjectWrapper.CREATE_GROUP:
                            
//                            Group g = (Group)receivedData.getData();
                            gd = new GroupDAO();
//                            g.setId(gd.add(g));
                            pg = (PlayerGroup)receivedData.getData();
                            pgd = new PlayerGroupDAO();
                            resultData.setPerformative(ObjectWrapper.REPLY_CREATE_GROUP);
                            
//                            if (pgd.addPlayerGroup(g.getPlayerGroupList().get(0).getPlayer().getId(), g.getId(),PlayerGroup.MOD)){
                            if (pgd.addPlayerGroup(pg)){
                                resultData.setData(gd.getGroupById(pg.getGroup().getId()));
                            } else {
                                resultData.setData("false");
                            }

                            break;
                            
                        case ObjectWrapper.START_MATCH:
                            Room r = (Room)receivedData.getData();
                            new MatchDAO().startMatch(r);
                            resultData.setPerformative(ObjectWrapper.REPLY_START_MATCH);
                            resultData.setData(r);
                            break;
                            
                        case ObjectWrapper.UPDATE_MATCH:
                            r = (Room)receivedData.getData();
                            MatchDAO ppd = new MatchDAO();
                            ppd.updateMatch(r);
                            break;
                        
                        case ObjectWrapper.GET_MATCH_HISTORY:
                            playerid = (Integer)receivedData.getData();
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_MATCH_HISTORY);
                            resultData.setData(new MatchDAO().getMatchHistory(playerid));
                            break;
                            
                        case ObjectWrapper.GET_MATCH_HISTORY_DETAIL:
                            int matchid = (Integer)receivedData.getData();
                            resultData.setPerformative(ObjectWrapper.REPLY_GET_MATCH_HISTORY_DETAIL);
                            resultData.setData(new MatchDAO().getMatchHistoryDetai(matchid));
                            break;
                            
                            
                    }
                     
                     
                    //prepare the buffer and write the data to send into the buffer
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(resultData);
                    oos.flush();            
                     
                    //create data package and send
                    byte[] sendData = baos.toByteArray();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    myServer.send(sendPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showMessage("Error when processing an incoming package");
                }    
            }
        }
    }
}
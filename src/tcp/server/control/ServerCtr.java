package tcp.server.control;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Timer;

import model.*;
import tcp.server.view.ServerMainFrm;
import udp.client.control.ClientCtr;
 
public class ServerCtr {
    private ClientCtr udpControl;
    private ServerMainFrm view;
    private ServerSocket myServer;
    private ServerListening myListening;
    private ArrayList<ServerProcessing> myProcess;
    private IPAddress myAddress = new IPAddress("localhost",8888);  //default server host and port
    private Map<Integer,Room> roomMap;
    private int roomMapId;
    
    private Map<String,ServerProcessing> processMap;
    
    
    public ServerCtr(ServerMainFrm view){
        myProcess = new ArrayList<ServerProcessing>();
        processMap = new HashMap<String,ServerProcessing>();
        roomMap = new HashMap<Integer,Room>();
        roomMapId = 0;
        udpControl = new ClientCtr();
        this.view = view;
        openServer();       
    }
     
    public ServerCtr(ServerMainFrm view, int serverPort){
        super();
        this.view = view;
        myAddress.setPort(serverPort);
        openServer();       
    }
     
     
    private void openServer(){
        try {
            myServer = new ServerSocket(myAddress.getPort());
            myListening = new ServerListening();
            myListening.start();
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showServerInfor(myAddress);
            //System.out.println("server started!");
            view.showMessage("TCP server is running at the port " + myAddress.getPort() +"...");
            if(!udpControl.open()){
                System.out.println("Lỗi server");
            }
        }catch(Exception e) {
            e.printStackTrace();;
        }
    }
     
    public void stopServer() {
        try {
            for(ServerProcessing sp:myProcess)
                sp.stop();
            myListening.stop();
            myServer.close();
            view.showMessage("TCP server is stopped!");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
     
    /**
     * The class to announce the client their number  
     */
    public void publicClientNumber() {
        ObjectWrapper data = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_NUMBER, myProcess.size());
        for(ServerProcessing sp : myProcess) {
            sp.sendData(data);
        }
    }
    
    public void sendDataClient(Socket mySocket, ObjectWrapper data) {
    	try {
            ObjectOutputStream oos= new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(data);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * The class to listen the connections from client, avoiding the blocking of accept connection
     *
     */
    class ServerListening extends Thread{
         
        public ServerListening() {
            super();
        }
         
        public void run() {
            view.showMessage("server is listening... ");
            try {
                while(true) {
                    Socket clientSocket = myServer.accept();
                    ServerProcessing sp = new ServerProcessing(clientSocket);
                    sp.start();
                    myProcess.add(sp);
                    view.showMessage("Number of client connecting to the server: " + myProcess.size());
                    publicClientNumber();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * The class to treat the requirement from client
     *
     */
    class ServerProcessing extends Thread{
        private Socket mySocket;
        private Player myPlayer;
        private int roomId,second;

        public ServerProcessing(Socket s) {
            myPlayer = new Player();
            roomId = 0;
            second = 0;
            mySocket = s;
            cdTimer();
        }
         
        public void sendData(Object obj) {
            try {
                ObjectOutputStream oos= new ObjectOutputStream(mySocket.getOutputStream());
                oos.writeObject(obj);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        public ObjectWrapper udpSendData(ObjectWrapper data){
            udpControl.sendData(data);
            return udpControl.receiveData();
        }
        
        public Socket getSocket(){
            return mySocket;
        }

        public void setMySocket(Socket mySocket) {
            this.mySocket = mySocket;
        }
        

        public Player getMyPlayer() {
            return myPlayer;
        }

        public void setMyPlayer(Player myPlayer) {
            this.myPlayer = myPlayer;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }
        
        public void setStatus(){
            for (Player plr : myPlayer.getFriendList())
                if (processMap.containsKey(plr.getUsername()))
                    plr.setStatus(processMap.get(plr.getUsername()).getMyPlayer().getStatus());
            
            for (Group group: myPlayer.getGroupList()){
                for (PlayerGroup pg: group.getPlayerGroupList()){
                    Player plr = pg.getPlayer();
                    if (processMap.containsKey(plr.getUsername()))
                        plr.setStatus(processMap.get(plr.getUsername()).getMyPlayer().getStatus());
                }
            }
        }
        
        public void annouceStatus(int status){
            myPlayer.setStatus(status);
            for (Player plr : myPlayer.getFriendList())
                if (processMap.containsKey(plr.getUsername())
                        && processMap.get(plr.getUsername()).getMyPlayer().getStatus() != Player.OFFLINE) 
                    processMap.get(plr.getUsername()).sendData(new ObjectWrapper(ObjectWrapper.ANNOUNCE_STATUS,myPlayer));
            
            for (Group group: myPlayer.getGroupList()){
                for (PlayerGroup pg: group.getPlayerGroupList())
                if (myPlayer.getId()!= pg.getPlayer().getId())    
                {
                    Player plr = pg.getPlayer();
                    if (processMap.containsKey(plr.getUsername())
                            && processMap.get(plr.getUsername()).getMyPlayer().getStatus() != Player.OFFLINE)
                        processMap.get(plr.getUsername()).sendData(new ObjectWrapper(ObjectWrapper.ANNOUNCE_STATUS,myPlayer));
                }
            }
                
        }
        
        public void annouceRoom(Room room){
            for (PlayingPlayer p: room.getPlayingplayerList()){
                String username = p.getPlayer().getUsername();
                if (processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus()!= Player.OFFLINE)
                    processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.GET_ROOM,room));
            }                               
        }
        
        public void annouceGroup(Group group){
            //Group group = new GroupDAO().getGroupById(playergroup.getGroupid());
            for(PlayerGroup pg : group.getPlayerGroupList()){
                String username = pg.getPlayer().getUsername();
                if(!username.equals(myPlayer.getUsername()) && processMap.containsKey(username)){
                    //System.out.println("245- "+username);
                    sendDataClient(processMap.get(username).getSocket(), new ObjectWrapper(ObjectWrapper.ANNOUCE_GROUP_REQUEST_SUCCESS, group));
                }
            }
        }
        
        public void setSecond(int secs){
            this.second = secs;
        }
        
        public void cdTimer(){
            new Timer(1000, new ActionListener(){
              
                @Override
                public void actionPerformed(ActionEvent e){
                    second--;
                    if (second==0 && roomMap.containsKey(roomId) 
                            && roomMap.get(roomId).getCurrentTurn().getName().equals(myPlayer.getUsername()))
                    {
                        boolean victory = new GameplayCtr().autoPlay(roomMap.get(roomId));
                        Room room = roomMap.get(roomId);
                        for (PlayingPlayer pp: roomMap.get(roomId).getPlayingplayerList()){
                            String username = pp.getPlayer().getUsername();
                            if (processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus() == Player.PLAYING){
                                processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.GET_PLAYING_ROOM,room));
                            }                   
                        }
                        if (victory == true){
                            
                            
                            udpSendData(new ObjectWrapper(ObjectWrapper.UPDATE_MATCH,roomMap.get(roomId)));
                            for (PlayingPlayer pp: roomMap.get(roomId).getPlayingplayerList()){
                                String username = pp.getPlayer().getUsername();
                                pp.getPlayer().setTotalmatch(pp.getPlayer().getTotalmatch()+1);
                                
                                if (username.equals(myPlayer.getUsername())){
                                    pp.getPlayer().setWinmatch(pp.getPlayer().getWinmatch()+1);
                                    
                                }
                                
                                if ( processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus() == Player.PLAYING ){
                                    processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.VICTORY_ROOM,myPlayer.getUsername()));
                                }
                            }   
                            roomMap.get(roomId).setStatus(Room.WAITING);
                            for (PlayingPlayer pp: roomMap.get(roomId).getPlayingplayerList()){
                                String username = pp.getPlayer().getUsername();
                                if ( processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus() != Player.OFFLINE ){
                                    processMap.get(username).annouceRoom(roomMap.get(roomId));
                                    processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.ANNOUCE_PLAYER_INFO,pp.getPlayer()));
                                }
                            }  
                            
                        
                        } else {
                        
                            if (processMap.containsKey(roomMap.get(roomId).getCurrentTurn().getName())){
                                processMap.get(roomMap.get(roomId).getCurrentTurn().getName()).setSecond(RoomOld.TURN_SECS);
                            }
                        
                        }
                    };
                }
                
            } 
            ).start(); 
        }
        
        public void run() { 
            try {
                while(true) {
                    ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
                    Object o = ois.readObject();
                    if(o instanceof ObjectWrapper){
                        ObjectWrapper data = (ObjectWrapper)o;
                        switch(data.getPerformative()) {
                            case ObjectWrapper.LOGIN_PLAYER:
                                System.out.println("LOGIN_PLAYER");
                                ObjectWrapper rdata = udpSendData(data);
                                if (rdata.getData() instanceof Player){
                                    Player plr = (Player) rdata.getData();
                                    if (processMap.containsKey(plr.getUsername())== false){
                                        myPlayer = plr;
                                        processMap.put(myPlayer.getUsername(), this);
                                        
                                        setStatus();
                                        annouceStatus(Player.ONLINE);
                                        
                                        sendData(new ObjectWrapper(ObjectWrapper.REPLY_LOGIN_PLAYER,myPlayer));
                                        
                                    } else {
                                        if (processMap.get(plr.getUsername()).getMyPlayer().getStatus() == Player.OFFLINE
                                                && roomMap.containsKey(processMap.get(plr.getUsername()).getRoomId())
                                                && roomMap.get(processMap.get(plr.getUsername()).getRoomId()).getStatus() == Room.PLAYING)
                                        {
                                            setRoomId(processMap.get(plr.getUsername()).getRoomId());
                                            setMyPlayer(plr);
                                            setStatus();
                                            annouceStatus(Player.PLAYING);
                                            sendData(new ObjectWrapper(ObjectWrapper.REPLY_LOGIN_PLAYER,getMyPlayer()));
                                            sendData(new ObjectWrapper( ObjectWrapper.GET_ROOM,roomMap.get(getRoomId()) ) );
                                            sendData(new ObjectWrapper( ObjectWrapper.REPLY_START_ROOM,"") ); 
                                            processMap.replace(plr.getUsername(), this);
                                        }
                                    }
                                    
                                } else {
                                    sendData(rdata);
                                }
                                break;
                            
                            case ObjectWrapper.REGISTER_PLAYER:
                                System.out.println("REGISTER_PLAYER");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                            
                            case ObjectWrapper.FRIEND_REQUEST:
                                System.out.println("FRIEND_REQUEST");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.ACCEPT_FRIEND_REQUEST:
                                System.out.println("ACCEPT_FRIEND_REQUEST");
                                rdata = udpSendData(data);
                                if (rdata.getData() instanceof Player){
                                    Player plr = (Player) rdata.getData();
                                    if (processMap.containsKey(plr.getUsername())){
                                                plr.setStatus(processMap.get(plr.getUsername()).getMyPlayer().getStatus()); 
                                                
                                                processMap.get(plr.getUsername()).sendData(new ObjectWrapper(ObjectWrapper.ANNOUCE_FRIEND_REQUEST_SUCCESS,myPlayer));
                                            } else {
                                                plr.setStatus(Player.OFFLINE);  
                                            }
                                    sendData(new ObjectWrapper(ObjectWrapper.REPLY_ACCEPT_FRIEND,plr));
                                } else
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.REJECT_FRIEND_REQUEST:
                                System.out.println("REJECT_FRIEND_REQUEST");
                                rdata = udpSendData(data);
                                if (rdata.getData() instanceof Player){
                                    Player plr = (Player) rdata.getData();
                                    sendData(new ObjectWrapper(ObjectWrapper.REPLY_ACCEPT_FRIEND,plr));
                                } else
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.GET_RANK:
                                System.out.println("GET_RANK");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.GET_GROUP_PLAYER_NOT_IN:
                                System.out.println("GET_GROUP_PLAYER_NOT_IN");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.JOIN_GROUP:
                                System.out.println("JOIN_GROUP");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.GET_PLAYERS_WANT_JOIN_GROUP:
                                System.out.println("GET_PLAYERS_WANT_JOIN_GROUP");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.ACCEPT_JOIN_REQUEST:
                                System.out.println("ACCEPT_JOIN_REQUEST");
                                rdata = udpSendData(data);
                                if (rdata.getData() instanceof Group){
                                    Group g = (Group)rdata.getData();
                                    for (PlayerGroup pg :g.getPlayerGroupList()){
                                        
                                        String username = pg.getPlayer().getUsername();
                                        if (processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus()!= Player.OFFLINE){
                                            pg.getPlayer().setStatus(processMap.get(username).getMyPlayer().getStatus());
                                        }
                                    }
                                    for (PlayerGroup pg :g.getPlayerGroupList()){
                                        String username = pg.getPlayer().getUsername();
                                        if (processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus()!= Player.OFFLINE){
                                            processMap.get(username).getMyPlayer().updateGroupInfo(g);
                                            processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.UPDATE_GROUP_INFO,g));
                                        }
                                    }

                                    sendData(new ObjectWrapper(ObjectWrapper.REPLY_ACCEPT_JOIN_REQUEST,"ok"));
                                    
                                } else 
                                sendData(rdata);
                                break;
                                
                            case ObjectWrapper.REJECT_JOIN_REQUEST:
                                System.out.println("REJECT_JOIN_REQUEST");
                                rdata = udpSendData(data);
                                sendData(rdata);
                                break;
                            
                            case ObjectWrapper.CREATE_GROUP:
                                System.out.println("CREATE_GROUP");
                                rdata = udpSendData(data);
                                Group g = (Group)rdata.getData();
                                myPlayer.updateGroupInfo(g);
                                sendData(new ObjectWrapper(ObjectWrapper.UPDATE_GROUP_INFO,g));
                                sendData(rdata);
                                break;  
                            
                            /*
                                *************Kien**********************
                            */

                            case ObjectWrapper.GROUP_REQUEST:
                                ObjectWrapper result = new ObjectWrapper();
                                udpControl.sendData(data);
                                result = udpControl.receiveData();
                                sendData(result);

                                if(result.getData() instanceof PlayerGroup){
                                    PlayerGroup pg = (PlayerGroup) result.getData();
                                    Player player = pg.getPlayer();
                                    if(processMap.containsKey(player.getUsername())){
                                    Socket socket = processMap.get(player.getUsername()).getSocket();
                                    udpControl.sendData(new ObjectWrapper(ObjectWrapper.GET_GROUP_BY_ID, pg.getGroup().getId()));
                                    Group res1= (Group) udpControl.receiveData().getData();

                                    sendDataClient(socket, new ObjectWrapper(ObjectWrapper.ANNOUCE_GROUP_REQUEST, res1));
                                    }
                                }     
                                break;

                            case ObjectWrapper.ACCEPT_GROUP_REQUEST:
                                result = new ObjectWrapper();
                                udpControl.sendData(data);
                                result = udpControl.receiveData();

                                sendData(result);
                                if(result.getData() instanceof Group){
                                    annouceGroup((Group) result.getData());
                                }
                                break;
                            case ObjectWrapper.REJECT_GROUP_REQUEST:
                                 result = new ObjectWrapper();
                                udpControl.sendData(data);
                                result = udpControl.receiveData();
                                sendData(result);
                                break;
                            case ObjectWrapper.GET_GROUP_MEMBER:
                                result = new ObjectWrapper();
                                udpControl.sendData(data);
                                result = udpControl.receiveData();
                                ArrayList<PlayerGroup> member = (ArrayList<PlayerGroup>) result.getData();
                                for(PlayerGroup i : member){
                                    if(processMap.containsKey(i.getPlayer().getUsername())){
                                        Player p = processMap.get(i.getPlayer().getUsername()).getMyPlayer();
                                        i.getPlayer().setStatus(p.getStatus());
                                    }
                                }

                                result.setData(member);
                                sendData(result);
                                break;
                            case ObjectWrapper.SEARCH_GROUP:
                                 result = new ObjectWrapper();
                                udpControl.sendData(data);
                                result = udpControl.receiveData();
                                sendData(result);
                                break;
                            case ObjectWrapper.OUT_GROUP:
                                result = new ObjectWrapper();
                                udpControl.sendData(data);
                                result = udpControl.receiveData();
                                Group group = (Group) result.getData();
                                annouceGroup(group);
                                System.out.println("Out Group");
                                sendData(result);
                                     
                        /*
                            ********************GAME PLAY*******************************        
                                    */
                                case ObjectWrapper.CREATE_ROOM:
                                System.out.println("CREATE_ROOM");
                                Room room = new Room();
                                ArrayList<PlayingPlayer> ppl = new ArrayList<PlayingPlayer>();
                                ppl.add(new PlayingPlayer(this.myPlayer, new ArrayList<String>()));
                                room.setPlayingplayerList(ppl);
                                
                                roomMapId++;
                                room.setId(roomMapId);
                                this.roomId = roomMapId;
                                roomMap.put(roomMapId, room);
                                annouceStatus(Player.PLAYING);
                                sendData(new ObjectWrapper(ObjectWrapper.GET_ROOM,room));
                                break;
                            
                            case ObjectWrapper.INVITE_ROOM_SERVER:
                                String username = (String)data.getData();
                                if (roomMap.containsKey(this.roomId) && processMap.containsKey(username)
                                        && processMap.get(username).getMyPlayer().getStatus()== Player.ONLINE){
                                    Room r = roomMap.get(this.roomId);
                                    processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.INVITE_ROOM_CLIENT, r));
                                }
                                break;    
                            
                                
                            case ObjectWrapper.ACCEPT_INVITE_ROOM:
                                int rid = (Integer)data.getData();
                                if (roomMap.containsKey(rid) && roomMap.get(rid).getStatus()==Room.WAITING){
                                    PlayingPlayer pp = new PlayingPlayer();
                                    pp.setPlayer(this.myPlayer);
                                    roomMap.get(rid).addPlayingPlayer(pp);

                                    if (roomMap.containsKey(this.roomId) 
                                            && roomMap.get(this.roomId).getStatus() == Room.WAITING){
                                        if (roomMap.get(this.roomId).getPlayingplayerList().size()>1){
                                            roomMap.get(this.roomId).removePlayingPlayer(myPlayer.getId());
                                            annouceRoom(roomMap.get(this.roomId));
                                        } else {
                                            roomMap.remove(this.roomId);
                                        }

                                    }
                                    this.roomId = rid;
                                    annouceStatus(Player.PLAYING);
                                    annouceRoom(roomMap.get(rid));

                                }
                                break;
//                                
                            case ObjectWrapper.EXIT_ROOM:
                                if (roomMap.containsKey(this.roomId)){
                                    annouceStatus(Player.ONLINE);
                                    if (roomMap.get(this.roomId).getPlayingplayerList().size()>1){
                                        roomMap.get(this.roomId).removePlayingPlayer(myPlayer.getId());

                                        annouceRoom(roomMap.get(this.roomId));
                                    } else {
                                        roomMap.remove(this.roomId);
                                    }
                                    this.roomId=0;
                                }
                                break;

                            case ObjectWrapper.START_ROOM:
                                // thông báo người chơi playing
                                if (roomMap.containsKey(this.roomId)
                                        && roomMap.get(this.roomId).getStatus()==Room.WAITING)
                                { 
                                    new GameplayCtr().startGame(roomMap.get(this.roomId));
                                    room = roomMap.get(this.roomId);
                                    for (PlayingPlayer pp: roomMap.get(this.roomId).getPlayingplayerList()){
                                        username = pp.getPlayer().getUsername();
                                        if (processMap.containsKey(username)){
                                            processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.REPLY_START_ROOM,""));
                                            processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.GET_PLAYING_ROOM,room));

                                        }

                                    }
                                    rdata = udpSendData(new ObjectWrapper(ObjectWrapper.START_MATCH,room));
                                    if (rdata.getPerformative() == ObjectWrapper.REPLY_START_MATCH){
                                        room = (Room)rdata.getData();
                                        roomMap.get(this.roomId).setMatchId(room.getMatchId());
                                        System.out.println(roomMap.get(this.roomId).getMatchId());
                                    }
                                    setSecond(Room.TURN_SECS);

                                }
                                break;


                            case ObjectWrapper.DRAW_CARD:
                                if ( roomMap.containsKey(this.roomId) 
                                        && roomMap.get(this.roomId).getStatus() == Room.PLAYING
                                        && roomMap.get(this.roomId).getCurrentTurn().getName().equals(this.myPlayer.getUsername()))
                                {
                                    new GameplayCtr().drawCard(roomMap.get(this.roomId)); 
                                    room = roomMap.get(this.roomId);
                                    for (PlayingPlayer pp: roomMap.get(this.roomId).getPlayingplayerList()){
                                        username = pp.getPlayer().getUsername();
                                        if (processMap.containsKey(username)){
                                            processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.GET_PLAYING_ROOM,room));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.DISCARD_CARD:
                                if (roomMap.containsKey(this.roomId) 
                                        && roomMap.get(this.roomId).getStatus() == Room.PLAYING
                                        && roomMap.get(this.roomId).getCurrentTurn().getName().equals(this.myPlayer.getUsername()))
                                {
                                    int index = (Integer)data.getData();
                                    boolean victory = new GameplayCtr().discardCard(index, roomMap.get(this.roomId));

                                    room = roomMap.get(this.roomId);
                                        for (PlayingPlayer pp: roomMap.get(this.roomId).getPlayingplayerList()){
                                            username = pp.getPlayer().getUsername();
                                            if (processMap.containsKey(username)){
                                                processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.GET_PLAYING_ROOM,room));
                                            }
                                        }

                                    if (victory == true){
                                            room = roomMap.get(roomId);
                                            udpSendData(new ObjectWrapper(ObjectWrapper.UPDATE_MATCH,room));
                                            for (PlayingPlayer pp: roomMap.get(roomId).getPlayingplayerList()){
                                                username = pp.getPlayer().getUsername();
                                                pp.getPlayer().setTotalmatch(pp.getPlayer().getTotalmatch()+1);

                                                if (username.equals(myPlayer.getUsername())){
                                                    pp.getPlayer().setWinmatch(pp.getPlayer().getWinmatch()+1);

                                                }

                                                if ( processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus() == Player.PLAYING ){
                                                    processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.VICTORY_ROOM,room.getWinner().getPlayer().getUsername()));

                                                }
                                            }   
//                                                roomMap.get(roomId).endRoom();
                                            for (PlayingPlayer pp: roomMap.get(roomId).getPlayingplayerList()){
                                                username = pp.getPlayer().getUsername();
                                                if ( processMap.containsKey(username) && processMap.get(username).getMyPlayer().getStatus() != Player.OFFLINE ){
                                                    processMap.get(username).annouceRoom(roomMap.get(roomId));
                                                    processMap.get(username).sendData(new ObjectWrapper(ObjectWrapper.ANNOUCE_PLAYER_INFO,pp.getPlayer()));
                                                }
                                            }  


                                        } else {
                                            if (processMap.containsKey(roomMap.get(roomId).getCurrentTurn().getName())){
                                                processMap.get(roomMap.get(roomId).getCurrentTurn().getName()).setSecond(RoomOld.TURN_SECS);
                                            }
                                        }

                                }
                            break;   
                        
                                
                            // ------------MatchHistory--------------------------------
                            
                            case ObjectWrapper.GET_MATCH_HISTORY:
                                sendData(udpSendData(new ObjectWrapper(ObjectWrapper.GET_MATCH_HISTORY,this.myPlayer.getId())));
                                break;
                            
                            case ObjectWrapper.GET_MATCH_HISTORY_DETAIL:
                                sendData(udpSendData(data));
                                break;
                            
                                    

                        }
 
                    }

                }
            }catch (EOFException | SocketException e) {             
                //e.printStackTrace();
                
                if (processMap.containsKey(myPlayer.getUsername())){
                    annouceStatus(Player.OFFLINE);
                    if ( roomMap.containsKey(this.roomId) && roomMap.get(this.roomId).getStatus()== Room.WAITING ){
                        
                        if (roomMap.get(this.roomId).getPlayingplayerList().size()>1){
                            roomMap.get(this.roomId).removePlayingPlayer(myPlayer.getId());
                            annouceRoom(roomMap.get(this.roomId));
                        } else {
                            roomMap.remove(this.roomId);
                        } 
                        
                        processMap.remove(myPlayer.getUsername());
                        
                    } else 
                    if ( roomMap.containsKey(this.roomId) && roomMap.get(this.roomId).getStatus()== Room.PLAYING )
                    {
                        
                    } else {
                        processMap.remove(myPlayer.getUsername());
                    }
                      
                }
                
                myProcess.remove(this);
                view.showMessage("Number of client connecting to the server: " + myProcess.size());
                try {
                    mySocket.close();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
                this.stop();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
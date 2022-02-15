package tcp.client.control;

import tcp.client.view.RegisterFrm;
import tcp.client.view.RankFrm;
import tcp.client.view.GroupPlayerNotInFrm;
import tcp.client.view.PlayingRoomFrm;
import tcp.client.view.HomeFrm;
import tcp.client.view.PlayerInfoFrm;
import tcp.client.view.ClientMainFrm;
import tcp.client.view.GroupFrm;
import tcp.client.view.GroupInfoFrm;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
 

import model.*;
 
 
public class ClientCtr {
    private Socket mySocket;
    private ClientMainFrm view;
    private ClientListening myListening;                            // thread to listen the data from the server
    private ArrayList<ObjectWrapper> myFunction;                  // list of active client functions
    private IPAddress serverAddress = new IPAddress("localhost",8888);  // default server host and port
     
    public ClientCtr(ClientMainFrm view){
        super();
        this.view = view;
        myFunction = new ArrayList<ObjectWrapper>();  
    }
     
    public ClientCtr(ClientMainFrm view, IPAddress serverAddr) {
        super();
        this.view = view;
        this.serverAddress = serverAddr;
        myFunction = new ArrayList<ObjectWrapper>();
    }
 
 
    public boolean openConnection(){        
        try {
            mySocket = new Socket(serverAddress.getHost(), serverAddress.getPort());  
            myListening = new ClientListening();
            myListening.start();
            //view.showMessage("Connected to the server at host: " + serverAddress.getHost() + ", port: " + serverAddress.getPort());
            System.out.println("Connected to the server at host: " + serverAddress.getHost() + ", port: " + serverAddress.getPort());
        } catch (Exception e) {
            //e.printStackTrace();
            //view.showMessage("Error when connecting to the server!");
            System.out.println("Error when connecting to the server!");
            return false;
        }
        return true;
    }
     
    
    public boolean sendData(Object obj){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(obj);           
             
        } catch (Exception e) {
            //e.printStackTrace();
            //view.showMessage("Error when sending data to the server!");
            System.out.println("Error when sending data to the server!");
            return false;
        }
        return true;
    }
    
    
    public boolean closeConnection(){
         try {
             if(myListening != null)
                 myListening.stop();
             if(mySocket !=null) {
                 mySocket.close();
                 //view.showMessage("Disconnected from the server!");
                 System.out.println("Disconnected from the server!");
             }
            myFunction.clear();             
         } catch (Exception e) {
             //e.printStackTrace();
             //view.showMessage("Error when disconnecting from the server!");
             System.out.println("Error when disconnecting from the server!");
             return false;
         }
         return true;
    }
     
     
     
    public ArrayList<ObjectWrapper> getActiveFunction() {
        return myFunction;
    }
 
 
    class ClientListening extends Thread{
         
        public ClientListening() {
            super();
        }
         
        public void run() {
            try {
                while(true) {
                ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
                Object obj = ois.readObject();
                if(obj instanceof ObjectWrapper) {
                    ObjectWrapper data = (ObjectWrapper)obj;
                    if(data.getPerformative() == ObjectWrapper.SERVER_INFORM_CLIENT_NUMBER)
                        //view.showMessage("Number of client connecting to the server: " + data.getData());
                        System.out.println("Number of client connecting to the server: " + data.getData());
                    else {
                        for(int i=0; i<myFunction.size(); i++){
                            ObjectWrapper fto = myFunction.get(i);
                        
                            if(fto.getPerformative() == data.getPerformative()) {
                            	
                                switch(data.getPerformative()) {
                                    case ObjectWrapper.REPLY_LOGIN_PLAYER:
                                        ClientMainFrm loginView = (ClientMainFrm)fto.getData();
                                        loginView.receivedDataProcessing(data);
                                        System.out.println("login");
                                        break;
                                    case ObjectWrapper.REPLY_REGISTER_PLAYER:
                                        System.out.println("receive regis reply");
                                        RegisterFrm registerView = (RegisterFrm)fto.getData();
                                        registerView.receivedDataProcessing(data); 
                                        System.out.println("register");
                                        break;
                                    case ObjectWrapper.ANNOUNCE_STATUS:
                                        System.out.println("ANNOUNCE_STATUS");
                                        HomeFrm homeView = (HomeFrm)fto.getData();
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.REPLY_ACCEPT_FRIEND:
                                        System.out.println("REPLY_ACCEPT_FRIEND");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.ANNOUCE_FRIEND_REQUEST_SUCCESS:
                                        System.out.println("ANNOUCE_FRIEND_REQUEST_SUCCESS");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.REPLY_REJECT_FRIEND_REQUEST:
                                        System.out.println("REPLY_REJECT_FRIEND_REQUEST");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                    case ObjectWrapper.GET_ROOM:
                                        System.out.println("GET_ROOM");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.INVITE_ROOM_CLIENT:
                                        System.out.println("INVITE_ROOM_CLIENT");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.REPLY_START_ROOM:
                                        System.out.println("REPLY_START_ROOM");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.GET_PLAYING_ROOM:
                                        System.out.println("GET_PLAYING_ROOM");
                                        PlayingRoomFrm playingRoomView = (PlayingRoomFrm)fto.getData();
                                        playingRoomView.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.VICTORY_ROOM:
                                        System.out.println("VICTORY_ROOM");
                                        playingRoomView = (PlayingRoomFrm)fto.getData();
                                        playingRoomView.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.ANNOUCE_PLAYER_INFO:
                                        System.out.println("ANNOUCE_PLAYER_INFO");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.REPLY_GET_RANK:
                                        System.out.println("REPLY_GET_RANK");
                                        RankFrm rf = (RankFrm)fto.getData();
                                        rf.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.REPLY_GET_GROUP_PLAYER_NOT_IN:
                                        System.out.println("REPLY_GET_GROUP_PLAYER_NOT_IN");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.REPLY_GET_PLAYERS_WANT_JOIN_GROUP:
                                        System.out.println("REPLY_GET_PLAYERS_WANT_JOIN_GROUP");
                                        GroupInfoFrm giView = (GroupInfoFrm)fto.getData();
                                        giView.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.REPLY_JOIN_GROUP:
                                        GroupPlayerNotInFrm gpniView = (GroupPlayerNotInFrm)fto.getData();
                                        gpniView.receivedDataProcessing(data);
                                        break;

                                    case ObjectWrapper.UPDATE_GROUP_INFO:
                                        System.out.println("UPDATE_GROUP_INFO");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                    
                                    case ObjectWrapper.REPLY_CREATE_GROUP:
                                        System.out.println("REPLY_CREATE_GROUP");
                                        homeView = (HomeFrm)fto.getData();
                                        homeView.receivedDataProcessing(data);
                                        break;
                                        //****************
                                    case ObjectWrapper.REPLY_GROUP_REQUEST:
                                        PlayerInfoFrm playerview = (PlayerInfoFrm)fto.getData();
                                        playerview.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.ANNOUCE_GROUP_REQUEST:
                                         HomeFrm hmview = (HomeFrm)fto.getData();
                                         hmview.receivedDataProcessing(data);
                                         break;

                                    case ObjectWrapper.REPLY_ACCPET_GROUP_REQUEST:
                                        System.out.println("Accept group request");
                                        hmview = (HomeFrm)fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;

                                    case ObjectWrapper.REPLY_REJECT_GROUP_REQUEST:
                                        System.out.println("Reject group request");
                                        hmview = (HomeFrm)fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;

                                    case ObjectWrapper.ANNOUCE_GROUP_REQUEST_SUCCESS:
                                        System.out.println("Annouce group re success");
                                        hmview = (HomeFrm)fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;

                                    case ObjectWrapper.REPLY_SEARCH_GROUP:
                                        System.out.println("Reply_Search_group");
                                        hmview = (HomeFrm)fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;
                                    case ObjectWrapper.REPLY_GET_GROUP:
                                        System.out.println("REPLY GET GROUP");
                                        GroupFrm grview = (GroupFrm) fto.getData();
                                        grview.receivedDataProcessing(data);
                                        break;

                                    case ObjectWrapper.REPLY_OUT_GROUP:
                                        System.out.println("OUT GROUP");
                                        hmview = (HomeFrm) fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;

                                    case ObjectWrapper.REPLY_GET_GROUP_MEMBER:
                                        System.out.println("REPLY GET MEMBER");
                                        GroupInfoFrm grinfo = (GroupInfoFrm) fto.getData();
                                        grinfo.receivedDataProcessing(data);
                                        break;
                                        
                                    
                                    //---------------------------
                                    case ObjectWrapper.REPLY_GET_MATCH_HISTORY:    
                                        System.out.println("REPLY_GET_MATCH_HISTORY");
                                        hmview = (HomeFrm) fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;
                                        
                                    case ObjectWrapper.REPLY_GET_MATCH_HISTORY_DETAIL:    
                                        System.out.println("REPLY_GET_MATCH_HISTORY_DETAIL");
                                        hmview = (HomeFrm) fto.getData();
                                        hmview.receivedDataProcessing(data);
                                        break;
                                }
                            }
                        }
                        System.out.println("Received an object: " + data.getPerformative());
                    }
                }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error when receiving data from the server!");
                view.resetClient();
            }
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp.client.view;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Group;
import model.ObjectWrapper;
import model.Player;
import model.Request;
import tcp.client.control.ClientCtr;
import java.util.Iterator;
import model.MatchHistory;
import model.MatchHistoryDetail;
import model.PlayerGroup;
import model.PlayingPlayer;
import model.Room;

/**
 *
 * @author fake1
 */
public class HomeFrm extends javax.swing.JFrame {
    private ClientCtr myControl;
    private Player myPlayer;
    private GroupInfoFrm groupInfoView;
    private Room room;
    private PlayingRoomFrm playingRoomView;
    private ArrayList<InviteRoomFrm> inviteRoomList;
    private ArrayList<Room> inviteRoomL;
    private ArrayList<Group> groupList,requestGroupList,group2List;
    private RankFrm rankfrm;
    private DefaultTableModel model,modelGroup,modelRequest,modelRoom,modelGroupRequest,modelGroup2,modelInvite;
    
    /**
     * Creates new form HomeFrm
     */
    public HomeFrm() {
        initComponents();
    }
    
    public HomeFrm(ClientCtr myControl){
        
        
        this.myControl = myControl;
        initComponents();
        groupInfoView = new GroupInfoFrm(myControl);
        playingRoomView = new PlayingRoomFrm(myControl);
        inviteRoomList = new ArrayList<InviteRoomFrm>();
        inviteRoomL = new ArrayList<Room>();
        
        String colFriend [] ={"Name","Rank","Status"};
        String colRequest [] ={"Request From"};
        String colGroup [] ={"Group name","Rank","Quantity"};
        String colRoom [] ={"Name",""};
        String colGroupRe [] ={"Group name","Rank","Quantity"};
        String colGroup2 [] = {"Group name","Rank","Note"};
        String colInvite[] ={"STT","Id phòng"};
        model = new DefaultTableModel(colFriend, 0);
        modelRequest = new DefaultTableModel(colRequest,0);
        modelGroup = new DefaultTableModel(colGroup,0);
        modelRoom = new DefaultTableModel(colRoom, 0);
        modelGroupRequest = new DefaultTableModel(colGroupRe,0);
        modelGroup2 = new DefaultTableModel(colGroup2,0);
        modelInvite = new DefaultTableModel(colInvite,0);
        
        tblFriend.setModel(model);
        tblGroup.setModel(modelGroup);
        tblGroupRequest.setModel(modelGroupRequest);
        tblGroup2.setModel(modelGroup2);
        
        
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.ANNOUNCE_STATUS, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_ACCEPT_FRIEND, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.ANNOUCE_FRIEND_REQUEST_SUCCESS, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_REJECT_FRIEND_REQUEST, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.GET_ROOM, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.INVITE_ROOM_CLIENT, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_START_ROOM, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.ANNOUCE_PLAYER_INFO, this));
//        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_RANK, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_GROUP_PLAYER_NOT_IN, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.UPDATE_GROUP_INFO, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CREATE_GROUP, this));
        
        
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.ANNOUCE_GROUP_REQUEST, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_REJECT_GROUP_REQUEST, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_ACCPET_GROUP_REQUEST, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.ANNOUCE_GROUP_REQUEST_SUCCESS, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_SEARCH_GROUP, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_OUT_GROUP, this));
        
        
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_MATCH_HISTORY, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_MATCH_HISTORY_DETAIL, this));
        
    }
    
    public void viewInit(){
        txtUsername.setText(myPlayer.getUsername());
        txtWinmatch.setText(myPlayer.getWinmatch()+"");
        txtTotalmatch.setText(myPlayer.getTotalmatch()+"");
        viewTblFriend();
        viewTblFriendRequest();
        viewTblGroup();
        getRequestGroupList();
    }
    
    public void viewTblFriend(){
        ArrayList<Player> friendList = myPlayer.getFriendList();
        
        String[] columnNames={"Name","Rank","Status"};
        String[][] value=new String[friendList.size()][4];
        for(int i=0;i<friendList.size();i++)
            {
                value[i][0]=friendList.get(i).getUsername();
                value[i][1]=friendList.get(i).getWinmatch()+"";
                switch (friendList.get(i).getStatus()){
                    case Player.ONLINE:
                        value[i][2]= "online";
                        break;
                    case Player.OFFLINE:
        		value[i][2]= "offline ";
        		break;
                    case Player.PLAYING:
        		value[i][2]= "playing";
        		break;
                }
            }
        tblFriend.setModel(new DefaultTableModel(value,columnNames));
                               
    }
    
    public void viewTblFriendRequest(){
        ArrayList<Player> friendRequestList = myPlayer.getFriendRequestList();
        
        String[] columnNames={"From"};
        String[][] value=new String[friendRequestList.size()][4];
        for(int i=0;i<friendRequestList.size();i++)
            {
                value[i][0]=friendRequestList.get(i).getUsername()+" muốn kết bạn";
            }
        tblFriendRequest.setModel(new DefaultTableModel(value,columnNames));
    }
    
    public void viewTblGroup(){
        groupList= myPlayer.getGroupList();
        String[] columnNames={"Group name ","Rank ","Quantity "};
            String[][] value=new String[groupList.size()][4];
            for(int i=0;i<groupList.size();i++)
            {
                value[i][0]= groupList.get(i).getName();
                value[i][2]= groupList.get(i).getPlayerGroupList().size()+"";

            }
        tblGroup.setModel(new DefaultTableModel(value,columnNames));
    }
    
    private void getRequestGroupList(){
        requestGroupList = myPlayer.getRequestGroupList();
        modelGroupRequest.setRowCount(0);
        for(Group i : requestGroupList){
            modelGroupRequest.addRow(new Object[]{i.getName(),"",i.getPlayerGroupList().size()});
           // System.out.println(i.getId()+" "+i.getName()+" 140-homefrm");
        }
        
    }
    
    public void setPlayer(Player player){
        myPlayer = player;
        
        viewInit();
    }

    public void updateStatus(Player player){
        for (Player plr : myPlayer.getFriendList()) {
    		if (plr.getId()== player.getId() ) {
    			plr.setStatus(player.getStatus());
                        break;
    		}
    	}
        viewTblFriend();
        for (Group group: myPlayer.getGroupList()){
                for (PlayerGroup pg: group.getPlayerGroupList()){
                    //Player plr = pg.getPlayer();
                    if (pg.getPlayer().getId()== player.getId() ) {
    			pg.getPlayer().setStatus(player.getStatus());
                        break;
                    }
                }
            }
        if (groupInfoView.isVisible()==true) groupInfoView.viewInit();
    }
    
    private void getRoomPlayerList(){
        ArrayList<PlayingPlayer> ppl= new ArrayList<PlayingPlayer>();
        ppl= this.room.getPlayingplayerList();
        String[] columNames={"Name ",""};
        String[][] value=new String[ppl.size()][3];
        for(int i=0;i<ppl.size();i++)
        {
            value[i][0]= ppl.get(i).getPlayer().getUsername();
        }
        value[0][1]= "Chủ phòng";
        tblLobby.setModel(new DefaultTableModel(value,columNames));  
    }
    
    private void getGroup2List(){
        modelGroup2.setRowCount(0);
        //System.out.println("167-homefrm " + group2List.size());
        for(Group i : group2List){
            modelGroup2.addRow(new Object[]{i.getName(),"",i.getNote()});
        }
    }
    
    public void viewTblRoomInvite(){
        String[] columNames={"Bạn có lời mời chơi game vào room"};
        String[][] value=new String[inviteRoomL.size()][1];
        for(int i=0;i<inviteRoomL.size();i++)
        {
            value[i][0]= inviteRoomL.get(i).getPlayingplayerList().get(0).getPlayer().getUsername()+" mời bạn vào phòng "+inviteRoomL.get(i).getId()+"";
        }
        tblRoomInvite.setModel(new DefaultTableModel(value,columNames));
    }
    
    public void viewTblHistory(ArrayList<MatchHistory> mhl){
        String[] columNames={"Kết quả","Thời gian trận đấu","id"};
        String[][] value=new String[mhl.size()][3];
        for(int i=0;i<mhl.size();i++)
        {
            if (mhl.get(i).getStatus() == PlayingPlayer.WIN){
                value[i][0] = "WIN";
            } else {
                value[i][0] = "LOSE";
            }
            value[i][1] = mhl.get(i).getBegin() +" : "+mhl.get(i).getEnd();
            value[i][2] = mhl.get(i).getId()+"";
        }
        tblHistory.setModel(new DefaultTableModel(value,columNames));
    }
    
    public void receivedDataProcessing(ObjectWrapper data) {
        switch (data.getPerformative()){
           case ObjectWrapper.ANNOUNCE_STATUS:
                Player player = (Player)data.getData();
                updateStatus(player);

                break;
            
            case ObjectWrapper.REPLY_ACCEPT_FRIEND:
                if (data.getData() instanceof String){
                    JOptionPane.showMessageDialog(this, "Accept friend request error!");
                } else {
                    player = (Player)data.getData();
//                    listFriend.add(player);
//                    this.player.setListfriend(listFriend);
                    myPlayer.getFriendList().add(player);
                    viewTblFriend();
                    Iterator itr = myPlayer.getFriendRequestList().iterator();
                    while (itr.hasNext()){
                        Player plr = (Player)itr.next();
                        if (plr.getId()== player.getId() ) {
                            itr.remove();
                            break;
                        }
                    }
                    viewTblFriendRequest();
                    JOptionPane.showMessageDialog(this, "Accept friend request success!");                   
                }
                break;
           
            case ObjectWrapper.ANNOUCE_FRIEND_REQUEST_SUCCESS:
                player = (Player)data.getData();
                myPlayer.getFriendList().add(player);
                viewTblFriend();
                break;
                
            case ObjectWrapper.REPLY_REJECT_FRIEND_REQUEST:
                if (data.getData() instanceof String){
                    JOptionPane.showMessageDialog(this, "Reject friend request error!");
                } else {
                    player = (Player)data.getData();
                    Iterator itr = myPlayer.getFriendRequestList().iterator();
                    while (itr.hasNext()){
                        Player plr = (Player)itr.next();
                        if (plr.getId()== player.getId() ) {
                            itr.remove();
                            break;
                        }
                    }
                    viewTblFriendRequest();
                    JOptionPane.showMessageDialog(this, "Reject friend request success!");
                }
                break;
            
            case ObjectWrapper.UPDATE_GROUP_INFO:
                if (data.getData() instanceof Group){
                    Group gr = (Group)data.getData();
                    myPlayer.updateGroupInfo(gr);
                    viewTblGroup();
                    if (groupInfoView.isVisible()==true) groupInfoView.viewInit();
                }
                
                break;
            
            case ObjectWrapper.GET_ROOM:
                this.room = (Room)data.getData();
                getRoomPlayerList();
                tblLobby.setVisible(true);
                btnCreateRoom.setEnabled(false);
                if (this.room.getPlayingplayerList().get(0).getPlayer().getId()==this.myPlayer.getId()) btnPlay.setEnabled(true); else btnPlay.setEnabled(false);
                btnExitRoom.setEnabled(true);
                inviteRoomL.clear();
                viewTblRoomInvite();
                break;
                
            case ObjectWrapper.INVITE_ROOM_CLIENT:
                Room r = (Room)data.getData();
                inviteRoomL.add(r);
                viewTblRoomInvite();
                break;
            
            case ObjectWrapper.REPLY_START_ROOM:
                for (int i=0; i<room.getPlayingplayerList().size();i++)
                    if (room.getPlayingplayerList().get(i).getPlayer().getId()== this.myPlayer.getId()){
                        playingRoomView.setIndex(i);
                        System.out.println(i);
                        break;
                    }
                playingRoomView.setLocationRelativeTo(this);
                playingRoomView.setVisible(true);
                
                btnPlay.setEnabled(false);
                break;
            
            case ObjectWrapper.ANNOUCE_PLAYER_INFO:
                myPlayer = (Player)data.getData();
                txtWinmatch.setText(myPlayer.getWinmatch()+"");
                txtTotalmatch.setText(myPlayer.getTotalmatch()+"");
                break;
                
                
//            case ObjectWrapper.REPLY_GET_RANK:
//                RankFrm rankView = new RankFrm(myPlayer, (ArrayList<Player>)data.getData(), myControl);
//                rankView.setLocationRelativeTo(this);
//                rankView.setVisible(true);
//                break;
            
            case ObjectWrapper.REPLY_GET_GROUP_PLAYER_NOT_IN:
                GroupPlayerNotInFrm gpView = new GroupPlayerNotInFrm(myControl, (ArrayList<Group>)data.getData(), myPlayer.getId());
                gpView.setLocationRelativeTo(this);
                gpView.setVisible(true);
                break;
               
            
            case ObjectWrapper.ANNOUCE_GROUP_REQUEST:
                System.out.println("288/HomeFrm - annouce gr request");
                if(data.getData() instanceof Group){
                    Group gr = (Group) data.getData();
                    System.out.println(gr.getName()+" "+gr.getPlayerGroupList().size());
                    requestGroupList.add(gr);
                    myPlayer.setRequestGroupList(requestGroupList);
                    getRequestGroupList();
                }
               break;
               
            case ObjectWrapper.REPLY_ACCPET_GROUP_REQUEST:
                if (data.getData() instanceof String){
                    JOptionPane.showMessageDialog(this, "Accept group request error!");
                } else {
                    Group group = (Group)data.getData();
                    groupList.add(group);
                    this.myPlayer.setGroupList(groupList);
                    viewTblGroup();
                    for (Group i : requestGroupList) {
                        if (i.getId()== group.getId() ) {
                            requestGroupList.remove(i);
                            break;
                        }
                    }
                    this.myPlayer.setRequestGroupList(requestGroupList);
                    getRequestGroupList();
                    JOptionPane.showMessageDialog(this, "Accept group request success!");                   
                }
                break;
            case ObjectWrapper.REPLY_REJECT_GROUP_REQUEST:
               if (data.getData() instanceof String){
                   JOptionPane.showMessageDialog(this, "Reject group request error!");
               } else {
                   Group group = (Group)data.getData();
                   for (Group i : requestGroupList) {
                       if (i.getId()== group.getId() ) {
                           requestGroupList.remove(i);
                           break;
                       }
                   }
                   this.myPlayer.setRequestGroupList(requestGroupList);
                   getRequestGroupList();
                   JOptionPane.showMessageDialog(this, "Reject group request success!");
               }
               break;
            case ObjectWrapper.ANNOUCE_GROUP_REQUEST_SUCCESS:
                if(data.getData() instanceof Group){
                    Group group = (Group) data.getData();
                   System.out.println(group.getPlayerGroupList().size());
                   for(Group i : groupList){
                       if(i.getId() == group.getId()){  
                           i.setPlayerGroupList(group.getPlayerGroupList());
                           break;
                       }
                   }
                    this.myPlayer.setGroupList(groupList);
                    viewTblGroup();

                }
                break;
            case ObjectWrapper.REPLY_SEARCH_GROUP:
                group2List = (ArrayList<Group>) data.getData();
                getGroup2List();
                break;

            case ObjectWrapper.REPLY_JOIN_GROUP:

                if(data.getData() instanceof Group){
                    Group group = (Group) data.getData();
                    //System.out.println("354-- "+group.getPlayerGroupList().size());
                    groupList.add(group);
                    this.myPlayer.setGroupList(groupList);
                    viewTblGroup();
                    if(group != null){
                        JOptionPane.showMessageDialog(this, "Join Group Thành công");
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Có lỗi!");
                    }

                }

                break;
            case ObjectWrapper.REPLY_OUT_GROUP:
                Group group = (Group) data.getData();
                for(Group i : groupList){
                    if(i.getId() == group.getId()){
                        groupList.remove(i);
                        break;
                    }
                }
                this.myPlayer.setGroupList(groupList);
                viewTblGroup();
                break;
   
            case ObjectWrapper.REPLY_GET_MATCH_HISTORY:
                ArrayList<MatchHistory> mhl = (ArrayList<MatchHistory>) data.getData();
                viewTblHistory(mhl);
                break;
            
            case ObjectWrapper.REPLY_GET_MATCH_HISTORY_DETAIL:
                MatchHistoryFrm mhf = new MatchHistoryFrm((MatchHistoryDetail) data.getData());
                mhf.setLocationRelativeTo(this);
                mhf.setVisible(true);
                break;
        }

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        historytab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtWinmatch = new javax.swing.JTextField();
        txtTotalmatch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnCreateRoom = new javax.swing.JButton();
        btnExitRoom = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLobby = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRoomInvite = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        btnHistoryTabRefresh = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        friendPanel = new javax.swing.JPanel();
        friendList = new javax.swing.JScrollPane();
        tblFriend = new javax.swing.JTable();
        requestPane = new javax.swing.JScrollPane();
        tblFriendRequest = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        groupPane = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGroup = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnNewGroup = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblGroupRequest = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblGroup2 = new javax.swing.JTable();
        txtSearchGroup = new javax.swing.JTextField();
        btnSearchGroup = new javax.swing.JButton();
        btnRank = new javax.swing.JToggleButton();
        btnJoinGroup = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        historytab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historytabMouseClicked(evt);
            }
        });

        jLabel5.setText("Username");

        jLabel7.setText("Win match");

        jLabel8.setText("Total match");

        txtUsername.setEditable(false);

        txtWinmatch.setEditable(false);

        txtTotalmatch.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(42, 42, 42)
                        .addComponent(txtTotalmatch, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(48, 48, 48)
                        .addComponent(txtWinmatch))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(50, 50, 50)
                        .addComponent(txtUsername)))
                .addGap(48, 48, 48))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtWinmatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTotalmatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(221, Short.MAX_VALUE))
        );

        historytab.addTab("PlayerInfo", jPanel1);

        btnCreateRoom.setText("Create Room");
        btnCreateRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRoomActionPerformed(evt);
            }
        });

        btnExitRoom.setText("Exit Room");
        btnExitRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitRoomActionPerformed(evt);
            }
        });

        btnPlay.setText("Play");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        tblLobby.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblLobby);

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        tblRoomInvite.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblRoomInvite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRoomInviteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblRoomInvite);

        jLabel4.setText("Lobby");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCreateRoom)
                        .addGap(56, 56, 56)
                        .addComponent(btnExitRoom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCreateRoom)
                        .addComponent(btnExitRoom))
                    .addComponent(btnPlay, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        historytab.addTab("Play game", jPanel2);

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHistoryMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblHistory);

        btnHistoryTabRefresh.setText("Refresh");
        btnHistoryTabRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryTabRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnHistoryTabRefresh)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHistoryTabRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap())
        );

        historytab.addTab("History", jPanel3);

        tblFriend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFriendMouseClicked(evt);
            }
        });
        friendList.setViewportView(tblFriend);

        tblFriendRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFriendRequest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFriendRequestMouseClicked(evt);
            }
        });
        requestPane.setViewportView(tblFriendRequest);

        jLabel1.setText("Friend List");

        jLabel2.setText("Friend Request");

        javax.swing.GroupLayout friendPanelLayout = new javax.swing.GroupLayout(friendPanel);
        friendPanel.setLayout(friendPanelLayout);
        friendPanelLayout.setHorizontalGroup(
            friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(friendList, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(requestPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(friendPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 105, Short.MAX_VALUE)))
                .addContainerGap())
        );
        friendPanelLayout.setVerticalGroup(
            friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(friendList, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                    .addComponent(requestPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Friend", friendPanel);

        tblGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroupMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGroup);

        jLabel3.setText("Group List");

        btnNewGroup.setText("Tạo Group");
        btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout groupPaneLayout = new javax.swing.GroupLayout(groupPane);
        groupPane.setLayout(groupPaneLayout);
        groupPaneLayout.setHorizontalGroup(
            groupPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .addGroup(groupPaneLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(groupPaneLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btnNewGroup)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupPaneLayout.setVerticalGroup(
            groupPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(btnNewGroup)
                .addGap(52, 52, 52))
        );

        jTabbedPane2.addTab("Group", groupPane);

        tblGroupRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGroupRequest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroupRequestMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblGroupRequest);

        jTabbedPane2.addTab("Group Request", jScrollPane4);

        tblGroup2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGroup2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroup2MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblGroup2);

        btnSearchGroup.setText("Tìm ");
        btnSearchGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(txtSearchGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSearchGroup)
                .addGap(191, 191, 191))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchGroup))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Group", jPanel5);

        btnRank.setText("Rank");
        btnRank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRankActionPerformed(evt);
            }
        });

        btnJoinGroup.setText("Join Group");
        btnJoinGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(historytab)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRank, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnJoinGroup)
                        .addGap(75, 75, 75)))
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(historytab, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRank)
                            .addComponent(btnJoinGroup)))
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFriendMouseClicked
        // TODO add your handling code here:
        int r = tblFriend.getSelectedRow();
        if (r>=0 && r< tblFriend.getRowCount()){
            PlayerInfoFrm playerView = new PlayerInfoFrm(myPlayer.getFriendList().get(r), myControl, true,myPlayer.getId(),myPlayer.getGroupList());
            playerView.setLocationRelativeTo(this);
            playerView.setVisible(true);
        }
    }//GEN-LAST:event_tblFriendMouseClicked

    private void tblGroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupMouseClicked
        // TODO add your handling code here:
        int r = tblGroup.getSelectedRow();
        if (r>=0 && r<tblGroup.getRowCount()){
            groupInfoView.setGroupInfoFrm(myPlayer, myPlayer.getGroupList().get(r), myControl);
            groupInfoView.setLocationRelativeTo(this);
            groupInfoView.setVisible(true);
        }
    }//GEN-LAST:event_tblGroupMouseClicked

    private void tblFriendRequestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFriendRequestMouseClicked
        // TODO add your handling code here:
        int r = tblFriendRequest.getSelectedRow();
        if (r>=0 && r<tblFriendRequest.getRowCount()){
            Player plr = myPlayer.getFriendRequestList().get(r);
            int response = JOptionPane.showConfirmDialog(this,"Bạn có đồng ý làm bạn với "+ plr.getUsername() , "Y/N", JOptionPane.YES_NO_CANCEL_OPTION);
            switch(response){
                case JOptionPane.YES_OPTION:
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.ACCEPT_FRIEND_REQUEST,new Request(plr.getId(),myPlayer.getId())));
                    
                    break;
                    
                case JOptionPane.NO_OPTION:
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.REJECT_FRIEND_REQUEST,new Request(plr.getId(),myPlayer.getId())));
                    break;
                    
            }
        }
    }//GEN-LAST:event_tblFriendRequestMouseClicked

    private void btnRankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRankActionPerformed
        // TODO add your handling code here:
        rankfrm = new RankFrm(myControl);
        rankfrm.setPlayer(myPlayer);
        rankfrm.setVisible(true);
        
    }//GEN-LAST:event_btnRankActionPerformed

    private void btnJoinGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinGroupActionPerformed
        // TODO add your handling code here:
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_GROUP_PLAYER_NOT_IN,myPlayer.getId()));
        
    }//GEN-LAST:event_btnJoinGroupActionPerformed

    private void btnCreateRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRoomActionPerformed
        // TODO add your handling code here:
        
        myControl.sendData(new ObjectWrapper(ObjectWrapper.CREATE_ROOM,""));
    }//GEN-LAST:event_btnCreateRoomActionPerformed

    private void btnExitRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitRoomActionPerformed
        // TODO add your handling code here:
        myControl.sendData(new ObjectWrapper(ObjectWrapper.EXIT_ROOM,this.room.getId()));
        tblLobby.setVisible(false);
        btnCreateRoom.setEnabled(true);
        btnPlay.setEnabled(false);
        btnExitRoom.setEnabled(false);
    }//GEN-LAST:event_btnExitRoomActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        // TODO add your handling code here:
        myControl.sendData(new ObjectWrapper(ObjectWrapper.START_ROOM,this.room.getId()));
    }//GEN-LAST:event_btnPlayActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void tblRoomInviteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRoomInviteMouseClicked
        // TODO add your handling code here:
        int r = tblRoomInvite.getSelectedRow();
        if (r>=0 && r< tblRoomInvite.getRowCount()){
            int roomid = inviteRoomL.get(r).getId();
            InviteRoomFrm ir = new InviteRoomFrm(myControl,roomid);
            ir.setLocationRelativeTo(this);
            ir.setVisible(true);
        }
    }//GEN-LAST:event_tblRoomInviteMouseClicked

    private void btnNewGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGroupActionPerformed
        // TODO add your handling code here:
        String groupname = JOptionPane.showInputDialog(this, "Nhập tên group");
        Group g = new Group();
        g.setName(groupname);
        ArrayList<PlayerGroup> pgl= g.getPlayerGroupList();
        Player pl = new Player();
        pl.setId(myPlayer.getId());
        pgl.add(new PlayerGroup(g, pl , PlayerGroup.MOD));
        g.setPlayerGroupList(pgl);
        myControl.sendData(new ObjectWrapper(ObjectWrapper.CREATE_GROUP,g));
        
    }//GEN-LAST:event_btnNewGroupActionPerformed

    private void tblGroupRequestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupRequestMouseClicked
        // TODO add your handling code here:
        int r = tblGroupRequest.getSelectedRow();
        
        if(r < tblGroupRequest.getRowCount() && r>=0){
            int ans = JOptionPane.showConfirmDialog(this, "Tham gia vào group");
            PlayerGroup pg = new PlayerGroup();
            pg.setGroup(requestGroupList.get(r));
            pg.setPlayer(myPlayer);
            if(ans == 0){
                myControl.sendData(new ObjectWrapper(ObjectWrapper.ACCEPT_GROUP_REQUEST, pg));
            }
            else if(ans == 1){
                myControl.sendData(new ObjectWrapper(ObjectWrapper.REJECT_GROUP_REQUEST, pg));
            }
        }
    }//GEN-LAST:event_tblGroupRequestMouseClicked

    private void tblGroup2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroup2MouseClicked
        // TODO add your handling code here:
        //group chung
        int r = tblGroup2.getSelectedRow();
        if(r>=0 && r < tblGroup2.getRowCount()){
            Group group = group2List.get(r);
            new GroupFrm(myControl, myPlayer, group).setVisible(true);
        }
    }//GEN-LAST:event_tblGroup2MouseClicked

    private void btnSearchGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchGroupActionPerformed
        // TODO add your handling code here:
        String key = txtSearchGroup.getText();
        if(key != ""){
            myControl.sendData(new ObjectWrapper(ObjectWrapper.SEARCH_GROUP, key));

        }

    }//GEN-LAST:event_btnSearchGroupActionPerformed

    private void historytabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historytabMouseClicked
        // TODO add your handling code here:
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MATCH_HISTORY,""));
    }//GEN-LAST:event_historytabMouseClicked

    private void btnHistoryTabRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryTabRefreshActionPerformed
        // TODO add your handling code here:
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MATCH_HISTORY,""));
    }//GEN-LAST:event_btnHistoryTabRefreshActionPerformed

    private void tblHistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHistoryMouseClicked
        // TODO add your handling code here:
        int r = tblHistory.getSelectedRow();
        if(r>=0 && r < tblHistory.getRowCount()){
            int matchid = Integer.parseInt((String)tblHistory.getValueAt(r, 2));
            myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MATCH_HISTORY_DETAIL,matchid));
        }
    }//GEN-LAST:event_tblHistoryMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateRoom;
    private javax.swing.JButton btnExitRoom;
    private javax.swing.JButton btnHistoryTabRefresh;
    private javax.swing.JToggleButton btnJoinGroup;
    private javax.swing.JButton btnNewGroup;
    private javax.swing.JButton btnPlay;
    private javax.swing.JToggleButton btnRank;
    private javax.swing.JButton btnSearchGroup;
    private javax.swing.JScrollPane friendList;
    private javax.swing.JPanel friendPanel;
    private javax.swing.JPanel groupPane;
    private javax.swing.JTabbedPane historytab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JScrollPane requestPane;
    private javax.swing.JTable tblFriend;
    private javax.swing.JTable tblFriendRequest;
    private javax.swing.JTable tblGroup;
    private javax.swing.JTable tblGroup2;
    private javax.swing.JTable tblGroupRequest;
    private javax.swing.JTable tblHistory;
    private javax.swing.JTable tblLobby;
    private javax.swing.JTable tblRoomInvite;
    private javax.swing.JTextField txtSearchGroup;
    private javax.swing.JTextField txtTotalmatch;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtWinmatch;
    // End of variables declaration//GEN-END:variables
}

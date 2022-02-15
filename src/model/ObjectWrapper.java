package model;
 
import java.io.Serializable;
 
public class ObjectWrapper  implements Serializable{
    private static final long serialVersionUID = 20210811011L;
    public static final int LOGIN_PLAYER = 1;
    public static final int REPLY_LOGIN_PLAYER = 2;
    
    public static final int REGISTER_PLAYER = 3;
    public static final int REPLY_REGISTER_PLAYER = 4;
    public static final int EDIT_PLAYER = 5;
    public static final int REPLY_EDIT_PLAYER = 6;
    public static final int SEARCH_CUSTOMER_BY_NAME = 7;
    public static final int REPLY_SEARCH_CUSTOMER = 8;
    public static final int SERVER_INFORM_CLIENT_NUMBER = 9;
    public static final int ANNOUNCE_STATUS = 10;      // gửi tới player (thông báo tới tất cả người  quen của A trạng thái của A online,offline, playing)
    public static final int FRIEND_REQUEST = 11;     // gửi tới server (player A muốn kết bạn B ) player A want to make friend with B
    public static final int REPLY_FRIEND_REQUEST = 12;
    public static final int ANNOUCE_FRIEND_REQUEST_SUCCESS = 13;    //gửi tới player (player A là B đã đồng ý kết bạn);
    public static final int ACCEPT_FRIEND_REQUEST = 14;     // gửi tới server (player B chấp nhận làm bạn với A) player B accept an request
    public static final int REPLY_ACCEPT_FRIEND = 15; // gửi tới player (cho A biết B đã chấp nhận làm bạn với A)
    public static final int REJECT_FRIEND_REQUEST = 16;     // gửi tới server (player B từ chối làm bạn với A) player B reject an re request
    public static final int REPLY_REJECT_FRIEND_REQUEST = 17;   
    public static final int CREATE_ROOM = 20;   // gửi tới server
    public static final int GET_ROOM = 21;  //gửi về player
    public static final int INVITE_ROOM_SERVER = 22;
    public static final int INVITE_ROOM_CLIENT = 23;
    public static final int ACCEPT_INVITE_ROOM = 24;
    public static final int EXIT_ROOM = 25;
    
    public static final int LOGOUT_PLAYER = 26;
    public static final int REPLY_LOGOUT_PLAYER = 27;    
    
    public static final int JOIN_GROUP = 28;
    public static final int REPLY_JOIN_GROUP = 29;
    public static final int GET_GROUP_LIST = 30;
    public static final int REPLY_GET_GROUP_LIST = 31;
    
    public static final int START_ROOM = 32;
    public static final int GET_PLAYING_ROOM = 33;
    public static final int DRAW_CARD = 34;
    public static final int REPLY_DRAW_CARD = 35;
    public static final int DISCARD_CARD = 36;
    public static final int REPLY_START_ROOM = 37;
    public static final int VICTORY_ROOM = 38;
    public static final int STILL_PLAYING = 39;
    public static final int ANNOUCE_PLAYER_INFO = 40;
    
    public static final int GET_PLAYER_INFO = 41;
    public static final int REPLY_GET_PLAYER_INFO = 42;
    
    public static final int GET_RANK = 43;
    public static final int REPLY_GET_RANK = 43;
    
    public static final int GET_PLAYERS_WANT_JOIN_GROUP = 46;
    public static final int REPLY_GET_PLAYERS_WANT_JOIN_GROUP = 47;
    
    public static final int ACCEPT_JOIN_REQUEST = 48;
    public static final int REPLY_ACCEPT_JOIN_REQUEST = 49;
    public static final int REJECT_JOIN_REQUEST = 50;
    public static final int REPLY_REJECT_JOIN_REQUEST = 51;
    
    public static final int GET_GROUP_PLAYER_NOT_IN = 52;
    public static final int REPLY_GET_GROUP_PLAYER_NOT_IN = 53;
    
    public static final int UPDATE_GROUP_INFO = 54;
    
    public static final int CREATE_GROUP = 56;
    public static final int REPLY_CREATE_GROUP = 57;
    public static final int UPDATE_ROLE = 58;
    
    // gameplay --------------------------------
    public static final int UPDATE_MATCH = 60;
    public static final int START_MATCH = 61;
    public static final int REPLY_START_MATCH = 61;
    
    public static final int GET_MATCH_HISTORY = 62;
    public static final int REPLY_GET_MATCH_HISTORY = 62;
    
    public static final int GET_MATCH_HISTORY_DETAIL = 64;
    public static final int REPLY_GET_MATCH_HISTORY_DETAIL = 65;
    
    //--------------------------------------------
            
    public static final int GROUP_REQUEST = 127;
    public static final int REPLY_GROUP_REQUEST = 128;
    public static final int ANNOUCE_GROUP_REQUEST = 129;   //Khi A mời B vào group -> SV sẽ gửi yêu cầu này sang cho B
    public static final int ACCEPT_GROUP_REQUEST = 130;
    public static final int REJECT_GROUP_REQUEST = 131;
    public static final int REPLY_ACCPET_GROUP_REQUEST = 132;
    public static final int REPLY_REJECT_GROUP_REQUEST = 133;
    public static final int ANNOUCE_GROUP_REQUEST_SUCCESS = 134;
    public static final int SEARCH_GROUP = 135;
    public static final int REPLY_SEARCH_GROUP = 136;
    public static final int GET_GROUP = 139;
    public static final int REPLY_GET_GROUP = 140;
    public static final int GET_GROUP_MEMBER = 150;
    public static final int REPLY_GET_GROUP_MEMBER = 151;
    public static final int OUT_GROUP = 152;
    public static final int REPLY_OUT_GROUP =153;
    public static final int GET_GROUP_BY_ID = 160;
    public static final int ANNOUCE_FRIEND_REQUEST = 161;
    
     
    private int performative;
    private Object data;
    public ObjectWrapper() {
        super();
    }
    public ObjectWrapper(int performative, Object data) {
        super();
        this.performative = performative;
        this.data = data;
    }
    public int getPerformative() {
        return performative;
    }
    public void setPerformative(int performative) {
        this.performative = performative;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }   
}   
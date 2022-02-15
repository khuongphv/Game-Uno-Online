package model;

import java.io.Serializable;

public class PlayerGroup implements Serializable {
        private static final long serialVersionUID = 73963153754540L;
        public static final int MOD = 1;
        public static final int MEM = 2;
        public static final int WANT_JOIN = 4;
        public static final int REQUEST = 3;

        private Group group;
        private Player player;
        private int role;

        public PlayerGroup() {
            super();
        }

        public PlayerGroup(Group group, Player player, int role) {
            super();
            this.group = group;
            this.player = player;
            this.role = role;
        }

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
        
}

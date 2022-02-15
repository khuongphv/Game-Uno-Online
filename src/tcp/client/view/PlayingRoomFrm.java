
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp.client.view;

import tcp.client.control.ClientCtr;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.ObjectWrapper;
import model.Player;
import model.PlayingPlayer;
import model.Room;
import model.UnoPack;

/**
 *
 * @author fake1
 */
public class PlayingRoomFrm extends javax.swing.JFrame {
    private ClientCtr myControl;
    private Room room;
    private int myIndex;
    private int second;
    /**
     * Creates new form PlayingRoomFrm
     */
    public PlayingRoomFrm() {
        initComponents();
    }
    
    public PlayingRoomFrm(ClientCtr myControl) {
        initComponents();
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.GET_PLAYING_ROOM, this));
        myControl.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.VICTORY_ROOM, this));
        this.myControl = myControl;
        this.room = new Room();
        
        Image img = new ImageIcon("src\\assets\\card\\card-back.png").getImage().getScaledInstance(388/3, 562/3, Image.SCALE_SMOOTH);
        jLabelDrawCard.setIcon(new ImageIcon(img));
        second =0;
        cdTimer();
        
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    public void setIndex(int index){
        this.myIndex = index;
    }
    
    public void viewInit(){
        getCardList();
        getPlayerList();
        Image img = new ImageIcon("src\\assets\\card\\"+this.room.getDiscardpile().get(this.room.getDiscardpile().size()-1)+".png").getImage();
        jLabelDisCard.setIcon(new ImageIcon(img.getScaledInstance(388/3, 562/3, Image.SCALE_SMOOTH)));
        
        
        jButtonDiscard.setEnabled(false);
        
        if (this.room.getCurrentTurn().getPplindex() == this.myIndex){
            jLabelCurrentP.setText("Lượt của bạn");
            jButtonDraw.setEnabled(true);
        } else {
            jButtonDraw.setEnabled(false);
            jLabelCurrentP.setText("Lượt của "+this.room.getCurrentTurn().getName());
        }
     
    }
    
    public void getCardList(){
        ArrayList<String> cardList = this.room.getPlayingplayerList().get(this.myIndex).getListcard();
        String[] columNames={"Image",""};
            Object[][] value=new Object[cardList.size()][2];
        for (int i=0; i<cardList.size(); i++){
            Image img = new ImageIcon("src\\assets\\card\\"+cardList.get(i)+".png").getImage().getScaledInstance(77, 112, Image.SCALE_SMOOTH);
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon(img));
            value[i][0] = label;
        }
            
        jTableCardList.setModel(new DefaultTableModel(value,columNames));
        
//        for (int i=0; i<cardList.size(); i++){
//            Image img = new ImageIcon("src\\assets\\card\\"+cardList.get(i)+".png").getImage().getScaledInstance(77, 112, Image.SCALE_SMOOTH);
//            JLabel label = new JLabel();
//            label.setIcon(new ImageIcon(img));
//            jTableCardList.setValueAt(label, i, 0);
//        }
        jTableCardList.getColumn("Image").setCellRenderer(new CellRenderer());
        
//        
            
    }
    
    public void getPlayerList(){
        ArrayList<PlayingPlayer> playerList = this.room.getPlayingplayerList();
        
        String[] columNames={"Username","Number of remain card"};
            String[][] value=new String[playerList.size()][4];
            for(int i=0;i<playerList.size();i++)
            {
                value[i][0]= playerList.get(i).getPlayer().getUsername();
                value[i][1]= playerList.get(i).getListcard().size()+"";
 
            }
        jTablePlayerList.setModel(new DefaultTableModel(value,columNames));

    }
    
    public void showRoomResult(String username){
        if (this.room.getPlayingplayerList().get(myIndex).getPlayer().getUsername().equals(username))
            JOptionPane.showMessageDialog(this,"YOU WIN");
        else  JOptionPane.showMessageDialog(this,"YOU LOSE \n"+username+" win");
        this.dispose();
        
    }
    
    public void receivedDataProcessing(ObjectWrapper data) {
        switch(data.getPerformative()){
            case ObjectWrapper.GET_PLAYING_ROOM:
                this.room = (Room)data.getData();
                second = Room.TURN_SECS;
                viewInit();
                break;
            
            case ObjectWrapper.VICTORY_ROOM:
                String username = (String)data.getData();
                showRoomResult(username);
                this.dispose();
                break;
            
        }
    }
    
    class CellRenderer implements TableCellRenderer {
 
        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
 
            TableColumn tb = jTableCardList.getColumn("Image");
            tb.setMinWidth(90);
            tb.setWidth(90);
            jTableCardList.setRowHeight(115);
 
            return (Component) value;
        }
        
 
    }
    
    public void cdTimer(){
            new Timer(1000, new ActionListener(){
              
                @Override
                public void actionPerformed(ActionEvent e){
                    second--;
                    jLabelCountdownTime.setText("Thời gian còn lại: "+second+" s");
                }
                
            } 
            ).start();  
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCardList = new javax.swing.JTable();
        jButtonDraw = new javax.swing.JButton();
        jLabelDisCard = new javax.swing.JLabel();
        jButtonDiscard = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePlayerList = new javax.swing.JTable();
        jLabelCurrentP = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabelDrawCard = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelCountdownTime = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableCardList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                ""
            }
        ));
        jTableCardList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCardListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCardList);

        jButtonDraw.setText("Bốc bài");
        jButtonDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDrawActionPerformed(evt);
            }
        });

        jLabelDisCard.setText("Chồng bài bỏ");

        jButtonDiscard.setText("Đánh bài");
        jButtonDiscard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDiscardActionPerformed(evt);
            }
        });

        jTablePlayerList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Username", "Số lá còn lại"
            }
        ));
        jScrollPane2.setViewportView(jTablePlayerList);

        jTabbedPane1.addTab("Người chơi", jScrollPane2);

        jLabelCurrentP.setText("Giờ đến lượt player :");

        jButton3.setText("Thoát");

        jLabelDrawCard.setText("Chồng bài bốc");

        jLabel1.setText("Bài bỏ");

        jLabel2.setText("Bài bốc");

        jLabelCountdownTime.setText("Thời gian còn lại:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCurrentP)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelDisCard, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jButtonDiscard)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabelDrawCard, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(jButtonDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabelCountdownTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelCurrentP)
                        .addGap(14, 14, 14)
                        .addComponent(jLabelCountdownTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDisCard, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDrawCard, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonDiscard, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDiscardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDiscardActionPerformed
        // TODO add your handling code here:
        int index = jTableCardList.getSelectedRow();
        if (index>-1 && jTableCardList.getRowCount() > index ){
            myControl.sendData(new ObjectWrapper(ObjectWrapper.DISCARD_CARD,index));
        }
    }//GEN-LAST:event_jButtonDiscardActionPerformed

    private void jTableCardListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCardListMouseClicked
        // TODO add your handling code here:
        int index = jTableCardList.getSelectedRow();
        if (this.myIndex== this.room.getCurrentTurn().getPplindex()  && index>-1 && jTableCardList.getRowCount() > index ){
            String card = this.room.getPlayingplayerList().get(this.myIndex).getListcard().get(index);
            String dcard = this.room.getDiscardpile().get(this.room.getDiscardpile().size()-1);
            if (card.charAt(2)=='4' || card.charAt(2)=='5' || dcard.charAt(2)=='4' || dcard.charAt(2)=='5' 
                    || card.charAt(0)==dcard.charAt(0) || (card.charAt(1)==dcard.charAt(1) && card.charAt(2)==dcard.charAt(2)) )
            {
                jButtonDiscard.setEnabled(true);
            } else {
                jButtonDiscard.setEnabled(false);
            }
            
        }
    }//GEN-LAST:event_jTableCardListMouseClicked

    private void jButtonDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDrawActionPerformed
        // TODO add your handling code here:
        myControl.sendData(new ObjectWrapper(ObjectWrapper.DRAW_CARD,""));
    }//GEN-LAST:event_jButtonDrawActionPerformed

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
            java.util.logging.Logger.getLogger(PlayingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlayingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlayingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlayingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlayingRoomFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonDiscard;
    private javax.swing.JButton jButtonDraw;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCountdownTime;
    private javax.swing.JLabel jLabelCurrentP;
    private javax.swing.JLabel jLabelDisCard;
    private javax.swing.JLabel jLabelDrawCard;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableCardList;
    private javax.swing.JTable jTablePlayerList;
    // End of variables declaration//GEN-END:variables
}

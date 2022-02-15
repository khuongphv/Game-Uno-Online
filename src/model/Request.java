/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author fake1
 */
public class Request implements Serializable{
    private int sendId;
    private int receiveID;

    public Request() {
        super();
    }

    public Request(int sendId, int receiveID) {
        super();
        this.sendId = sendId;
        this.receiveID = receiveID;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(int receiveID) {
        this.receiveID = receiveID;
    }
    
    
}

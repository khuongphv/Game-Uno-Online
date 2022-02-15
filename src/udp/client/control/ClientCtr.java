package udp.client.control;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
 
import model.IPAddress;
import model.ObjectWrapper;
 
 
public class ClientCtr {
    private DatagramSocket myClient;    
    private IPAddress serverAddress = new IPAddress("localhost", 5555); //default server address
    private IPAddress myAddress = new IPAddress("localhost", 6666); //default client address
     
    public ClientCtr(){
        
    }
     
    public ClientCtr( int clientPort){
        myAddress.setPort(clientPort);
    }
     
    public ClientCtr( IPAddress serverAddr){
        serverAddress = serverAddr;
    }
     
    public ClientCtr( IPAddress serverAddr, int clientPort){
        serverAddress = serverAddr;
        myAddress.setPort(clientPort);
    }
     
     
    public boolean open(){
        boolean check = false;
        while (check== false){
        try {
            myClient = new DatagramSocket(myAddress.getPort());
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
//            view.setServerandClientInfo(serverAddress, myAddress);
            System.out.println("UDP client is running at the host: " + myAddress.getHost() + ", port: " + myAddress.getPort());
            check = true;
        }catch(Exception e) {
            //e.printStackTrace();
            //System.out.println("Error to open the datagram socket!");
            myAddress.setPort(myAddress.getPort()+1);
        }
        }
        return check;
        
    }
     
    public boolean close(){
        try {
            myClient.close();
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error to close the datagram socket!");
            return false;
        }
        return true;
    }
     
    public boolean sendData(ObjectWrapper data){
        try {
            //prepare the buffer and write the data to send into the buffer
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.flush();            
             
            //create data package and send
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverAddress.getHost()), serverAddress.getPort());
            myClient.send(sendPacket);
             
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in sending data package");
            return false;
        }
        return true;
    }
     
    public ObjectWrapper receiveData(){
        ObjectWrapper result = null;
        try {   
            //prepare the buffer and fetch the received data into the buffer
            byte[] receiveData = new byte[16384];
            DatagramPacket receivePacket = new  DatagramPacket(receiveData, receiveData.length);
            myClient.receive(receivePacket);
             
            //read incoming data from the buffer 
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            result = (ObjectWrapper)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in receiving data package");
        }
        return result;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp.dao;

import java.sql.Connection;
import java.sql.DriverManager;


public class DAO {
    protected static Connection con;//declare the connection
    
    public DAO(){
        if(con == null){
            String dbUrl = "jdbc:mysql://localhost:3306/uno";
            String dbClass = "com.mysql.cj.jdbc.Driver";
 
            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection (dbUrl, "root", "");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

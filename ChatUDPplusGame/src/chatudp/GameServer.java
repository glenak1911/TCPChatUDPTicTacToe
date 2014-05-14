/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author Glen
 */
public class GameServer extends Thread {
    int port;
    String moveMade;
    int test;
    public GameServer(int p){
        this.port = p;
    }
  
    @Override
    public void run(){
       ServerSocket welcomeSocket = null;
        try {
            welcomeSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Game Server started on port "+port+" ...");
      
                
        Socket connectionSocket = null;
        try {
            connectionSocket = welcomeSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
                UDPChatJFrame.jLabel6.setText("CONNECTED");
                UDPChatJFrame.jLabel6.setForeground(Color.green);
       
        
                System.out.println("Game connection establishted!");
           
                
        
        
                while(true) {
                try {
                    
                DataInputStream  inToServer = 
                 new DataInputStream(connectionSocket.getInputStream()); 
                BufferedReader reader = new BufferedReader(new InputStreamReader(inToServer,"UTF-8"));
                
                moveMade = reader.readLine();
                if(isInteger(moveMade)){
                   
                    UDPChatJFrame.whosFirst(moveMade);
                }else{
                System.out.println(moveMade);
                UDPChatJFrame.update(moveMade);
                }
                } catch (IOException ex) {
                    Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                }
        
            }
    
    public static boolean isInteger(String s) {
        try { 
                Integer.parseInt(s); 
            } catch(NumberFormatException e) { 
                return false; 
        }
    
        return true;
}
            
    }


    
    
    
    

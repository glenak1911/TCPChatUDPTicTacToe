
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;
import java.io.*; 
import java.net.*; 
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Glen
 */
public class GameClient extends Thread{
    public void run(){
    String sentence; 
        String modifiedSentence; 

        BufferedReader inFromUser = 
          new BufferedReader(new InputStreamReader(System.in)); 
        try (Socket clientSocket = new Socket("hostname", 6789)) {
            DataOutputStream outToServer = 
              new DataOutputStream(clientSocket.getOutputStream()); 
            
            BufferedReader inFromServer = 
              new BufferedReader(new
              InputStreamReader(clientSocket.getInputStream())); 

            sentence = inFromUser.readLine(); 

            outToServer.writeBytes(sentence + '\n'); 

            modifiedSentence = inFromServer.readLine(); 

            System.out.println("FROM SERVER: " + modifiedSentence);
        }
        catch (UnknownHostException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }    }
    
    public static void sendPlay(){
        
    }

}

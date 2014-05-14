/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;

/**
 *
 * @author Glen
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.crypto.*;


class UDPServer extends Thread{
    private static String ALGORITHM="RSA";
    Calendar calendar = new GregorianCalendar();
    //private DatagramSocket serverSocket;
    private String sentence;
    private int myport;
    private int hour = calendar.get(Calendar.HOUR);
    private int minute = calendar.get(Calendar.MINUTE);
    private int second = calendar.get(Calendar.SECOND);
    private DatagramPacket receivePacket;
    private static String newText;
    private ObjectInputStream inputStream;
    public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
    public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
    
  public UDPServer(int portNumber){
      this.myport = portNumber;
      
  }
  
  
  
//  public static String decryption(byte[] m){
//        
//      try{
//                KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
//                SecretKey myDesKey = keygenerator.generateKey();
//                Cipher desCipher;
//                // Create the cipher 
//                desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//                // Initialize the cipher for encryption
//                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
//                // Decrypt the text
//                byte[] textDecrypted = desCipher.doFinal(m);
//                newText = new String(textDecrypted);
//                
//       }
//        catch(NoSuchAlgorithmException e){
//			System.out.println("No Such Algorithm!");
//		}catch(NoSuchPaddingException e){
//			System.out.println("No Such Padding!");
//		}catch(InvalidKeyException e){
//			System.out.println("Invalid Key!");
//		}catch(IllegalBlockSizeException e){
//			System.out.println("Illegal Block Size!");
//		}catch(BadPaddingException e){
//			System.out.println("Bad Padding!");
//		}  
//       return newText;
//  }
         
    @Override
    public void run(){
      while(true) 
        { 
            DatagramSocket serverSocket = null;
            byte[] receiveData = new byte[1024]; 
            //byte[] sendData  = new byte[1024];
            try {
            serverSocket = new DatagramSocket(myport);
            } catch (SocketException ex) {
            System.out.println("Server canot connect to port "+myport+"!!");
            }
            System.out.println("The Server is running on port:"+myport);
            while(true){
                
                    receivePacket = 
                        new DatagramPacket(receiveData, receiveData.length); 
                    try{
                    serverSocket.receive(receivePacket); 
                    }catch (IOException e){
                        System.out.println("Cannot receive message!");
                    }
                    
                    sentence = new String(receivePacket.getData()).trim();
                    sentence = removeCheckSum(sentence);
                    sentence = decryption(sentence);
                    sentence = sentence.replace("7=T"," ");
                    sentence = sentence.replace("Q=T"," ");
                    //System.out.println("The text to be decrypted is: "+receivePacket.getData());
                    //sentence = decryption(receivePacket.getData());
                    //System.out.println("The decrypted data is: "+ sentence);
                        UDPChatJFrame.chatArea.append((("("+hour+":"+minute+":"+second+")"+":"+sentence))+"\n");
                        System.out.println("Message received from "+receivePacket.getAddress()+":"+receivePacket.getPort());
                        sentence ="";
                } 
                
                
        }
    }

//    public static String decrypt(byte[] text, PrivateKey key) {
//    byte[] dectyptedText = null;
//    try {
//      // get an RSA cipher object and print the provider
//      final Cipher cipher = Cipher.getInstance(ALGORITHM);
//
//      // decrypt the text using the private key
//      cipher.init(Cipher.DECRYPT_MODE, key);
//      dectyptedText = cipher.doFinal(text);
//
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//
//    return new String(dectyptedText);
//  }

    
//    public static String decryption(byte[] text, PrivateKey key) {
//    byte[] dectyptedText = null;
//    try {
//      // get an RSA cipher object and print the provider
//      final Cipher cipher = Cipher.getInstance(ALGORITHM);
//
//      // decrypt the text using the private key
//      cipher.init(Cipher.DECRYPT_MODE, key);
//      dectyptedText = cipher.doFinal(text);
//
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//
//    return new String(dectyptedText);
//  }
    
    public String removeCheckSum(String m){
        String message = m.substring(8, m.length());
        System.out.println(message);
        String checksum = m.replace(message,"");
        System.out.println(checksum);
        
        //converts string value to a byte array
        byte bytes[]=message.getBytes();
        //creates CRC32 checksum object
        Checksum checksum1 = new CRC32();
        //creates checksum out of byte array
        checksum1.update(bytes,0,bytes.length);
        //converts checksum to long value
        long lngCheckSum = checksum1.getValue();
        //converts long checksum to uppercase hex string
        String checkSumValue = Long.toHexString(lngCheckSum).toUpperCase();
        System.out.println(checkSumValue);
        if(checksum.equals(checkSumValue)){
            return message;
        }
        else{
            message = "Checksum Failed! Message Corrupted!";
           return message; 
        }
        
        }
    
    public String decryption(String m){
      
      //FileReader fr = new FileReader(args[0]); 
      //BufferedReader br = new BufferedReader(fr); 
      String ciphertext = m; 
      ciphertext = ciphertext.toUpperCase(); 
      
      int shiftKey = 3; 
      shiftKey = shiftKey % 26;  
       
      String plainText = ""; 
  
      for (int i=0; i<ciphertext.length(); i++){ 
         int asciiValue = (int) ciphertext.charAt(i); 
         if (asciiValue < 65 || asciiValue > 90){ 
            plainText += ciphertext.charAt(i); 
            //continue; 
         } 
          
         int basicValue = asciiValue - 65; 
        
         int newAsciiValue = -1000; 
  
         if (basicValue - shiftKey < 0){ 
           
            newAsciiValue = 90 - (shiftKey - basicValue) + 1; 
         } 
         else{ 
           newAsciiValue = 65 + (basicValue - shiftKey); 
          } 
         plainText += (char) newAsciiValue; 
  
       } 
        System.out.print(plainText);    
        return plainText;          
    }
    
} 
    


        

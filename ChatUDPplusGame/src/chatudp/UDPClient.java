/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;

/**
 *
 * @author Glen
 */
import java.io.*; 
import java.net.*; 
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

  


class UDPClient extends Thread{
    Calendar calendar = new GregorianCalendar();
    private String userName;
    private int serverPort;
    private byte[] sendData = new byte[1024];
    private InetAddress IPAddress;
    private DatagramSocket clientSocket;
    private String message;
    private DatagramPacket sendPacket;
    private int hour = calendar.get(Calendar.HOUR);
    private int minute = calendar.get(Calendar.MINUTE);
    private int second = calendar.get(Calendar.SECOND);
    private int num =0;
    private boolean status;
    private String address;
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
    public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
    private ObjectInputStream inputStream;
    
    public UDPClient(String n, int x, String add) {
        this.userName= n;
        this.serverPort=x;
        this.address = add;
    }
    
   public void clearTextBox()
    {
        UDPChatJFrame.chatTextBox.setText("");
    }
        
   @Override
   public void run(){
       try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            System.out.println("Cannot connect to port!");
        }
       try {
        IPAddress = InetAddress.getByName(address);
        } 
        catch (UnknownHostException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       UDPChatJFrame.chatArea.append("Welcome to the A+ chat "+userName+"!"+"\n");
    }
   
   public int getSocket(){
       int cport = clientSocket.getPort();
       return cport;
   }
   
   
   public void sendMessage() throws IllegalBlockSizeException, BadPaddingException, InterruptedException, IOException, ClassNotFoundException
   {
       message ="";
       message = userName+":"+UDPChatJFrame.chatTextBox.getText();
       UDPChatJFrame.chatArea.append("("+hour+":"+minute+":"+second+")"+":"+message+"\n");
       clearTextBox();
//       if (!areKeysPresent()) {
//        generateKey();
//      }
       message = encryption(message);
       message = addCheckSum(message)+message;
       if(ack()==true){
        
      //inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
      //final PublicKey publicKey = (PublicKey) inputStream.readObject();   
      //sendData = message.getBytes();  
      
      System.out.println(message);
      sendData = message.getBytes();
        //sendData = encryption(message);
        //System.out.println("The encrypted data is "+sendData);
        sendPacket = 
         new DatagramPacket(sendData, sendData.length, IPAddress, serverPort); 
            try {
                clientSocket.send(sendPacket); 
                UDPChatJFrame.chatArea.append("("+hour+":"+minute+":"+second+")"+":"+"Message sent!"+"\n");
            } catch (IOException ex) {
                System.out.println("Packet cannot be sent!");
            }
            message ="";
       }else
       {
           Thread.sleep(6000);
           UDPChatJFrame.chatArea.append("("+hour+":"+minute+":"+second+")"+":"+"Send Failed!"+"\n");
           Thread.sleep(6000);
           UDPChatJFrame.chatArea.append("("+hour+":"+minute+":"+second+")"+":"+"Resending! Please wait!"+"\n");
           Thread.sleep(4000);
           sendPacket = 
         new DatagramPacket(sendData, sendData.length, IPAddress, serverPort); 
            try {
                clientSocket.send(sendPacket); 
                Thread.sleep(5000);
                UDPChatJFrame.chatArea.append("("+hour+":"+minute+":"+second+")"+":"+"Message sent!"+"\n");
            } catch (IOException ex) {
                System.out.println("Packet cannot be sent!");
            }
            
       }
   }
   
   
   
   public boolean ack()
   {
       Random random = new Random();
       num=random.nextInt(5);
       if(num==2){
           return false;
       }
       else
       {
           return true;
       }   
   }

      
    //creates CRC32 checksum and returns it to be appended to the sentence
    public static String addCheckSum(String checkSumValue)
        {
            checkSumValue.trim();
            //converts string value to a byte array
            byte bytes[]=checkSumValue.getBytes();
            //creates CRC32 checksum object
            Checksum checksum = new CRC32();
            //creates checksum out of byte array
            checksum.update(bytes,0,bytes.length);
            //converts checksum to long value
            long lngCheckSum = checksum.getValue();
            //converts long checksum to uppercase hex string
            checkSumValue = Long.toHexString(lngCheckSum).toUpperCase();
            System.out.println(checkSumValue);
            return checkSumValue;
        }
    
    public String encryption(String m){
      
      //FileReader fr = new FileReader(args[0]); 
      //BufferedReader br = new BufferedReader(fr); 
      String plaintext = m; 
      plaintext = plaintext.toUpperCase(); 
      
      int shiftKey = 3; 
      shiftKey = shiftKey % 26;  
       
      String cipherText = ""; 
  
      for (int i=0; i<plaintext.length(); i++){ 
         int asciiValue = (int) plaintext.charAt(i); 
         if (asciiValue < 65 || asciiValue > 90){ 
            cipherText += plaintext.charAt(i); 
           // continue; 
         } 
          
         int basicValue = asciiValue - 65; 
         int newAsciiValue = 65 + ((basicValue + shiftKey) % 26)  ; 
         cipherText += (char) newAsciiValue; 
  
       } 
       System.out.print(cipherText);     
       return cipherText;   
      }

} 


    
//    public static void generateKey() {
//            try {
//            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
//            keyGen.initialize(2048);
//            final KeyPair key = keyGen.generateKeyPair();
//
//            File privateKeyFile = new File(PRIVATE_KEY_FILE);
//            File publicKeyFile = new File(PUBLIC_KEY_FILE);
//
//            // Create files to store public and private key
//            if (privateKeyFile.getParentFile() != null) {
//                privateKeyFile.getParentFile().mkdirs();
//            }
//            privateKeyFile.createNewFile();
//
//            if (publicKeyFile.getParentFile() != null) {
//                publicKeyFile.getParentFile().mkdirs();
//            }
//            publicKeyFile.createNewFile();
//
//            // Saving the Public key in a file
//            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
//                new FileOutputStream(publicKeyFile));
//            publicKeyOS.writeObject(key.getPublic());
//            publicKeyOS.close();
//
//            // Saving the Private key in a file
//            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
//                new FileOutputStream(privateKeyFile));
//            privateKeyOS.writeObject(key.getPrivate());
//            privateKeyOS.close();
//            } catch (Exception e) {
//            e.printStackTrace();
//            }
//
//
//        }
    
//    public static boolean areKeysPresent() {
//
//        File privateKey = new File(PRIVATE_KEY_FILE);
//        File publicKey = new File(PUBLIC_KEY_FILE);
//
//        if (privateKey.exists() && publicKey.exists()) {
//        return true;
//        }
//        return false;
//    }
//}
    
//    public static byte[] encrypt(String text, PublicKey key) {
//    byte[] cipherText = null;
//    try {
//      // get an RSA cipher object and print the provider
//      final Cipher cipher = Cipher.getInstance(ALGORITHM);
//      // encrypt the plain text using the public key
//      cipher.init(Cipher.ENCRYPT_MODE, key);
//      cipherText = cipher.doFinal(text.getBytes());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return cipherText;
//  }
//    
//    }


    





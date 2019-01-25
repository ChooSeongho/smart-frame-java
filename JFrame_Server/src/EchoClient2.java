import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */
public class EchoClient2 {
   static final String endMessage = ".";
   public static void main(String[] args) {
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      JSON json;
      try {
         System.out.println("Welcome to the Echo client.\n" +
            "What is the name of the server host?");
         json = new JSON();
//         String hostName = br.readLine();
         String hostName = "localhost";
         if (hostName.length() == 0) // if user did not enter a name
            hostName = "localhost";  //   use the default host name
         System.out.println("What is the port number of the server host?");
//         String portNum = br.readLine();
         String portNum = "2017";
         if (portNum.length() == 0)
            portNum = "2017";          // default port number
         EchoClientHelper2 helper = 
            new EchoClientHelper2(hostName, portNum);
         boolean done = false;
         String message, echo;
//         message = br.readLine( );
         String dir = "C:/Users/LAB_CHOO/Desktop/";
         File file = new File(dir+"good3.jpg");
//         BufferedImage bufferedImage = ImageIO.read(file);
//         
//         ByteArrayOutputStream output = new ByteArrayOutputStream();
//         ImageIO.write(bufferedImage, "jpg", output);
//         
//         byte[] image = output.toByteArray();
//         String send = json.makeJSONArray(3,"And", "12:13:15:63:vb", "choo", Base64EnDe.fileToString(file));
         String send = json.makeJSONArray(3,"And", "12:13:15:63:vb", "get", "");
         helper.send(send);
         System.out.println("send = "+send);
         helper.done();
//         while (!done) {
//            System.out.println("Enter a line to receive an echo "
//               + "from the server, or a single period to quit.");
//            if ((message.trim()).equals (endMessage)){
//               done = true;
//               helper.done( );
//            }
//            else {
//               echo = helper.getEcho( message);
//               System.out.println(echo);
//            }
//          } // end while
      } // end try  
      catch (Exception ex) {
         ex.printStackTrace( );
      } //end catch
   } //end main
} // end class

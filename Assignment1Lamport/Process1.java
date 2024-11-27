package WordDistributor.Assignment1Lamport;

import java.net.*;
import java.io.*;

public class Process1 {

    static int lamportClock = 0;
    public static void main(String... args) {

        // Reference: https://www.digitalocean.com/community/tutorials/java-socket-programming-server-client

        // Receive
        try(ServerSocket server = new ServerSocket(1081)){
            System.out.println("Waiting for a client: ");
            lamportClock++;
            
            while(true) {
                try(Socket connection = server.accept()){
                    lamportClock++;
                    InputStream in = connection.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(in);
                    lamportClock++;
                    String word = (String) ois.readObject();
                    int receivedClock = (int) ois.readObject();
                    
                    lamportClock++;
                    System.out.println("The local lamport clock is " + lamportClock);

                    lamportClock = Math.max(receivedClock, lamportClock)+1;
                    System.out.println("Received word: '" + word + "'");
                    System.out.println("Received lamport clock: " + receivedClock);
                    System.out.println("lamport clock after recieve: " + lamportClock); //

               
                    Thread.sleep(1000); // Wait 1 second to ensure processes are ready to send words back


                    // Send back
                    try (Socket socket = new Socket("localhost", 1080)) {
                        lamportClock++;

                        OutputStream out = socket.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(out);
                        lamportClock++;
                        oos.writeObject(word);  // Send the word back
                        oos.writeObject(lamportClock); // local clock
                        oos.writeObject(receivedClock); // received one
                        oos.flush();

                        
                        System.out.println("Sent back word: '" + word + "'");
                        System.out.println(" ");
                    } catch (Exception ex) {
                        System.err.println("Error sending word back: " + ex.getMessage());
                    }
                    
                    
                }catch(Exception ex) {
                    System.err.println(ex);
                }
                
                
            }
            }catch(Exception ex) {
                System.err.println(ex);
            }


        }

}
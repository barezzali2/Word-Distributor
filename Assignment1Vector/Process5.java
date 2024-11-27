package WordDistributor.Assignment1Vector;

import java.net.*;
import java.util.Arrays;
import java.io.*;

public class Process5 {

    static int vectorClock[] = new int[6];

    public static void main(String... args) {

        // Receive the word
        try(ServerSocket server = new ServerSocket(1085)){
            System.out.println("Waiting for a client: ");
            vectorClock[5]++;
            
            
            while(true) {
                    try(Socket connection = server.accept()){
                    vectorClock[5]++;
                    InputStream in = connection.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(in);
                    vectorClock[5]++;
                    String word = (String) ois.readObject();
                    int[] receivedClock = (int[]) ois.readObject();
                    
                    vectorClock[5]++;
                    System.out.println("The vector clock is " + Arrays.toString(vectorClock));

                    
                    for(int i = 0; i < vectorClock.length; i++) {
                        vectorClock[i] = Math.max(receivedClock[i], vectorClock[i]);
                    }
                    vectorClock[5]++;

                    
                    System.out.println("Received word: '" + word + "'");
                    System.out.println("Received vector clock: " + Arrays.toString(vectorClock));


                    
                    Thread.sleep(1000); // Wait 1 second to ensure processes are ready to send words back


                    // Send back
                    try (Socket socket = new Socket("localhost", 1080)) {
                        vectorClock[5]++;

                        OutputStream out = socket.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(out);

                        vectorClock[5]++;
                        oos.writeObject(word);  // Send the word back
                        oos.writeObject(vectorClock);
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

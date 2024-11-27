
package WordDistributor.Assignment1Lamport;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Main {

    static int lamportClock = 0;
    
    public static void main(String... args) {


        int ports[] = {1081, 1082, 1083, 1084, 1085};
        Random rand = new Random();
        lamportClock++;
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a paragraph: ");
        String par = input.nextLine();
        lamportClock++;
        
        
        String str[] = par.split(" ");
        lamportClock++;
        
        
        // References: https://www.geeksforgeeks.org/split-string-java-examples/
        // References: https://docs.oracle.com/javase/8/docs/api///?java/net/Socket.html
            
            try{
                for(String word : str) {
                    int randSocket = rand.nextInt(ports.length);
                    lamportClock++;
                    Socket socket = new Socket("localhost", ports[randSocket]);

                    OutputStream out = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    lamportClock++;
                    System.out.println("Sending word: '" + word + "' to port: " + ports[randSocket]);
                    lamportClock++;
                    oos.writeObject(word);
                    oos.writeObject(lamportClock);
                    oos.flush();
            
                    
                    System.out.println("The lomport clock of word " + word + " is " + lamportClock);
                    System.out.println(" ");
                    
                    socket.close();

                }


                
                try (ServerSocket server = new ServerSocket(1080)) {
                    lamportClock++;

                    List<Pair> receivedWords = new ArrayList<>();
                    System.out.println("Waiting 15 seconds to receive the words...");
                    System.out.println(" ");
                    lamportClock++;

                    Thread.sleep(15000); // 15 seconds delay


                    for (int i = 0; i < str.length; i++) { // Expect the same number of words as sent
                        try (Socket connection = server.accept()) {
                            lamportClock++;
                            InputStream in = connection.getInputStream();
                            ObjectInputStream ois = new ObjectInputStream(in);
                
                            lamportClock++;
                            String receivedWord = (String) ois.readObject();
                            int receivedLamport = (int) ois.readObject(); // the clock to be incremented
                            int firstClock = (int) ois.readObject(); // Receiving back the clock that was sent in the first place
                            

                            lamportClock = Math.max(receivedLamport, lamportClock)+1; //
                            
                            
                            receivedWords.add(new Pair(receivedWord, firstClock));
                            System.out.println("Received word: '" + receivedWord + "'");
                            // lampordClock++;
                            System.out.println("First place clock: " + firstClock); //
                            System.out.println("Received lamport clock: " + receivedLamport); //
                            System.out.println("local lamport clock: " + lamportClock);

                            System.out.println(" ");

                        } catch (Exception ex) {
                            System.err.println("Error receiving word: " + ex.getMessage());
                        }
                    }

                    
                    // Reference: https://chatgpt.com/
                    
                // Sort words based on logical clock
                receivedWords.sort(Comparator.comparingInt(Pair::getClock));
                
                lamportClock++;
                System.out.println("All words received. Sorted paragraph:");
                System.out.println(receivedWords.stream().map(Pair::getWord).reduce("", (a, b) -> a + " " + b).trim());

                     
                } catch (Exception ex) {
                    System.err.println("Server error: " + ex.getMessage());
                }

                System.out.println(lamportClock);
                
            

        }catch(Exception ex) {
            System.err.println(ex);
        }
        
    }
    
}
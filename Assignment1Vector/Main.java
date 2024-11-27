
package WordDistributor.Assignment1Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Main {

    static int vectorClock[] = new int[6];
    
    public static void main(String... args) {


        int ports[] = {1081, 1082, 1083, 1084, 1085};
        Random rand = new Random();
        vectorClock[0]++;
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a paragraph: ");
        String par = input.nextLine();
        vectorClock[0]++;
        
        String str[] = par.split(" ");
        vectorClock[0]++;

        // References: https://www.javatpoint.com/socket-programming
        // https://www.geeksforgeeks.org/split-string-java-examples/
        // https://docs.oracle.com/javase/8/docs/api///?java/net/Socket.html
        
            
            try{
                for(String word : str) {
                    int randSocket = rand.nextInt(ports.length);
                    Socket socket = new Socket("localhost", ports[randSocket]);
                    vectorClock[0]++;

                    OutputStream out = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    vectorClock[0]++;
                    System.out.println("Sending word: '" + word + "' to port: " + ports[randSocket]);
                    vectorClock[0]++;
                    oos.writeObject(word);
                    oos.writeObject(vectorClock);
                    oos.flush();
                    
                    
                    System.out.println("The vector clock of word '" + word + "' is " + Arrays.toString(vectorClock));
                    System.out.println(" ");
                    
                    socket.close();

                }


                
                try (ServerSocket server = new ServerSocket(1080)) {
                    vectorClock[0]++;

                    List<Pair<String, int[]>> wordsWithClocks = new ArrayList<>();
                    System.out.println("Waiting 15 seconds to receive the words...");
                    System.out.println(" ");
                    vectorClock[0]++;

                    Thread.sleep(15000); // 15s delay before collecting the words


                    for (int i = 0; i < str.length; i++) { // Expect the same number of words as sent
                        try (Socket connection = server.accept()) {
                            vectorClock[0]++;
                            InputStream in = connection.getInputStream();
                            ObjectInputStream ois = new ObjectInputStream(in);
                
                            vectorClock[0]++;
                            String receivedWord = (String) ois.readObject();
                            int[] receivedClock = (int[]) ois.readObject();


                            for(int j = 0; j < vectorClock.length; j++) {
                                vectorClock[j] = Math.max(receivedClock[j], vectorClock[j]);

                            }
                            vectorClock[0]++;
                            

                            vectorClock[0]++;
                            wordsWithClocks.add(new Pair<>(receivedWord, receivedClock));
                            System.out.println("Received word: '" + receivedWord + "'");
                            vectorClock[0]++;
                            System.out.println("Received vector clock: " + Arrays.toString(vectorClock));
                            System.out.println(" ");

                        } catch (Exception ex) {
                            System.err.println("Error receiving word: " + ex.getMessage());
                        }
                    }

                    wordsWithClocks.sort((p1, p2) -> {
                        int[] clock1 = p1.getValue();
                        int[] clock2 = p2.getValue();
                    
                        for (int i = 0; i < clock1.length; i++) {
                            if (clock1[i] < clock2[i]) {
                                return -1; // clock1 is smaller
                            } else if (clock1[i] > clock2[i]) {
                                return 1; // clock1 is larger
                            }
                        }
                        return 0; // clocks are equal
                    });


                    // References: https://chatgpt.com/
                    // https://www.geeksforgeeks.org/pair-class-in-java/

                    StringBuilder reconstructedParagraph = new StringBuilder();
                    for (Pair<String, int[]> pair : wordsWithClocks) {
                        reconstructedParagraph.append(pair.getKey()).append(" ");
                    }

                    System.out.println("All words received and reordered. Sorted paragraph:");
                    System.out.println(reconstructedParagraph.toString().trim());


                     
                } catch (Exception ex) {
                    System.err.println("Server error: " + ex.getMessage());
                }
                
                
                System.out.println(Arrays.toString(vectorClock));


        }catch(Exception ex) {
            System.err.println(ex);
        }
        
    }
    
}
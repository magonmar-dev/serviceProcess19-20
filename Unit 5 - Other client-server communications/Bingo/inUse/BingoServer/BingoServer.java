package bingoserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BingoServer {

    public static final int PORT = 6000;
    public static final int MAX_CONNECTIONS = 1;
    public static final int MAX_NUMBER = 20;

    public static List<Integer> nums;
    public static boolean gameOver = false;

    public static void main(String[] args) {

        List<ServerThread> threads;
        List<Socket> sockets;

        try {
            ServerSocket server = new ServerSocket(PORT, MAX_CONNECTIONS);
            System.out.println("Listening...");
            while (true) {
                nums = IntStream.rangeClosed(1, MAX_NUMBER) // int (1..20)
                        .boxed() // int -> Integer
                        .collect(Collectors.toList()); // List<Integer>
                Collections.shuffle(nums);

                threads = new ArrayList<>();
                sockets = new ArrayList<>();

                while(!gameOver) {
                    Socket service = server.accept();
                    System.out.println("Client connection established");
                    sockets.add(service);
                    threads.add(new ServerThread(service));

                    for (int i=0; i<sockets.size(); i++) {
                        if(sockets.get(i).isClosed()) {
                            sockets.remove(i);
                            threads.get(i).interrupt();
                            threads.remove(i);
                            System.out.println("Socket disconnected");
                        }
                    }

                    if(sockets.size() == MAX_CONNECTIONS) {
                        System.out.println("Start the game");
                        for (int i = 0; i < threads.size(); i++) {
                            threads.get(i).start();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

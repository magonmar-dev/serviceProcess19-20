package bingoserver;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServerThread extends Thread {

    public static final int NUMBERS_IN_TICKET = 5;

    Socket service;
    List<Integer> ticket;
    int corrects;
    boolean lastAnswer;

    public ServerThread(Socket s) {
        service = s;
        ticket = generateRandomTicket();
        corrects = 0;
        lastAnswer = false;
    }

    private List<Integer> generateRandomTicket() {
        List<Integer> nums = IntStream.rangeClosed(1, BingoServer.MAX_NUMBER)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(nums);
        return nums.stream().limit(NUMBERS_IN_TICKET).sorted(Integer::compare).collect(Collectors.toList());
    }

    @Override
    public void run() {
        ObjectOutputStream socketOut = null;

        int i = 0;
        try {
            socketOut = new ObjectOutputStream(service.getOutputStream());

            socketOut.writeObject(ticket);
            socketOut.flush();

            do {
                if(!BingoServer.gameOver) {
                    sleep(2000);
                    socketOut.writeInt(BingoServer.nums.get(i));
                } else {
                    if (corrects == 5)
                        socketOut.writeInt(-2);
                    else
                        socketOut.writeInt(-1);
                    lastAnswer = true;
                }
                socketOut.flush();

                if (ticket.contains(BingoServer.nums.get(i)))
                    corrects++;
                if (corrects == 5)
                    BingoServer.gameOver = true;

                i++;
            } while(!BingoServer.gameOver || !lastAnswer);

            sleep(2000);
            BingoServer.gameOver = false;
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        } finally {
            try {
                if (socketOut != null)
                    socketOut.close();
            } catch (IOException ex) {}
            try {
                if (service != null)
                    service.close();
            } catch (IOException ex) {}
        }
    }
}

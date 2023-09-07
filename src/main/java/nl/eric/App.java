package nl.eric;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class App {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("Commands: [clear queue, show queue]");

        QueueManager qp = new QueueManager();

        Scanner userInput = new Scanner(System.in);
        while (true) {

            System.out.println("Add an URL to the queue: ");

            String input = userInput.nextLine();


            if (!input.isEmpty()) {

                if ("show queue".equals(input)) {

                    qp.showQueue();
                } else if ("clear queue".equals(input)) {

                    qp.clearQueue();
                } else if (input.contains("https://")) {

                    qp.addUrl(input);
                } else {

                }

            }
        }
    }
}


package nl.eric;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main (String[] args) throws ExecutionException, InterruptedException {

        log.info("Commands: [clear queue, show queue]");

        QueueManager qp = new QueueManager();

        Scanner userInput = new Scanner(System.in);
        while (userInput.hasNext()) {

            log.info("Add an URL to the queue: ");

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
        userInput.close();
    }
}

package nl.eric;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class QueueManager {

    private final ScheduledExecutorService scheduler;
    private Queue<String> myQueue = new ConcurrentLinkedQueue<>();

    public void addUrl (String url) {

        myQueue.add(url);

        showQueue();
    }

    public void showQueue () {

        System.out.println("The following item(s) is/are now on the queue:\n"
                + Arrays.toString(myQueue.toArray()).replace(" ", "\n"));
    }

    public QueueManager() {

        scheduler = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());

        Runnable runnable = () -> {

            try {
                String url = myQueue.remove();

                System.out.println("Started working on the following item: " + url);

                MyPDFWriter.writePDF(URLProcessor.getImageURLs(url), URLProcessor.getFilename(url));
            } catch (IOException | DocumentException | InterruptedException e) {

                System.out.println("Something went wrong: " + e);
            } catch (NoSuchElementException e) {

                // Don't do anything
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0L, 30L, SECONDS);
    }

    public void clearQueue () {

        this.myQueue.clear();

        System.out.println("Queue cleared");
    }
}

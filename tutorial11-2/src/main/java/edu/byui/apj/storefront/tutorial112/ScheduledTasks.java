package edu.byui.apj.storefront.tutorial112;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0 */3 * * * *")
    public void cronScheduledTask() {
        List<String> names = Arrays.asList(
            "Alice", "Bob", "Charlie", "David", "Emma",
            "Frank", "Grace", "Henry", "Ivy", "Jack",
            "Karen", "Liam", "Mia", "Noah", "Olivia",
            "Paul", "Quinn", "Ryan", "Sophia", "Thomas"
        );

        // Split the list into two batches
        int midPoint = names.size() / 2;
        List<String> firstBatch = names.subList(0, midPoint);
        List<String> secondBatch = names.subList(midPoint, names.size());

        // Create an ExecutorService with 2 threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit tasks for both batches
        executor.submit(() -> {
            for (String name : firstBatch) {
                log.info("Thread 1: {} - Current time: {}", name, dateFormat.format(new Date()));
                try {
                    Thread.sleep(100); // Small delay to make the output more readable
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        executor.submit(() -> {
            for (String name : secondBatch) {
                log.info("Thread 2: {} - Current time: {}", name, dateFormat.format(new Date()));
                try {
                    Thread.sleep(100); // Small delay to make the output more readable
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Shutdown the executor and wait for all tasks to complete
        executor.shutdown();
        try {
            if (executor.awaitTermination(60, TimeUnit.SECONDS)) {
                log.info("All done here!");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

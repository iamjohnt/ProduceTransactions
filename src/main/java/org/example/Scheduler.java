package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// schedules recurring job to generate transaction, and send to kafka topic
@Component
public class Scheduler {

    @Autowired
    TransactionGenerator generator;

    @Autowired
    TransactionKafkaSender sender;

    @Scheduled(fixedRate = 1000)
    public void schedule() {
        this.generateAndSendTransaction();
    }

    private void generateAndSendTransaction() {
        Transaction t = generator.generateRandomTransaction();
        sender.send(t);
    }

}

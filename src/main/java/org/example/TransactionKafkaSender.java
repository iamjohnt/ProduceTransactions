package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG;
import static org.apache.kafka.common.config.SaslConfigs.SASL_JAAS_CONFIG;
import static org.apache.kafka.common.config.SaslConfigs.SASL_MECHANISM;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.SECURITY_PROTOCOL_CONFIG;

// handles connecting to kafka, and sending transactions
@Component
public class TransactionKafkaSender {

    @Autowired
    private KafkaProducer<String, String> producer;
    private String confluentSecret;
    private String confluentBootstrapServer;
    private String topic = "transactions";

    TransactionKafkaSender(String confluentSecret, String confluentBootstrapServer) {
        this.confluentSecret = confluentSecret;
        this.confluentBootstrapServer = confluentBootstrapServer;
    }

    public void send(Transaction transaction) {
        System.out.println("sending: " + transaction.toString());

        // create record
        String key = transaction.getUid();
        String value = transaction.getTransactionAmount().toString();
        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, key, value);

        // send data
        producer.send(record);
//        producer.flush();
//        producer.close();
    }

}

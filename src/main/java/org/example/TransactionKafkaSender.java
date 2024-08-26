package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
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

    private String confluentSecret;
    private String confluentBootstrapServer;
    private String topic = "transactions";

    TransactionKafkaSender(String confluentSecret, String confluentBootstrapServer) {
        this.confluentSecret = confluentSecret;
        this.confluentBootstrapServer = confluentBootstrapServer;
    }

    public void send(Transaction transaction) {
        // create producer
        KafkaProducer<String, String> producer =
                new KafkaProducer<String, String>(this.createProperties());

        // create record
        String key = transaction.getUid();
        String value = transaction.getTransactionAmount().toString();
        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, key, value);

        // send data and close
        producer.send(record);
        producer.flush();
        producer.close();
    }

    private Properties createProperties() {
        return new Properties() {{
            put(BOOTSTRAP_SERVERS_CONFIG, confluentBootstrapServer);
            put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
            put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
            put(ACKS_CONFIG, "all");
            put(APPLICATION_ID_CONFIG, "JOHNTRAN_APP");
            put(SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            put(SASL_JAAS_CONFIG, confluentSecret);
            put(SASL_MECHANISM, "PLAIN");
            put(APPLICATION_ID_CONFIG, "JOHNTRAN_APP");
            put(CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        }};
    }




}

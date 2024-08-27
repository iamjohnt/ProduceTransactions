package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG;
import static org.apache.kafka.common.config.SaslConfigs.SASL_JAAS_CONFIG;
import static org.apache.kafka.common.config.SaslConfigs.SASL_MECHANISM;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.SECURITY_PROTOCOL_CONFIG;

@Configuration
public class Config {

    @Value("${app.confluent.secrets}")
    private String confluentSecret;

    @Value("${app.confluent.bootstrapserver}")
    private String confluentBootstrapServer;

    @Bean
    public TransactionGenerator transactionGenerator() {
        return new TransactionGenerator();
    }

    @Bean
    public TransactionKafkaSender transactionKafkaSender() {
        return new TransactionKafkaSender(this.confluentSecret, this.confluentBootstrapServer);
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties props = new Properties() {{
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

        return new KafkaProducer<String, String>(props);
    }
}

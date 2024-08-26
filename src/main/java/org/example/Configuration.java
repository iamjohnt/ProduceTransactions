package org.example;

import org.springframework.context.annotation.Bean;

public class Configuration {

    @Bean
    public TransactionGenerator transactionGenerator() {
        return new TransactionGenerator();
    }
}

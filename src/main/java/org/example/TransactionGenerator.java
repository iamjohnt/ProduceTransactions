package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

// simulates and generates uid's and their financial transactions
public class TransactionGenerator {

    final private Double minTrans = -100000.0;
    final private Double maxTrans = 100000.0;
    private String[] uidPool = {
            "12345678",
            "55550000",
            "98765432",
            "10101010"
    };

    private Transaction generateRandomTransaction() {
        String uid = uidPool[randInt(0, uidPool.length - 1)];
        Double value = randDoubleInclusive(minTrans, maxTrans);
        return new Transaction(uid, value);
    }

    private Double randDoubleInclusive(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = min + range * random.nextDouble();
        BigDecimal bd = new BigDecimal(scaled);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private Integer randInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}

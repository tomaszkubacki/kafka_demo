package kafka_demo.app;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Properties;


public class SimpleConsumer {
    KafkaConsumer<String, String> consumer;
    private static final Logger logger = LogManager.getLogger(SimpleConsumer.class);

    public SimpleConsumer(Properties properties) {
        consumer = new KafkaConsumer<>(properties);

    }

    public void wakeup() {
        consumer.wakeup();
    }

    public void consume(List<String> topics) {

        try {
            consumer.subscribe(topics);
            //noinspection InfiniteLoopStatement
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    final var offset = consumerRecord.offset();
                    final var key = consumerRecord.key();
                    final var val = consumerRecord.value();
                    logger.info("offset = {}, key = {} value = {}", offset, key, val);
                }
            }
        } catch (WakeupException wakeupException) {
            logger.info("consumer shutdown");
        } catch (Exception e) {
            logger.error("unexpected exception", e);
        } finally {
            consumer.close();
        }
    }

}

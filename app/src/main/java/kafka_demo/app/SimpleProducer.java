package kafka_demo.app;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.util.Properties;
import java.util.stream.Stream;

public class SimpleProducer implements Closeable {
    private static final Logger logger = LogManager.getLogger(SimpleProducer.class);

    Producer<String, String> producer;

    public SimpleProducer(Properties properties) {
        this.producer = new KafkaProducer<>(properties);
    }

    public void publishMessages(String topic, String[] messages) {
        logger.info("publishing messages to topic {}", topic);
        Stream.of(messages).forEach(m -> producer.send(createRecord(topic, m)));
        producer.flush();
    }

    ProducerRecord<String, String> createRecord(String topic, String message) {
        return new ProducerRecord<>(topic, message);
    }

    @Override
    public void close() {
        producer.close();
    }
}

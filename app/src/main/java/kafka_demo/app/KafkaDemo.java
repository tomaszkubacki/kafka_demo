package kafka_demo.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class KafkaDemo {
    private static final Logger logger = LogManager.getLogger(KafkaDemo.class);

    public static void main(String[] args) throws IOException {

        var arg0 = args.length == 0 ? null : args[0];

        if (arg0 == null) {
            logger.warn("use -p for producer or -c for consumer");
            return;
        }

        var topics = new String[]{"demo-topic-a"};
        var messages = new String[]{"message a", "message b", "message c"};

        Properties props = getProperties();

        switch (arg0) {
            case "-p" -> runProducer(topics[0], messages, props);
            case "-c" -> runConsumer(topics, props);
            default -> logger.warn("unrecognized arg {}", arg0);
        }

    }

    static void runProducer(String topic, String[] messages, Properties props) {
        try (var producer = new SimpleProducer(props)) {
            producer.publishMessages(topic, messages);
        }
    }


    static void runConsumer(String[] topics, Properties props) {
        final Thread main = Thread.currentThread();
        var consumer = new SimpleConsumer(props);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("shutdown started");
            consumer.wakeup();
            try {
                main.join();
            } catch (InterruptedException e) {
                logger.error("thread interrupted", e);
                main.interrupt();
            }
        }));

        consumer.consume(List.of(topics));
    }

    private static Properties getProperties() throws IOException {
        var loader = Thread.currentThread().getContextClassLoader();
        var resourceStream = loader.getResourceAsStream("app.properties");
        var props = new Properties();
        props.load(resourceStream);
        return props;
    }

}

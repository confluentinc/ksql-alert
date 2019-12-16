package io.confluent.ksql.alert;

import io.confluent.ksql.alert.serde.KafkaJsonDeserializer;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.ksql.alert.api.ResultTopicKey;
import io.confluent.ksql.alert.api.ResultTopicValue;

public class AlertConsumer implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(AlertConsumer.class);

  Properties properties = new Properties();
  KafkaConsumer<ResultTopicKey, ResultTopicValue> consumer;
  AlertManager alertManager;

  public AlertConsumer(String propertyPath, AlertManager alertManager) throws IOException {
    this.properties.load(AlertConsumer.class.getClassLoader().getResourceAsStream(propertyPath));
    this.consumer = new KafkaConsumer
        <ResultTopicKey, ResultTopicValue>(properties,
        new KafkaJsonDeserializer<ResultTopicKey>(ResultTopicKey.class),
        new KafkaJsonDeserializer<ResultTopicValue>(ResultTopicValue.class));
    consumer.subscribe(Collections.singletonList("result"));
    this.alertManager = alertManager;
  }

  public void run() {
    log.info("starting {}", this);
    try {
      if (Thread.currentThread().isInterrupted()) {
        log.warn("thread is interrupted");
      }
      while (!Thread.currentThread().isInterrupted()) {
        try {
          ConsumerRecords<ResultTopicKey, ResultTopicValue> records = this.consumer.poll(Duration.ofMillis(500));
          for (ConsumerRecord<ResultTopicKey, ResultTopicValue> record : records) {
            this.alertManager.mayBeSendAlert(record.value());
          }
        } catch (Exception e) {
          log.error("error consuming message", e);
        }
        Thread.sleep(Math.round(Math.random() * TimeUnit.SECONDS.toMillis(3)));
      }
    } catch (InterruptedException ie) {
      log.info("interrupted {}", this, ie);
    } catch (Exception e) {
      log.warn("exceptional {}", this, e);
    } finally {
      log.info("closing consumer {}", this);
      if (consumer != null) {
        consumer.close();
      }
    }
  }

}

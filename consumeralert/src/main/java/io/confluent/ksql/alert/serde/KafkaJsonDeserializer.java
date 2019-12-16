package io.confluent.ksql.alert.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaJsonDeserializer<T> implements Deserializer {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private Class <T> type;

  public KafkaJsonDeserializer(Class type) {
    this.type = type;
  }

  public void configure(Map map, boolean b) {

  }

  public Object deserialize(String s, byte[] bytes) {
    ObjectMapper mapper = new ObjectMapper();
    T obj = null;
    try {
      obj = mapper.readValue(bytes, type);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return obj;
  }

  public void close() {

  }

}

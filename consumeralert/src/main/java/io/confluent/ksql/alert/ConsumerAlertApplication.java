package io.confluent.ksql.alert;

import java.io.IOException;
import java.util.Properties;

public class ConsumerAlertApplication {

  public static void main(String[] args) throws IOException {
    Properties properties = new Properties();
    properties.load(ConsumerAlertApplication.class.getClassLoader().getResourceAsStream("alert.properties"));
    AlertManager alertManager = new AlertManager(properties.getProperty("slackurl"), Long. parseLong(properties.getProperty("intervalMs")));
    AlertConsumer alertConsumer = new AlertConsumer("consumer.properties",alertManager);
    alertConsumer.run();
  }

}

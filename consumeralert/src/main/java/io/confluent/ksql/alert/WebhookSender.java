package io.confluent.ksql.alert;

import io.confluent.command.record.alert.CommandAlert;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import io.confluent.serializers.ProtoSerde;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebhookSender {

  CommandAlert.WebHookAction webHookAction;

  private static final Logger log = LoggerFactory.getLogger(WebhookSender.class);

  public WebhookSender(CommandAlert.WebHookAction webHookAction) {
    this.webHookAction = webHookAction;
  }

  public String sendWebHook() {
    String rawResponse = null;
    try {
      HttpPost httppost = new HttpPost(webHookAction.getUrl());
      String payload = constructPayloadFormat();
      StringEntity requestEntity = new StringEntity(
          payload,
          ContentType.APPLICATION_JSON);
      httppost.setEntity(requestEntity);
      HttpClient httpclient = HttpClients.createDefault();
      ResponseHandler<String> responseHandler=new BasicResponseHandler();
      rawResponse = httpclient.execute(httppost, responseHandler);
    } catch (Exception e) {
      log.warn(
          "sending=failure webhook to={} with subject={} {}",
          webHookAction.getUrl(),
          webHookAction.getSubject(),
          e
      );
    }
    return rawResponse;
  }

  public String constructPayloadFormat() {
    ProtoSerde<CommandAlert.Slack> slackProtoSerde =
        new ProtoSerde<CommandAlert.Slack>(CommandAlert.Slack.getDefaultInstance());
    String content = slackProtoSerde.toJson(webHookAction.getSlack(), true);
    return content;
  }
}
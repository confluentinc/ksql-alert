package io.confluent.ksql.alert;

import io.confluent.command.record.alert.CommandAlert;
import io.confluent.ksql.alert.api.ResultTopicValue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AlertManager {

  String slackurl = "";
  long intervalMs = 0;
  List<Node> list = null;
  Map<String, Node> map = null;

  public AlertManager(String slackurl, long intervalMs) {
    this.slackurl = slackurl;
    this.intervalMs = intervalMs;
    this.list = new LinkedList<Node>();
    this.map = new HashMap<String, Node>();
  }

  class Node {
    long timestamp;
    ResultTopicValue resultTopicValue;

    public Node(long timestamp, ResultTopicValue resultTopicValue) {
      this.timestamp = timestamp;
      this.resultTopicValue = resultTopicValue;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public ResultTopicValue getResultTopicValue() {
      return resultTopicValue;
    }

    public void setResultTopicValue(ResultTopicValue resultTopicValue) {
      this.resultTopicValue = resultTopicValue;
    }
  }

  private void evict() {
    for (Node node : list) {
      if (System.currentTimeMillis() - node.getTimestamp() > intervalMs) {
        list.remove(node);
        map.remove(node.getResultTopicValue().getOpid());
      } else {
        break;
      }
    }
  }

  public void mayBeSendAlert(ResultTopicValue resultTopicValue) {
    evict();
    long current = System.currentTimeMillis();
    Node node = new Node(current, resultTopicValue);
    if ( map.size() == 0 || map.get(resultTopicValue.getOpid()) == null ) {
      sendAlert(node);
      list.add(node);
      map.put(resultTopicValue.getOpid(), node);
    }
  }

  private void sendAlert(Node node){
    CommandAlert.Slack slack = CommandAlert.Slack
        .newBuilder()
        .setText(String.format("Delay latency for operator %s is %s.",
            node.getResultTopicValue().getOpid(),
            node.getResultTopicValue().getDelayAvg()))
        .build();
    CommandAlert.WebHookAction webHookAction = CommandAlert.WebHookAction
        .newBuilder()
        .setSlack(slack)
        .setUrl(this.slackurl)
        .build();
    new WebhookSender(webHookAction).sendWebHook();

  }

}

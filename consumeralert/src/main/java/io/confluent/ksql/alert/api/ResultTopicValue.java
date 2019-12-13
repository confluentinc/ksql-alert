package io.confluent.ksql.alert.api;

public class ResultTopicValue {

  String opid;
  long delayAvg;

  public ResultTopicValue(String opid, long delayAvg) {
    this.opid = opid;
    this.delayAvg = delayAvg;
  }

  public String getOpid() {
    return opid;
  }

  public void setOpid(String opid) {
    this.opid = opid;
  }

  public long getDelayAvg() {
    return delayAvg;
  }

  public void setDelayAvg(long delayAvg) {
    this.delayAvg = delayAvg;
  }
}

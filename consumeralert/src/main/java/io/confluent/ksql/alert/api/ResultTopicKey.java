package io.confluent.ksql.alert.api;

public class ResultTopicKey {

  long windowstart;
  String opid;

  public ResultTopicKey(long windowstart, String opid) {
    this.windowstart = windowstart;
    this.opid = opid;
  }

  public long getWindowstart() {
    return windowstart;
  }

  public void setWindowstart(long windowstart) {
    this.windowstart = windowstart;
  }

  public String getOpid() {
    return opid;
  }

  public void setOpid(String opid) {
    this.opid = opid;
  }
}

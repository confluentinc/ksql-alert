package io.confluent.ksql.alert.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultTopicKey {

  long windowstart;
  String opid;

  @JsonCreator
  public ResultTopicKey(
      @JsonProperty("windowstart") long windowstart,
      @JsonProperty("opid") String opid) {
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

  @Override
  public String toString() {
    return "ResultTopicKey{" +
        "windowstart=" + windowstart +
        ", opid='" + opid + '\'' +
        '}';
  }
}

package io.confluent.ksql.alert.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultTopicValue {

  String opid;
  long delayAvg;

  @JsonCreator
  public ResultTopicValue(
      @JsonProperty("opid") String opid,
      @JsonProperty("delayAvg") long delayAvg) {
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

  @Override
  public String toString() {
    return "ResultTopicValue{" +
        "opid='" + opid + '\'' +
        ", delayAvg=" + delayAvg +
        '}';
  }
}

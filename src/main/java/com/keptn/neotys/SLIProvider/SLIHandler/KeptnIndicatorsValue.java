package com.keptn.neotys.SLIProvider.SLIHandler;

public class KeptnIndicatorsValue {
    String metric;
    double value;
    boolean success;

    public KeptnIndicatorsValue(String metric, double value, boolean success) {
        this.metric = metric;
        this.value = value;
        this.success = success;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

package com.keptn.neotys.SLIProvider.SLIHandler;

public class KeptnIndicatorsValue {
    String metric;
    double value;
    boolean sucess;

    public KeptnIndicatorsValue(String metric, double value, boolean sucess) {
        this.metric = metric;
        this.value = value;
        this.sucess = sucess;
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

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }
}

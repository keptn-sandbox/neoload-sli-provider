package com.keptn.neotys.SLIProvider.KeptnEvents;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keptn.neotys.SLIProvider.SLIHandler.KeptnIndicatorsValue;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class GetSli {
    String sliProvider;
    String start;
    String end;
    List<String> indicators;
    List<KeptnFilter> customFilters;
    List<KeptnIndicatorsValue>  indicatorValues;
    public String getSliProvider() {
        return sliProvider;
    }

    public void setSliProvider(String sliProvider) {
        this.sliProvider = sliProvider;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<KeptnIndicatorsValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(List<KeptnIndicatorsValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    public List<String> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<String> indicators) {
        this.indicators = indicators;
    }

    public List<KeptnFilter> getCustomFilters() {
        return customFilters;
    }

    public void setCustomFilters(List<KeptnFilter> customFilters) {
        this.customFilters = customFilters;
    }

    public GetSli(String sliProvider, String start, String end, List<String> indicators, List<KeptnFilter> customFilters) {
        this.sliProvider = sliProvider;
        this.start = start;
        this.end = end;
        this.indicators = indicators;
        this.customFilters = customFilters;
    }

    public JsonObject toJson()
    {
        Gson gson= new GsonBuilder().create();
        return new JsonObject(gson.toJson(this));
    }
}

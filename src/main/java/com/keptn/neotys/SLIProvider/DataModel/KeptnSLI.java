package com.keptn.neotys.SLIProvider.DataModel;

import java.util.HashMap;

public class KeptnSLI {
    String spec_version;
    HashMap<String,NeoLoadSLI> indicators;

    public KeptnSLI()
    {
        this.spec_version=null;
        this.indicators=new HashMap<>();
    }

    public KeptnSLI(String spec_version, HashMap<String, NeoLoadSLI> indicators) {
        this.spec_version = spec_version;
        this.indicators = indicators;
    }

    public String getSpec_version() {
        return spec_version;
    }

    public void setSpec_version(String spec_version) {
        this.spec_version = spec_version;
    }

    public HashMap<String, NeoLoadSLI> getIndicators() {
        return indicators;
    }

    public void setIndicators(HashMap<String, NeoLoadSLI> indicators) {
        this.indicators = indicators;
    }


}

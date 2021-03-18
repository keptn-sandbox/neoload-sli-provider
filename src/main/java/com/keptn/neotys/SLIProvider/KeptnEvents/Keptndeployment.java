package com.keptn.neotys.SLIProvider.KeptnEvents;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Keptndeployment {
    List<String> deploymentURIsLocal=new ArrayList<>();
    List<String> deploymentURIsPublic=new ArrayList<>();

    public Keptndeployment(List<String> deploymentURIsLocal, List<String> deploymentURIsPublic) {
        this.deploymentURIsLocal = deploymentURIsLocal;
        this.deploymentURIsPublic = deploymentURIsPublic;
    }

    public List<String> getDeploymentURIsLocal() {
        return deploymentURIsLocal;
    }

    public void setDeploymentURIsLocal(List<String> deploymentURIsLocal) {
        this.deploymentURIsLocal = deploymentURIsLocal;
    }

    public List<String> getDeploymentURIsPublic() {
        return deploymentURIsPublic;
    }

    public void setDeploymentURIsPublic(List<String> deploymentURIsPublic) {
        this.deploymentURIsPublic = deploymentURIsPublic;
    }

    public JsonObject tojson()
    {
        Gson gson=new GsonBuilder().create();
        return new JsonObject(gson.toJson(this));
    }
}

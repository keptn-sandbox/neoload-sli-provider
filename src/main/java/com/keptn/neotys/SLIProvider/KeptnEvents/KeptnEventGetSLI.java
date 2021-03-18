package com.keptn.neotys.SLIProvider.KeptnEvents;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keptn.neotys.SLIProvider.DataModel.Filter;
import com.keptn.neotys.SLIProvider.SLIHandler.KeptnIndicatorsValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class KeptnEventGetSLI {

    /*

  "type": "sh.keptn.events.tests-finished",
  "specversion": "0.2",
  "source": "https://github.com/keptn/keptn/jmeter-service",
  "id": "49ac0dec-a83b-4bc1-9dc0-1f050c7e781b",
  "time": "2019-06-07T07:02:15.64489Z",
  "contenttype": "application/json",
  "shkeptncontext":"49ac0dec-a83b-4bc1-9dc0-1f050c7e789b",
  "data": {
    "project": "sockshop",
    "stage": "staging",
    "service": "carts",
    "testStrategy": "performance",
    "deploymentStrategy": "direct",
    "startedat": "2019-09-01 12:03"
  }
}
     */

    //  "githuborg":"keptn-tiger",
    //      "project":"sockshop",
    //      "teststrategy":"functional",
    //      "deploymentstrategy":"direct",
    //      "stage":"dev",
    //      "service":"carts",
    //      "image":"10.11.245.27:5000/sockshopcr/carts",
    //      "tag":"0.6.7-16"

    private GetSli getsli;
    private static final String KEY_getsli="get-sli";

    private String project;
    private static final String KEY_project="project";
    private JsonObject test;
    private static final String KEY_test ="test";

    private String teststrategy;
    private static final String KEY_teststrategy="teststrategy";


    private JsonObject deployment;
    private static final String KEY_deployment="deployment";

    private List<String> indicators;
    private static final String KEY_indicators="indicators";

    private String message;
    private static final String KEY_message="message";

    private String status;
    private static final String KEY_status="status";

    private String result;
    private static final String KEY_result="result";

    private String KEY_customFilters="customFilters";

    private String deploymentstrategy;

    private static final String KEY_deploymentstrategy="deploymentstrategy";
    private String stage;
    private static final String KEY_stage="stage";

    private String service;
    private static final String KEY_service="service";


    private List<String> knowkeys= Arrays.asList(new String[]{KEY_deployment, KEY_getsli,KEY_project, KEY_service, KEY_stage,  KEY_label,KEY_message,KEY_status,KEY_result});
    private HashMap<String,Object> otherdata;

    private String testid;
    private static final String KEY_testid="neoload_testid";

    private JsonObject label;
    private static final String KEY_label="labels";

    private String neoloadURL;
    private static final String KEY_nlurl="neoload_url";

    private String teststatus;
    private static final String KEY_nlstatus="neoload_testStatus";



    private List<KeptnIndicatorsValue> indicatorValues;
    private static final String KEY_indicatorValues="indicatorValues";
    private Keptndeployment keptndeployment;

    public KeptnEventGetSLI(JsonObject object)
    {


        if(object.getValue(KEY_project) instanceof  String)
            project=object.getString(KEY_project);

        if(object.getValue(KEY_deploymentstrategy) instanceof String)
            deploymentstrategy=object.getString(KEY_deploymentstrategy);




        if(object.containsKey(KEY_deployment))
        {
            if(object.getValue(KEY_deployment) instanceof JsonObject) {
                deployment = object.getJsonObject(KEY_deployment);
                Gson gson=new GsonBuilder().create();
                keptndeployment=gson.fromJson(deployment.toString(),Keptndeployment.class);

            }
        }


        if(object.getValue(KEY_indicators) instanceof JsonArray)
        {
            JsonArray array=object.getJsonArray(KEY_indicators);
            indicators=new ArrayList<>();
            if (array != null) {
                for (int i=0;i<array.size(); i++){
                    indicators.add(array.getString(i));
                }
            }
        }
        if(object.getValue(KEY_message) instanceof String)
            message=object.getString(KEY_message);

        if(object.getValue(KEY_result) instanceof String)
            result=object.getString(KEY_status);

        if(object.getValue(KEY_status) instanceof String)
            status=object.getString(KEY_status);

        if(object.getValue(KEY_label) instanceof  JsonObject)
        {
            JsonObject labelobject=object.getJsonObject(KEY_label);
            if(labelobject!=null)
            {
                testid=labelobject.getString(KEY_testid);
                neoloadURL=labelobject.getString(KEY_nlurl);
                teststatus=labelobject.getString(KEY_nlstatus);

                labelobject.remove(KEY_nlurl);
                labelobject.remove(KEY_testid);
                labelobject.remove(KEY_nlstatus);
                label=new JsonObject();
                label=labelobject;
            }
        }

        if(object.getValue(KEY_service) instanceof String)
            service=object.getString(KEY_service);


        if(object.getValue(KEY_stage) instanceof String)
            stage=object.getString(KEY_stage);


        if(object.containsKey(KEY_test))
        {
            if (object.getValue(KEY_test) instanceof JsonObject) {
                test = object.getJsonObject(KEY_test);
                if ((test.containsKey(KEY_teststrategy) && test.getValue(KEY_teststrategy) instanceof String))
                    teststrategy = test.getString(KEY_teststrategy);

            }
        }
        if(object.containsKey(KEY_getsli)) {
            if (object.getValue(KEY_getsli) instanceof JsonObject) {
                JsonObject sli = object.getJsonObject(KEY_test);
                Gson gson=new GsonBuilder().create();
                getsli=gson.fromJson(sli.toString(),GetSli.class);
            }
        }
        getOtherData(object);

    }

    public GetSli getGetsli() {
        return getsli;
    }

    public void setGetsli(GetSli getsli) {
        this.getsli = getsli;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Keptndeployment getKeptndeployment() {
        return keptndeployment;
    }

    public void setKeptndeployment(Keptndeployment keptndeployment) {
        this.keptndeployment = keptndeployment;
    }

    public GetSli getSliProvider() {
        return getsli;
    }

    public void setSliProvider(GetSli sliProvider) {
        this.getsli = sliProvider;
    }

    public KeptnEventGetSLI(String project, String teststrategy, String deploymentstrategy, String stage, String service) {
        this.project = project;
        this.teststrategy = teststrategy;
        this.deploymentstrategy = deploymentstrategy;
        this.stage = stage;
        this.service = service;

    }

    public List<KeptnIndicatorsValue> getIndicatorValues() {
        return this.getsli.getIndicatorValues();
    }

    public void setIndicatorValues(List<KeptnIndicatorsValue> indicatorValues) {
        this.getsli.setIndicatorValues(indicatorValues);
    }

    private void getOtherData(JsonObject object)
    {
        otherdata=new HashMap<String,Object>();

        object.forEach(
                pair->{
                    if(!knowkeys.contains(pair.getKey()))
                        otherdata.put(pair.getKey(),pair.getValue());
                }
        );
    }

    public String getTeststatus() {
        return teststatus;
    }

    public void setTeststatus(String teststatus) {
        this.teststatus = teststatus;
    }

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public String getNeoloadURL() {
        return neoloadURL;
    }

    public void setNeoloadURL(String neoloadURL) {
        this.neoloadURL = neoloadURL;
    }



    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTeststrategy() {
        return teststrategy;
    }

    public void setTeststrategy(String teststrategy) {
        this.teststrategy = teststrategy;
    }

    public String getDeploymentstrategy() {
        return deploymentstrategy;
    }

    public void setDeploymentstrategy(String deploymentstrategy) {
        this.deploymentstrategy = deploymentstrategy;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }



    private  String convertDateLongToString(long longdate)
    {
        Date date=new Date(longdate);

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return df2.format(date);
    }


    public JsonObject toJsonObject()
    {
        JsonObject jsonObject=new JsonObject();
         jsonObject.put(KEY_stage,stage);
        jsonObject.put(KEY_service,service);

        jsonObject.put(KEY_status,status);
        jsonObject.put(KEY_message,message);
        jsonObject.put(KEY_result,result);
        jsonObject.put(KEY_project,project);
        jsonObject.put(KEY_getsli,getsli.toJson());

        jsonObject.put(KEY_indicators,indicators);
        HashMap<String,String> neoloaddata=new HashMap<>();


        if(keptndeployment!=null)
        {
            jsonObject.put(KEY_deployment,keptndeployment.tojson());
        }

        if(test!=null)
            jsonObject.put(KEY_test,test);

        label.put(KEY_testid,testid);
        label.put(KEY_nlurl,neoloadURL);

        label.put(KEY_nlstatus,teststatus);

        jsonObject.put(KEY_label, label);



        return jsonObject;
    }

    public JsonObject toJsonDoneObject()
    {
        JsonObject jsonObject=new JsonObject();
        jsonObject.put(KEY_stage,stage);
        jsonObject.put(KEY_service,service);
        jsonObject.put(KEY_project,project);


        label.put(KEY_testid,testid);
        label.put(KEY_nlurl,neoloadURL);

        label.put(KEY_nlstatus,teststatus);

        jsonObject.put(KEY_getsli,getsli.toJson());

        jsonObject.put(KEY_label, label);


        return jsonObject;
    }

    public JsonObject toJsonStartObjec()
    {
        JsonObject jsonObject=new JsonObject();
        jsonObject.put(KEY_stage,stage);
        jsonObject.put(KEY_service,service);
        jsonObject.put(KEY_project,project);
        jsonObject.put(KEY_message,message);
        jsonObject.put(KEY_result,result);
        jsonObject.put(KEY_status,status);

        label.put(KEY_testid,testid);
        label.put(KEY_nlurl,neoloadURL);

        label.put(KEY_nlstatus,teststatus);

        jsonObject.put(KEY_getsli,getsli.toJson());

        jsonObject.put(KEY_label, label);


        return jsonObject;
    }

    public String toString()
    {

        return toJsonObject().toString();

    }
}

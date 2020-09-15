package com.keptn.neotys.SLIProvider.EventSender;


import com.keptn.neotys.SLIProvider.KeptnEvents.CloudTestGetSliEvent;
import com.keptn.neotys.SLIProvider.KeptnEvents.KeptnEventGetSLI;
import com.keptn.neotys.SLIProvider.cloudevent.KeptnExtensions;
import com.keptn.neotys.SLIProvider.log.KeptnLogger;

import io.cloudevents.CloudEvent;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClientRequest;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.net.URI;

import static com.keptn.neotys.SLIProvider.KeptnEvents.EventType.KEPTN_EVENTS_GETSLI_DONE;
import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.*;


public class NeoLoadEndEvent {

    String eventid;
    KeptnLogger logger;
    Vertx vertx;
    private String keptnNameSpace;
    private final static String CONTENTYPE="application/json";
    private final static String CONTENTYPE_CLOUD=" application/cloudevents+json";
    public NeoLoadEndEvent(KeptnLogger log, String enventid, Vertx rxvertx) {
        this.eventid=enventid;
        logger=log;
        vertx=rxvertx;
        keptnNameSpace=System.getenv(SECRET_KEPTN_NAMESPACE);
    }

//{
//  "type": "sh.keptn.internal.event.get-sli.done",
//  "specversion": "0.2",
//  "source": "https://github.com/keptn/keptn/prometheus-service",
//  "id": "f2b878d3-03c0-4e8f-bc3f-454bc1b3d79d",
//  "time": "2019-06-07T07:02:15.64489Z",
//  "contenttype": "application/json",
//  "shkeptncontext": "08735340-6f9e-4b32-97ff-3b6c292bc509",
//  "data": {
//    "project": "sockshop",
//    "stage": "staging",
//    "service": "carts",
//    "start": "2019-11-05T16:30:27.152Z",
//    "end": "2019-11-05T16:35:27.152Z",
//    "teststrategy": "manual",
//    "deploymentstrategy": "direct",
//    "deployment": "direct",
//    "indicatorValues": [
//      {
//        "metric":"request_latency_p95",
//        "value": 1.1620000000000001,
//        "success": true
//      },
//      {
//        "metric":"error_rate",
//        "value": 0,
//        "success": true
//      }
//    ],
//    "labels": {
//      "testid": "12345",
//      "buildnr": "build17",
//      "runby": "JohnDoe"
//    }
//  }
//}

    public void endevent(KeptnEventGetSLI data, KeptnExtensions extensions, CloudEvent<Object> receivedEvent)
    {
        try {
            logger.debug("endevent : Start sending event");
            final HttpClientRequest request = vertx.createHttpClient().post(KEPTN_PORT_EVENT, KEPTN_EVENT_HOST+keptnNameSpace+KEPTN_END_URL, "/"+KEPTN_EVENT_URL);

            logger.debug("endevent : Defining cloud envet with data:" + data.toJsonObject().toString());

            logger.debug("endevnet specversion : "+receivedEvent.getSpecVersion()+" : source : "+URI.create(NEOLOAD_SOURCE).toString()+ " id :"+this.eventid);
            String id;
            if(receivedEvent.getId()==null)
                id=extensions.getShkeptncontext();
            else
                id=receivedEvent.getId();

            WebClient client=WebClient.create(vertx);

            HttpRequest<Buffer> httpRequest=client.post(KEPTN_PORT_EVENT, KEPTN_EVENT_HOST+keptnNameSpace+KEPTN_END_URL, "/"+KEPTN_EVENT_URL);

            httpRequest.putHeader(CONTENT_TYPE,CONTENTYPE_CLOUD);
            CloudTestGetSliEvent cloudSLIEventNeoload=new CloudTestGetSliEvent(KEPTN_EVENTS_GETSLI_DONE,CONTENTYPE,extensions.getShkeptncontext(),receivedEvent.getSpecVersion(),NEOLOAD_SOURCE,id,data.toJsonDoneObject());
            httpRequest.sendJson(cloudSLIEventNeoload.toJson(),httpResponseAsyncResult -> {
                if(httpResponseAsyncResult.succeeded())
                {
                    logger.info("endevent : received response code "+String.valueOf(httpResponseAsyncResult.result().statusCode())+ " message "+ httpResponseAsyncResult.result().statusMessage());
                }
                else
                {
                    logger.error("ERROR endevent : received response code "+String.valueOf(httpResponseAsyncResult.result().statusCode())+ " message "+ httpResponseAsyncResult.result().statusMessage());
                }
            });
            logger.info("Request sent " + cloudSLIEventNeoload.toJson().toString() );



        }
        catch(Exception e)
        {
            logger.error("end event generate exception",e);
            if(extensions.getShkeptncontext()==null)
                logger.debug("keptn context null");

            if(receivedEvent.getSpecVersion()==null)
                logger.debug("Specversion null");

            if(data.toJsonObject() ==null)
                logger.debug("data null");

        }
    }



}
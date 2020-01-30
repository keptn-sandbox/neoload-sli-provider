package com.keptn.neotys.SLIProvider.cloudevent;

import com.keptn.neotys.SLIProvider.EventSender.NeoLoadEndEvent;
import com.keptn.neotys.SLIProvider.KeptnEvents.KeptnEventGetSLI;
import com.keptn.neotys.SLIProvider.SLIHandler.KeptnIndicatorsValue;
import com.keptn.neotys.SLIProvider.SLIHandler.NeoLoadSLIHandler;
import com.keptn.neotys.SLIProvider.log.KeptnLogger;
import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.Vertx;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.keptn.neotys.SLIProvider.KeptnEvents.EventType.KEPTN_EVENTS_GETSLI;
import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.*;

public class CloudSLIEventNeoload extends AbstractVerticle {

    KeptnLogger loger;

    private Vertx rxvertx;
    public void start() {
        rxvertx= Vertx.newInstance(this.getVertx());
        loger=new KeptnLogger(this.getClass().getName());
        if(rxvertx ==null)
            System.out.println("Issues during init");

        rxvertx.createHttpServer()
                .requestHandler(req ->
                {
                    if(req.path().equalsIgnoreCase(HEALTH_PATH)) {
                        req.response().end("Status:OK");
                        return;
                    }
                    System.out.print(req.toString());
                    VertxCloudEvents.create().rxReadFromRequest(req,new Class[]{KeptnExtensions.class})
                            .subscribe((receivedEvent, throwable) -> {
                                if(throwable!=null)
                                {
                                    throwable.printStackTrace();
                                    req.response().setStatusCode(400).end(throwable.getMessage());
                                    return;
                                }
                                if (receivedEvent != null) {
                                    // I got a CloudEvent object:
                                    System.out.println("The event type: " + receivedEvent.getType());
                                    if(receivedEvent.getType().equalsIgnoreCase(KEPTN_EVENTS_GETSLI))
                                    {

                                        if(receivedEvent.getData().isPresent())
                                        {
                                            Object obj=receivedEvent.getData().get();
                                            try {
                                                JsonObject data = new JsonObject((HashMap<String,Object>)obj);
                                                if (data instanceof JsonObject)
                                                {
                                                    KeptnEventGetSLI keptnEventGetSLI = new KeptnEventGetSLI(data);
                                                    KeptnExtensions keptnExtensions = null;
                                                    if (receivedEvent.getExtensions().isPresent() && receivedEvent.getExtensions().get().size() > 0) {

                                                        keptnExtensions = (KeptnExtensions) receivedEvent.getExtensions().get().get(0);
                                                    }
                                                    else
                                                    {
                                                        Optional<String> kepncontext = Optional.ofNullable(req.getHeader(HEADER_KEPTNCONTEXT));
                                                        Optional<String> datacontent = Optional.ofNullable(req.getHeader(HEADER_datacontentype));
                                                        if(kepncontext.isPresent()&& datacontent.isPresent())
                                                            keptnExtensions=new KeptnExtensions(kepncontext.get(),datacontent.get());
                                                    }

                                                    if(keptnExtensions!=null)
                                                    {
                                                        if(keptnEventGetSLI.getSliProvider().equalsIgnoreCase(NEOLOAD_PROVIDER)) {
                                                            String keptncontext = keptnExtensions.getShkeptncontext();
                                                            loger.setKepncontext(keptncontext);
                                                            loger.debug("Received data " + keptnEventGetSLI.toString());
                                                            KeptnExtensions finalKeptnExtensions = keptnExtensions;
                                                            req.response().setStatusCode(200).putHeader("content-type", "text/plain").end("event received");

                                                            KeptnExtensions finalKeptnExtensions1 = keptnExtensions;
                                                            vertx.<String>executeBlocking(
                                                                    future -> {

                                                                        try {
                                                                            NeoLoadSLIHandler neoLoadSLIHandler = new NeoLoadSLIHandler(finalKeptnExtensions, keptnEventGetSLI, receivedEvent.getId());


                                                                            Future<List<KeptnIndicatorsValue>> listFuture = neoLoadSLIHandler.getSliFromNeoLaod(rxvertx, receivedEvent);
                                                                            listFuture.setHandler(listAsyncResult ->
                                                                            {
                                                                                String result;
                                                                                if (listAsyncResult.succeeded()) {

                                                                                    List<KeptnIndicatorsValue> indicatorsValues = listAsyncResult.result();
                                                                                    keptnEventGetSLI.setIndicatorValues(indicatorsValues);
                                                                                    ///---
                                                                                    NeoLoadEndEvent endEvent = new NeoLoadEndEvent(loger, receivedEvent.getId(), rxvertx);
                                                                                    endEvent.endevent(keptnEventGetSLI, finalKeptnExtensions1, receivedEvent);
                                                                                    //--send end event-------------

                                                                                    result = "SLI has been retrieved";
                                                                                    future.complete(result);
                                                                                } else {
                                                                                    result = "Exception :" + listAsyncResult.cause();
                                                                                    future.fail(result);
                                                                                }
                                                                            });

                                                                        } catch (Exception e) {
                                                                            future.fail("Exception :" + e.getMessage());
                                                                        }
                                                                    }, res ->
                                                                    {
                                                                        if (res.succeeded()) {

                                                                            req.response().setStatusCode(200).putHeader("content-type", "text/plain").end(res.result());

                                                                        } else {
                                                                            req.response().setStatusCode(500).putHeader("content-type", "text/plain").end(res.cause().getMessage());
                                                                        }
                                                                    }

                                                            );

                                                        }
                                                        else
                                                        {
                                                            req.response().setStatusCode(200).end("Event is not concerning the neoload-sli-provider");

                                                        }
                                                    }
                                                    else
                                                    {
                                                        req.response().setStatusCode(401).end("Unable to find Extensions in CLoud evnet");

                                                    }
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                req.response().setStatusCode(410).end("Exception :"+e.getMessage());
                                            }
                                        }


                                    }
                                    else{
                                        req.response().setStatusCode(203).end("Not Supported event type");
                                    }

                                }
                                else
                                {
                                    req.response().setStatusCode(400).end("UNsupported cloud event format");
                                }

                            });
                })
                .rxListen(KEPTN_PORT)
                .subscribe(server -> {
                    System.out.println("Server running!");});

    }
}

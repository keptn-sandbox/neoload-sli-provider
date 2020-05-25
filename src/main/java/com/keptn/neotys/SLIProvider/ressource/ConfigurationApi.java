package com.keptn.neotys.SLIProvider.ressource;

import com.google.gson.Gson;
import com.keptn.neotys.SLIProvider.exception.NeoLoadSLIException;
import com.keptn.neotys.SLIProvider.log.KeptnLogger;

import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.client.predicate.ResponsePredicate;

import java.util.concurrent.atomic.AtomicReference;

import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.*;


public class ConfigurationApi {
    private KeptnLogger logger;
    private Vertx vertx;

    private String projectname;
    private String stagename;
    private WebClient client;
    private String servicename;
    public ConfigurationApi(KeptnLogger logger, Vertx vertx, String projectname, String stagename,String service) {
        this.logger = logger;
        this.vertx = vertx;
        this.projectname = projectname;
        this.stagename = stagename;
        this.servicename=service;
        client=WebClient.create(vertx);
    }

    private KeptnRessource toKeptnRessource(String content)
    {
        Gson gson = new Gson();
        return gson.fromJson(content, KeptnRessource.class);
    }
    public Future<KeptnRessource> getRessource(String ressource) throws NeoLoadSLIException
    {
        Future<KeptnRessource> keptnRessourceFuture=Future.future();
       //----let's search at the service level---
        logger.debug("try tro retrieve from project, stage and service");
        String uri="/"+CONFIGURATION_VERSION+"/"+CONFIGURATION_PROJECT+"/"+projectname+"/"+CONFIGURATION_STAGE+"/"+stagename+"/"+CONFIGURATION_SERVICE+"/"+servicename+"/"+CONFIGURATION_RESSOURCE+"/"+ressource;
        Future<String> keptnRessourcetest=getRessourceByURL(uri);
        keptnRessourcetest.setHandler(keptnRessourceAsyncResult -> {
           if(keptnRessourceAsyncResult.succeeded())
           {
               logger.debug("Found the sli file");
               keptnRessourceFuture.complete(toKeptnRessource(keptnRessourceAsyncResult.result()));
           }
           else
           {
               logger.debug("sli file not found , trying on project/stage");
               String url="/"+CONFIGURATION_VERSION+"/"+CONFIGURATION_PROJECT+"/"+projectname+"/"+CONFIGURATION_STAGE+"/"+stagename+"/"+CONFIGURATION_RESSOURCE+"/"+ressource;
               Future<String> keptnRessourceFutureWithNOStage ;
               try {
                   keptnRessourceFutureWithNOStage=getRessourceByURL(url);
                   keptnRessourceFutureWithNOStage.setHandler(keptnRessourceAsyncResult1 -> {
                       if(keptnRessourceAsyncResult1.succeeded())
                       {
                           logger.debug("Found the sli file");
                           keptnRessourceFuture.complete(toKeptnRessource(keptnRessourceAsyncResult1.result()));
                       }
                       else
                       {
                           logger.debug("sli file not found , trying on project");
                           String urlproject="/"+CONFIGURATION_VERSION+"/"+CONFIGURATION_PROJECT+"/"+projectname+"/"+CONFIGURATION_RESSOURCE+"/"+ressource;
                           Future<String> keptnRessourceFutureWithNOService;
                           try
                           {
                           keptnRessourceFutureWithNOService=getRessourceByURL(urlproject);
                           keptnRessourceFutureWithNOService.setHandler(keptnRessourceAsyncResult2 -> {
                                   if(keptnRessourceAsyncResult2.succeeded())
                                   {
                                       keptnRessourceFuture.complete(toKeptnRessource(keptnRessourceAsyncResult2.result()));
                                   }
                                   else
                                   {
                                       logger.debug("sli file not found t");
                                       keptnRessourceFuture.fail(keptnRessourceAsyncResult2.cause());
                                   }
                               });
                           } catch (NeoLoadSLIException e) {
                               logger.error("ERROR to get hte sli",e);
                           }
                       }
                   });
               } catch (NeoLoadSLIException e) {
                  logger.error("ERROR to get hte sli",e);
               }

           }

        });

        return keptnRessourceFuture;
    }
    public Future<String> getRessourceByURL(String uri) throws NeoLoadSLIException
    {
        Future<String> keptnRessourceFuture=Future.future();
        HttpRequest<Buffer> request = client.get(CONFIGURAITON_PORT,CONFIGURATIONAPI_HOST,uri);
        request.putHeader(HEADER_ACCEPT,HEADER_APPLICATIONJSON);
        request.expect(ResponsePredicate.SC_SUCCESS);
        request.expect(ResponsePredicate.JSON);
        request.expect(ResponsePredicate.status(200));
        logger.debug("Sending GET Request : "+uri);
        AtomicReference<String> jsonBody=new AtomicReference<>();
        AtomicReference<String> error=new AtomicReference<>();
        request.send(httpResponseAsyncResult -> {
            if (httpResponseAsyncResult.succeeded()) {
                // Obtain response
                HttpResponse<Buffer> response = httpResponseAsyncResult.result();
                logger.debug("Received response : "+ response.toString());
                logger.debug("REceived following body:"+response.bodyAsString());
                keptnRessourceFuture.complete(response.bodyAsString());
            }
            if(httpResponseAsyncResult.failed())
            {
                logger.error("Request failed");
                error.set("The API ressource failed");
                keptnRessourceFuture.fail("The API ressource failed");
            }
        });

        return keptnRessourceFuture;
    }
}

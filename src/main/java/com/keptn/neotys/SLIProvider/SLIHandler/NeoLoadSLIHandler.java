package com.keptn.neotys.SLIProvider.SLIHandler;

import com.keptn.neotys.SLIProvider.DataModel.KeptnSLI;
import com.keptn.neotys.SLIProvider.DataModel.NeoLoadSLI;
import com.keptn.neotys.SLIProvider.KeptnEvents.KeptnEventGetSLI;
import com.keptn.neotys.SLIProvider.cloudevent.KeptnExtensions;
import com.keptn.neotys.SLIProvider.exception.NeoLoadSLIException;
import com.keptn.neotys.SLIProvider.log.KeptnLogger;
import com.keptn.neotys.SLIProvider.ressource.ConfigurationApi;
import com.keptn.neotys.SLIProvider.ressource.KeptnRessource;
import com.neotys.ascode.swagger.client.ApiException;
import io.cloudevents.CloudEvent;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.Vertx;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;

import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.NEOLOAD_SLI_RESSOURCE;

public class NeoLoadSLIHandler {
    KeptnLogger logger;
    private String keptncontext;
    private KeptnExtensions extensions;
    private KeptnEventGetSLI keptnEventGetSLI;
    private String eventid;
    private String stage;

    public NeoLoadSLIHandler( KeptnExtensions extensions, KeptnEventGetSLI keptnEventGetSLI, String eventid) {
        logger = new KeptnLogger(this.getClass().getName());
        logger.setKepncontext(extensions.getShkeptncontext());
        this.keptncontext =    extensions.getShkeptncontext();
        this.extensions = extensions;
        this.keptnEventGetSLI = keptnEventGetSLI;
        this.eventid = eventid;
        this.stage=keptnEventGetSLI.getStage();
    }


    public Future<List<KeptnIndicatorsValue>> getSliFromNeoLaod(Vertx rxvertx, CloudEvent<Object> receivedEvent) throws NeoLoadSLIException {
        Future<List<KeptnIndicatorsValue>> listFuture=Future.future();
        List<KeptnIndicatorsValue> keptnIndicatorsValues=new ArrayList<>();
        Future<KeptnSLI> keptnSLIFuture=getRessources(rxvertx);
        keptnSLIFuture.setHandler( keptnSLIAsyncResult -> {
            if(keptnSLIAsyncResult.succeeded())
            {
                KeptnSLI sli=keptnSLIAsyncResult.result();
                StringBuilder error=new StringBuilder();
                try {
                    SLiRetriever retriever=new SLiRetriever(extensions,keptnEventGetSLI);
                    sli.getIndicators().forEach( (s, neoLoadSLI) -> {
                        try
                        {
                            if(neoLoadSLI.validateMetric())
                            {
                                KeptnIndicatorsValue keptnIndicatorsValue=retriever.getIndicatorValue(s,neoLoadSLI);
                                if(keptnIndicatorsValue!=null)
                                    keptnIndicatorsValues.add(keptnIndicatorsValue);
                            }
                        }
                        catch (NeoLoadSLIException e)
                        {
                            sli.getIndicators().remove(s);
                            error.append("Error in GetSLI defintion  : "+e.getMessage());
                        } catch (ApiException e) {
                            error.append("Error in GetSLI defintion  : "+e.getMessage());
                        }
                    });


                } catch (NeoLoadSLIException e) {
                    logger.error("Error in GetSliFromNeoload ",e);
                }

                if(error.length()>0)
                {
                    logger.error("Error in GetSliFromNeoLoad "+error.toString());
                    listFuture.fail("Error in GetSliFromNeoLoad "+error.toString());
                }
                else
                {
                    if(keptnIndicatorsValues.size()>0)
                        listFuture.complete(keptnIndicatorsValues);
                    else
                        listFuture.fail("No Indicator found for the described SLA");
                }

            }
            if(keptnSLIAsyncResult.failed()) {
                logger.error("Error to receive the test steps",keptnSLIAsyncResult.cause());
                listFuture.fail("Error to receive the test steps -> "+ keptnSLIAsyncResult.cause());

            }

        });

        return listFuture;

    }

    private Future<KeptnSLI> getRessources(Vertx rxvertx) throws NeoLoadSLIException {
        ConfigurationApi configurationApi=new ConfigurationApi(logger,rxvertx,keptnEventGetSLI.getProject(),keptnEventGetSLI.getStage(),keptnEventGetSLI.getService());
        Future<KeptnRessource> keptnRessource=configurationApi.getRessource(NEOLOAD_SLI_RESSOURCE);
        Future<KeptnSLI> listFuture=Future.future();

        keptnRessource.setHandler(result->{
            if(result.succeeded())
            {
                KeptnRessource resourceObject =result.result();
                logger.debug("Ressource file found " + resourceObject.getResourceURI());
                String yaml = resourceObject.getDecodedRessourceContent();
                logger.debug("YAML received : "+yaml);
                KeptnSLI keptnSLI = new Yaml().loadAs(yaml, KeptnSLI.class);
                if (keptnSLI == null) {
                    logger.debug("getRessources - no able to deserialize the yaml file");
                    listFuture.fail(new NeoLoadSLIException("Unable to deserialize YAML file "));
                }
                if (keptnSLI.getIndicators().size() < 0) {
                    logger.debug("getRessources - there is no sli ");
                    listFuture.fail(new NeoLoadSLIException("There is no testing steps define "));

                }


                listFuture.complete(keptnSLI);
            } else {
                logger.error("No Ressrouce " + NEOLOAD_SLI_RESSOURCE + " found for project " + keptnEventGetSLI.getProject() + " and stage " + keptnEventGetSLI.getStage());
                listFuture.fail(new NeoLoadSLIException("No Ressrouce " + NEOLOAD_SLI_RESSOURCE + " found for project " + keptnEventGetSLI.getProject() + " and stage " + keptnEventGetSLI.getStage()));

            }
        });

        return listFuture;


    }
}

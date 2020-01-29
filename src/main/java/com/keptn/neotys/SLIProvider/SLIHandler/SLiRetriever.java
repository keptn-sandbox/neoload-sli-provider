package com.keptn.neotys.SLIProvider.SLIHandler;

import com.keptn.neotys.SLIProvider.DataModel.NeoLoadSLI;
import com.keptn.neotys.SLIProvider.KeptnEvents.KeptnEventGetSLI;
import com.keptn.neotys.SLIProvider.cloudevent.KeptnExtensions;
import com.keptn.neotys.SLIProvider.exception.NeoLoadSLIException;
import com.keptn.neotys.SLIProvider.log.KeptnLogger;
import com.neotys.ascode.swagger.client.ApiClient;
import com.neotys.ascode.swagger.client.ApiException;
import com.neotys.ascode.swagger.client.api.ResultsApi;
import com.neotys.ascode.swagger.client.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.*;

public class SLiRetriever {
    private String testid;
    KeptnLogger logger;
    private String neoloadAPitoken;
    private Optional<String> neoloadweb_apiurl;
    private Optional<String> neoloadweb_url;
    private ApiClient apiClient;
    private ResultsApi resultsApi;

    public SLiRetriever(KeptnExtensions extensions, KeptnEventGetSLI keptnEventGetSLI) throws  NeoLoadSLIException {
        logger = new KeptnLogger(this.getClass().getName());
        logger.setKepncontext(extensions.getShkeptncontext());
        this.testid=keptnEventGetSLI.getTestid();
        getSecrets();
        if(neoloadweb_apiurl.isPresent())
        {
            apiClient=new ApiClient();
            apiClient.setApiKey(neoloadAPitoken);
            apiClient.setBasePath(NLWEB_PROTOCOL+neoloadweb_apiurl.get()+NLWEB_APIVERSION);
            resultsApi=new ResultsApi(apiClient);
        }
        else
        {
            logger.error("SLIRetriever : there is no Neooad API url defined");
            throw new NeoLoadSLIException("SLIRetriever : THere is no Neoload API url defined");
        }
    }

    public KeptnIndicatorsValue getIndicatorValue(String key,NeoLoadSLI sli) throws NeoLoadSLIException, ApiException {
        String id=null;
        List<String> listid;
        switch (sli.getMetricType().toUpperCase())
        {

            case NeoLoadSLI.GLOBAL:
                id="all-requests";
                listid=new ArrayList<>();
                listid.add(id);
            break;


            case NeoLoadSLI.MONITORING:
                listid=getMetricId(sli.getMetricType(),sli.getElementName());
            break;

            default:
            //---for PAGE, REQUEST and TRANSACTION
                listid=getMetricId(sli.getMetricType(),sli.getElementName());
            break;
        }

        if(listid.size()>1)
        {
            logger.info("GetINdicator Value : several ids found in NeoLoad for "+sli.getMetricType() +" name "+ sli.getElementName());
            logger.info("GetINdicator Value : name "+ sli.getElementName()+" the first value would be used ");
            id=listid.stream().findFirst().get();
        }
        else
        {
            if(listid.size()>0)
                id=listid.stream().findFirst().get();

        }

        if(id!=null)
        {
            return getIndicatorValue(key,sli.getMetricType(),sli.getStatistics(),id);
        }
        else {
            logger.error("GetIndicator : no id found for " +sli.getElementName());
            return null;
        }

    }


    private double getMonitorValue(String statistic,CounterValues counterValues)
    {
        switch (statistic.toUpperCase())
        {
            case NeoLoadSLI.AVG:
                return counterValues.getAvg();

            case NeoLoadSLI.MAX:
                return counterValues.getMin();

            case NeoLoadSLI.MIN:
                return counterValues.getMin();
        }
        return 0;
    }
    private double getElementValue(String statistic,ElementValues elementValues)
    {
        switch (statistic.toUpperCase())
        {
            case NeoLoadSLI.AVG_DURATION:
                return elementValues.getAvgDuration();


            case NeoLoadSLI.MAX_DURATION:
                return elementValues.getMaxDuration();


            case NeoLoadSLI.MIN_DURATION:
                return elementValues.getMinDuration();

            case NeoLoadSLI.AVG_TTFB:
                return elementValues.getAvgTTFB();


            case NeoLoadSLI.MIN_TTFB:
                return elementValues.getMinTTFB();

            case NeoLoadSLI.MAX_TTFB:
                return elementValues.getMaxTTFB();


            case NeoLoadSLI.DOWNLOADED_BYTES_PER_SECOND:
                return elementValues.getDownloadedBytesPerSecond();

            case NeoLoadSLI.ELEMENTS_PER_SECOND:
                return elementValues.getElementPerSecond();


            case NeoLoadSLI.FAILURE_COUNT:
                return elementValues.getFailureCount();

            case NeoLoadSLI.FAILURE_PER_SECOND:
                return elementValues.getFailurePerSecond();

            case NeoLoadSLI.FAILURE_RATE:
                return elementValues.getFailureRate();

            case NeoLoadSLI.P50:
                return elementValues.getPercentile50();

            case NeoLoadSLI.P90:
                return elementValues.getPercentile90();

            case NeoLoadSLI.P95:
                return elementValues.getPercentile95();

            case NeoLoadSLI.P99:
                return elementValues.getPercentile99();

            case NeoLoadSLI.SUCCESS_COUNT:
                return elementValues.getSuccessCount();

            case NeoLoadSLI.SUCCESS_PER_SECOND:
                return elementValues.getSuccessPerSecond();

            case NeoLoadSLI.SUCCESS_RATE:
                return elementValues.getSuccessRate();


        }
        return 0;
    }

    private KeptnIndicatorsValue getIndicatorValue(String key,String metricType,String statisctic,String metricid) throws ApiException {
       //#TODO add the part to return of range points to support neoloadSLI/scope = RANGE and PERCENTILE
        if(!metricType.toUpperCase().equalsIgnoreCase(NeoLoadSLI.MONITORING)) {
            ElementValues elementValues=resultsApi.getTestElementsValues(testid,metricid);
            if(elementValues!=null)
                return new KeptnIndicatorsValue(key,getElementValue(statisctic,elementValues),true);
            else
                return new KeptnIndicatorsValue(key,0,false);

        }
        else
        {
            CounterValues counterValues=resultsApi.getTestMonitorsValues(testid,metricid);
            if(counterValues!=null)
            {
                return new KeptnIndicatorsValue(key,getMonitorValue(statisctic,counterValues),true);
            }
            else
            {
                return new KeptnIndicatorsValue(key,0,false);
            }
        }
    }

    private List<String> getMetricId(String metricType,String metricName) throws ApiException, NeoLoadSLIException {
        List<String> metricid=new ArrayList<>();
        if(!metricType.toUpperCase().equalsIgnoreCase(NeoLoadSLI.MONITORING))
        {

            ArrayOfElementDefinition arrayOfElementDefinition =resultsApi.getTestElements(testid,metricType.toUpperCase());
            if(arrayOfElementDefinition!=null) {
                arrayOfElementDefinition.forEach(elementDefinition -> {
                    if(elementDefinition.getPath().contains(metricName))
                    {
                        metricid.add(elementDefinition.getId());
                    }
                });
                if(metricid.size()<=0)
                {
                    logger.error("Getmetricid no element find with this name "+metricName);
                    throw new NeoLoadSLIException("GetMetricId: no Element find with the neame " + metricName);
                }
                else
                {
                    if(metricid.size()>1)
                    {
                        logger.info("there are several metrics with the same name "+ metricName);
                    }


                }
            }
            else
            {
                logger.error("GetMetricid : Impossible to find any elements");
            }
        }
        else
        {
            ArrayOfCounterDefinition arrayOfCounterDefinition=resultsApi.getTestMonitors(testid);
            arrayOfCounterDefinition.forEach(counterDefinition -> {
                if(counterDefinition.getName().equalsIgnoreCase(metricName))
                    metricid.add(counterDefinition.getId());
            });
        }
        return metricid;
    }

    private void getSecrets()
    {

        logger.debug("retrieve the environement variables for neoload  neoload service ");
        neoloadAPitoken=System.getenv(SECRET_API_TOKEN);
        neoloadweb_apiurl= Optional.ofNullable(System.getenv(SECRET_NL_API_HOST));
        neoloadweb_url=Optional.ofNullable(System.getenv(SECRET_NL_WEB_HOST));

    }
}

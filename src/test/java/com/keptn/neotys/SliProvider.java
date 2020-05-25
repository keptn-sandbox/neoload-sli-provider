package com.keptn.neotys;

import com.keptn.neotys.SLIProvider.DataModel.NeoLoadSLI;
import com.keptn.neotys.SLIProvider.SLIHandler.KeptnIndicatorsValue;
import com.keptn.neotys.SLIProvider.exception.NeoLoadSLIException;
import com.neotys.ascode.swagger.client.ApiClient;
import com.neotys.ascode.swagger.client.ApiException;
import com.neotys.ascode.swagger.client.api.ResultsApi;
import com.neotys.ascode.swagger.client.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.NLWEB_APIVERSION;
import static com.keptn.neotys.SLIProvider.conf.NeoLoadConfiguration.NLWEB_PROTOCOL;

public class SliProvider {
    String apitoken="TOKEN";
    String apirul="neoload-api.saas.neotys.com";
    String testid="TESTID";
    ResultsApi resultsApi;
    // order_p95:ee090070-93e3-49b2-9e3b-7da0dccce9bc
    //      metricType: TRANSACTION
    //      scope: AGGREGATED
    //      statistics: P95
    //      elementName: Order
    //    error_nl_rate:
    //      metricType: GLOBAL
    //      scope: AGGREGATED
    //      statistics: FAILURE_RATE
    //    basicCheck_p99:
    //      metricType: TRANSACTION
    //      scope: AGGREGATED
    //      statistics: P99
    //      elementName: Basic Check

    @Test
    public void testGetid()
    {

        try {
            ApiClient apiClient=new ApiClient();
            apiClient.setApiKey(apitoken);
            apiClient.setBasePath(NLWEB_PROTOCOL+apirul+NLWEB_APIVERSION);
            resultsApi=new ResultsApi(apiClient);
            List<String> ids=getMetricId("TRANSACTION","CatalogLoad/Add Item");
            KeptnIndicatorsValue value=getIndicatorValue("order_p95","TRANSACTION","P95",ids.get(0));
            List<String> ids2=getMetricId("MONITORING","Dynatrace/catalog/Process/total cpu usage");
            KeptnIndicatorsValue valuez=getIndicatorValue("cpu_process","MONITORING","AVG",ids2.get(0));
            System.out.println("value "+String.valueOf(valuez.getValue()));
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (NeoLoadSLIException e) {
            e.printStackTrace();
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

    private List<String> getMetricId(String metricType, String metricName) throws ApiException, NeoLoadSLIException {
        List<String> metricid=new ArrayList<>();
        if(!metricType.toUpperCase().equalsIgnoreCase(NeoLoadSLI.MONITORING))
        {

            ArrayOfElementDefinition arrayOfElementDefinition =resultsApi.getTestElements(testid,metricType.toUpperCase());
            if(arrayOfElementDefinition!=null) {
                arrayOfElementDefinition.forEach(elementDefinition -> {
                    if(metricName.contains("/"))
                    {
                        String[] submetricname=metricName.split("/");
                        String elementname=submetricname[submetricname.length-1];
                        if (elementDefinition.getPath() != null) {
                            List<String> common=elementDefinition.getPath();
                            common.retainAll(Arrays.asList(submetricname));
                            if (common.size()>1 && common.size()== submetricname.length) {
                                metricid.add(elementDefinition.getId());
                            }
                        } else {
                            if (elementDefinition.getName().contains(elementname)) {
                                metricid.add(elementDefinition.getId());
                            }
                        }
                    }
                    else {
                        if (elementDefinition.getPath() != null) {
                            if (elementDefinition.getPath().contains(metricName)) {
                                metricid.add(elementDefinition.getId());
                            }
                        } else {
                            if (elementDefinition.getName().contains(metricName)) {
                                metricid.add(elementDefinition.getId());
                            }
                        }
                    }
                });
                if(metricid.size()<=0)
                {
                  //  logger.error("Getmetricid no element find with this name "+metricName);
                    throw new NeoLoadSLIException("GetMetricId: no Element find with the neame " + metricName);
                }
                else
                {
                    if(metricid.size()>1)
                    {
                      //  logger.info("there are several metrics with the same name "+ metricName);
                    }


                }
            }
            else
            {
              //  logger.error("GetMetricid : Impossible to find any elements");
            }
        }
        else
        {
            ArrayOfCounterDefinition arrayOfCounterDefinition=resultsApi.getTestMonitors(testid);
            arrayOfCounterDefinition.forEach(counterDefinition -> {
                if(counterDefinition.getPath()!=null)
                {
                    String[] submetricname=metricName.split("/");
                    String elementname=submetricname[submetricname.length-1];
                    List<String> common=counterDefinition.getPath();
                    common.retainAll(Arrays.asList(submetricname));
                    if (common.size()>1 && common.size()== submetricname.length) {
                        metricid.add(counterDefinition.getId());
                    }

                }
                else {
                    if (counterDefinition.getName().equalsIgnoreCase(metricName))
                        metricid.add(counterDefinition.getId());
                }
            });
        }
        return metricid;
    }
}

package com.keptn.neotys.SLIProvider.DataModel;

import com.keptn.neotys.SLIProvider.exception.NeoLoadSLIException;

import java.util.Arrays;
import java.util.List;

public class NeoLoadSLI extends Throwable {
    String metryType;
    String statistics;
    String elementType;
    String elementName;
    String scope;

    public static final String AGGREGATED="AGGREGATED";
    public static final String RANGE="RANGE";
    public static final String PERCENTILE="PERCENTILE";
    private List<String> allowScope= Arrays.asList(new String[]{RANGE,AGGREGATED,PERCENTILE});

    public static final String TRANSACTION="TRANSACTION";
    public static  final String REQUEST="REQUEST";
    public static final String PAGE="PAGE";
    public static final String GLOBAL="GLOBAL";
    public static final String MONITORING="MONITORING";
    private List<String> allowedMetryType= Arrays.asList(new String[]{TRANSACTION,REQUEST,PAGE,GLOBAL, MONITORING});


    public static final String AVG_DURATION="AVG_DURATION";
    public static final String MIN_DURATION ="MIN_DURATION";
    public static final String MAX_DURATION="MAX_DURATION";
    private static final String COUNT="COUNT";
    private static final String THROUGHPUT="THROUGHPUT";
    public static final String ELEMENTS_PER_SECOND ="ELEMENTS_PER_SECOND";
    private static final String ERRORS="ELEMENTS_PER_SECOND";
    private static final String ERRORS_PER_SECOND="ERRORS_PER_SECOND";
    private static final String  ERROR_RATE="ERROR_RATE";
    public static final String AVG_TTFB="AVG_TTFB";
    public static final String MIN_TTFB="MIN_TTFB";
    public static final String MAX_TTFB="MAX_TTFB";
    public static final String AVG="AVG";
    private List<String> allowedPointsElements= Arrays.asList(new String[]{AVG_DURATION,MIN_DURATION,MAX_DURATION,COUNT, THROUGHPUT, ELEMENTS_PER_SECOND,ERRORS,ERRORS_PER_SECOND,ERROR_RATE,AVG_TTFB,MIN_TTFB,MAX_TTFB});

    public static final String SUCCESS_COUNT="SUCCESS_COUNT";
    public static final String SUCCESS_PER_SECOND="SUCCESS_PER_SECOND";
    public static final String SUCCESS_RATE="SUCCESS_RATE";
    public static final String FAILURE_COUNT="FAILURE_COUNT";
    public static final String FAILURE_PER_SECOND="FAILURE_PER_SECOND";
    public static final String FAILURE_RATE="FAILURE_RATE";
    public static final String P50="P50";
    public static final String P90="P90";
    public static final String P95="P95";
    public static final String P99="P99";
    public static final String DOWNLOADED_BYTES_PER_SECOND="DOWNLOADED_BYTES_PER_SECOND";
    private List<String> allowedValuesElements=Arrays.asList(new String[]{ELEMENTS_PER_SECOND,MAX_DURATION,MIN_DURATION,AVG_DURATION,MIN_TTFB,MAX_TTFB,AVG_TTFB,DOWNLOADED_BYTES_PER_SECOND,SUCCESS_COUNT,SUCCESS_PER_SECOND,SUCCESS_RATE,FAILURE_COUNT,FAILURE_PER_SECOND,FAILURE_RATE,P50,P90,P95,P99});

    private List<String> allowedPointsMonitoring=Arrays.asList(new String[]{AVG});
    public static final String MIN="MIN";
    public static final String MAX="MAX";
    private List<String> allowedValuesMonitoring=Arrays.asList(new String[]{AVG,MIN,MAX});

    //----values
    //"count": 0,
    //  "elementPerSecond": 0,
    //  "minDuration": 0,
    //  "maxDuration": 0,
    //  "sumDuration": 0,
    //  "avgDuration": 0,
    //  "minTTFB": 0,
    //  "maxTTFB": 0,
    //  "sumTTFB": 0,
    //  "avgTTFB": 0,
    //  "sumDownloadedBytes": 0,
    //  "downloadedBytesPerSecond": 0,
    //  "successCount": 0,
    //  "successPerSecond": 0,
    //  "successRate": 0,
    //  "failureCount": 0,
    //  "failurePerSecond": 0,
    //  "failureRate": 0,
    //  "percentile50": 0,
    //  "percentile90": 0,
    //  "percentile95": 0,
    //  "percentile99": 0


    // Points
    // "AVG_DURATION": 0,
    //    "MIN_DURATION": 0,
    //    "MAX_DURATION": 0,
    //    "COUNT": 0,
    //    "THROUGHPUT": 0,
    //    "ELEMENTS_PER_SECOND": 0,
    //    "ERRORS": 0,
    //    "ERRORS_PER_SECOND": 0,
    //    "ERROR_RATE": 0,
    //    "AVG_TTFB": 0,
    //    "MIN_TTFB": 0,
    //    "MAX_TTFB": 0,
    //    "AVG": 0

    //--Values for monitoring
    // "count": 0,
    //  "min": 0,
    //  "max": 0,
    //  "sum": 0,
    //  "avg": 0


    public String getMetryType() {
        return metryType;
    }

    public void setMetryType(String metryType) {
        this.metryType = metryType;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public NeoLoadSLI(String metryType,String statistics,String elementType,String metricname,String scope) {
        this.metryType = metryType;
        this.statistics = statistics;
        this.elementType = elementType;
        this.elementName = metricname;
        this.scope=scope;
    }

    public boolean validateMetric() throws NeoLoadSLIException
    {
        if(!allowedMetryType.contains(getElementType().toUpperCase()))
        {
            throw new NeoLoadSLIException("Metrictype needs to be equal to one of the following values : "+ String.join(",",allowedMetryType));
        }
        else
        {
           if(!allowScope.contains(getScope().toUpperCase()))
           {
               throw new NeoLoadSLIException("scope needs to be equal to one of the following values : "+ String.join(",",allowScope));
           }

            switch (getScope().toUpperCase()) {
                case RANGE:
                        if(getMetryType().equalsIgnoreCase(MONITORING))
                        {
                            if(!allowedPointsMonitoring.contains(getStatistics().toUpperCase()))
                            {
                                throw new NeoLoadSLIException("Statistics needs to be equal to one of the following values : "+ String.join(",",allowedPointsMonitoring));

                            }
                        }
                        else
                        {
                            if(!allowedPointsElements.contains(getStatistics().toUpperCase()))
                            {
                                throw new NeoLoadSLIException("Statistics needs to be equal to one of the following values : "+ String.join(",",allowedPointsElements));

                            }
                        }
                    break;
                case AGGREGATED:
                    if(getMetryType().equalsIgnoreCase(MONITORING))
                    {
                        if(!allowedValuesMonitoring.contains(getStatistics().toUpperCase()))
                        {
                            throw new NeoLoadSLIException("Statistics needs to be equal to one of the following values : "+ String.join(",",allowedValuesMonitoring));

                        }
                    }
                    else
                    {
                        if(!allowedValuesElements.contains(getStatistics().toUpperCase()))
                        {
                            throw new NeoLoadSLIException("Statistics needs to be equal to one of the following values : "+ String.join(",",allowedValuesElements));
                        }
                    }

                    break;
                case PERCENTILE:
                    if(getMetryType().equalsIgnoreCase(MONITORING))
                    {
                        throw new NeoLoadSLIException("Percentile is only available on PAGE,REQUEST, TRANSACTION or GLOBAL");

                    }

                    break;
            }
        }

        return true;

    }

}

# Keptn NeoLoad SLI Service

This neoload-sli-service is a Keptn service that is responsible for retrieving the values of Keptn-supported SLIs from a NeoLoad test.

The neoload-sli-service will be called by the Keptn Quality Gate if you define SLIs and SLOs using the neoload datasource

## NeoLoad indicators
Neoload sli service does not provide any predefined indicators.
Each project would be able to collect indicateors by building neoload/sli.yaml file.

```yaml
spec_version: '1.0'
indicators:
    hit_per_second:
      metricType: GLOBAL
      scope: AGGREGATED
      statistics: ELEMENTS_PER_SECOND
    additem_p95:
      metricType: TRANSACTION
      scope: AGGREGATED
      statistics: P95
      elementName: Add Item
    addcustomer_p95:
      metricType: TRANSACTION
      scope: AGGREGATED
      statistics: P95
      elementName: Add customer
    order_p95:
      metricType: TRANSACTION
      scope: AGGREGATED
      statistics: P95
      elementName: Order
    error_rate:
      metricType: GLOBAL
      scope: AGGREGATED
      statistics: FAILURE_RATE
    basicCheck_p99:
      metricType: TRANSACTION
      scope: AGGREGATED
      statistics: P99
      elementName: Basic Check
```

Each indicator is defined by :
a unique name `basicCheck_p99` for example.
The indicator will be defined by :
* metricType : Could be equal to : `GLOBAL`,`TRANSACTION`,`PAGE`,`REQUEST`,`MONITORING`
* scope : Currently Neoload-sli-provider will only support `AGGREGATED`'
* statistics: the value statistics depends on the metricType 
* elementName : Name of the element ( Transaction, Page, Request,Monitoring )


To store this configuration, you need to add this file to a Keptn's configuration store. This is done by using  Keptn CLI with the [add-resource](https://keptn.sh/docs/0.6.0/reference/cli/#keptn-add-resource) command. 

## available Statistics 
The property statistic accept the following values depending on the metricType :
* GLOBAL (all-requests), TRANSACTION, PAGE, REQUEST :
    * ELEMENTS_PER_SECOND
    * DOWNLOADED_BYTES_PER_SECOND
    * AVG_DURATION (ms)
    * MIN_DURATION (ms)
    * MAX_DURATION (ms)
    * AVG_TTFB where TTFB stands for Time To First Byte
    * MAX_TTFB
    * MIN_TTFB
    * SUCCESS_COUNT
    * SUCCESS_PER_SECOND
    * SUCCESS_RATE
    * FAILURE_COUNT
    * FAILURE_PER_SECOND
    * FAILURE_RATE
    * P50 : 50 Percentile in ms (only for metricType TRANSACTION)
    * P90 : 90 Percentile in ms (only for metricType TRANSACTION)
    * P95 : 95 Percentile in ms (only for metricType TRANSACTION)
    * P99 : 99 Percentile in ms (only for metricType TRANSACTION)
* MONITORING 
    * AVG
    * MIN
    * MAX

Example :
```yaml
basicCheck_p99:
  metricType: TRANSACTION
  scope: AGGREGATED
  statistics: P99
  elementName: Basic Check
hit_per_second:
  metricType: GLOBAL
  scope: AGGREGATED
  statistics: ELEMENTS_PER_SECOND
```
`basicCheck_p99` means the 99 Percentile of the Transaction named "Basic Check"
`hit_per_second` means the Number of request per seconds


## Secret for credentials
During the setup of neoload-sli-service, a secret is created that contains key-value pairs for the NeoLoad  URL, NeoLoad apiKey:
   * NL_WEB_HOST 
   * NL_API_HOST 
   * NL_API_TOKEN
    

## Install service <a id="install"></a>

1. To install the service, you need to run :
 * installer/defineNeoLoadWebCredentials.sh to configure the required parameters :
    1. NL_WEB_HOST : host of the web ui of NeoLoad web
    1. NL_API_HOST : host of the api of NeoLoad web
    1. NL_API_TOKEN: api token of your NeoLoad account
    
1. Run the deployment script : `installer/deployNeoLoadWeb.sh`  


## Enable the SLi provider in Keptn
Create a file `lighthous-source-neoload.yaml `
```yaml
apiVersion: v1
data:
  sli-provider: neoload
kind: ConfigMap
metadata:
  name: lighthouse-config-<YOUR KEPTN PROJECT NAME>
  namespace: keptn
 ```
Replace the `<YOUR KEPTN PROJECT NAME>` with your project name
Enable the neoload provider by running this command :
```
kubectl apply -f lighthouse-source-neoload.yaml    
```
## Uninstall the neoload-sli-service

To uninstall the neoload-sli-serice you will need to run the following script : `installer/uninstallNeoLoadService.sh`

 
   

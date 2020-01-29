# Keptn NeoLoad SLI Service

This neoload-sli-service is a Keptn service that is responsible for retrieving the values of Keptn-supported SLIs from a NeoLoad test.

The neoload-sli-service will be called by the Keptn Quality Gate if you define SLIs and SLOs using the neoload datasource

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

## Uninstall the neoload-sli-service

To uninstall the neoload-sli-serice you will need to run the following script : `installer/uninstallNeoLoadService.sh`

 
    [here](https://keptn.sh/docs/0.6.0/installation/setup-keptn/) is the ling to keptn's documentation. 

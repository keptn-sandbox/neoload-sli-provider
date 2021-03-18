package com.keptn.neotys.SLIProvider.conf;

public class NeoLoadConfiguration {
    public static int PORT = 8080;
    public static final int KUBETIMEOUT=60;
    public static String HEALTH_PATH="/health";
    public static String NEOLOAD_SOURCE = "neoload-sli-provider";
    public static String NEOLOAD_PROVIDER = "neoload";
    public static String KEPTN_EVENT_HOST = "event-broker.";
    public static String KEPTN_END_URL=".svc.cluster.local";
    public static String KEPTN_EVENT_URL = "keptn";
    public static int KEPTN_PORT=8080;
    public static int KEPTN_PORT_EVENT=80;

    public static String LOGING_LEVEL_KEY="logging-level";
    public static String NEOLOAD_SLI_RESSOURCE=NEOLOAD_PROVIDER+"%2Fsli.yaml";

    public static String HEADER_KEPTNCONTEXT="shkeptncontext";
    public static String HEADER_datacontentype="datacontenttype";
    public static String HEADER_triggeredid="triggeredid";
    public static String HEADER_shkeptnspecversion="shkeptnspecversion";

    public static final String YML_EXTENSION=".yml";
    public static final String YAML_EXTENSION=".yaml";
    public static final String NLP_EXTENSION=".nlp";
    public static final String ON_PREM_ZONE="ON_PREMISE_LOAD_GENERATOR";
    public static final String NEOLOAD_SECRET="neoload";
    public static final String SECRET_API_TOKEN="NL_API_TOKEN";
    public static final String SECRET_NL_WEB_HOST="NL_WEB_HOST";
    public static final String SECRET_NL_API_HOST="NL_API_HOST";
    public static final String SECRET_NL_ZONEID="NL_WEB_ZONEID";
    public static final String SECRET_NL_UPLOAD_HOST="NL_UPLOAD_HOST";
    public static final String SECRET_KEPTN_NAMESPACE="KEPTN_NAMESPACE";

    public static final String NLWEB_PROTOCOL="https://";
    public static final String NLWEB_APIVERSION="/v1";

    public static final String TEST_STATUS_FAIL="FAILED";
    public static final String DEFAULT_BRANCH="master";
    public static final String CONFIGURATIONAPI_HOST="configuration-service.";
    public static final int CONFIGURAITON_PORT=8080;
    public static final String CONFIGURATION_PROTOCOL="http://";
    public static final String CONFIGURATION_VERSION="v1";
    public static final String CONFIGURATION_PROJECT="project";
    public static final String CONFIGURATION_STAGE="stage";
    public static final String CONFIGURATION_RESSOURCE="resource";
    public static final String CONFIGURATION_SERVICE="service";

    public static final String CONTENT_TYPE="Content-Type";

    public static final String HEADER_ACCEPT="accept";
    public static final String HEADER_APPLICATIONJSON="application/json";
}
#!/bin/bash

NL_WEB_HOST=$(cat creds_nl.json | jq -r '.nlwebWebHost')
NL_API_HOST=$(cat creds_nl.json | jq -r '.nlwebAPIHost')
NL_API_TOKEN=$(cat creds_nl.json | jq -r '.nlwebAPIToken')

# Create secrets to be used by neoload-service
echo "Create secrets to be used by neoload-service"
kubectl -n keptn create secret generic neoload-sli --from-literal="NL_WEB_HOST=$NL_WEB_HOST" --from-literal="NL_API_HOST=$NL_API_HOST"  --from-literal="NL_API_TOKEN=$NL_API_TOKEN"


# Create dynatrace-service
NL_SERVICE_RELEASE="0.7.0"

echo "Deploying neoload-sli-service $NL_SERVICE_RELEASE"
wget https://raw.githubusercontent.com/keptn-contrib/neoload-sli-provider/$NL_SERVICE_RELEASE/config/service.yaml -O service.yaml
wget https://raw.githubusercontent.com/keptn-contrib/neoload-sli-provider/$NL_SERVICE_RELEASE/config/distributor.yaml -O distributor.yaml

# to update the link
[  -z "$1" ] && NAMESPACE="keptn" || NAMESPACE=$1

sed -i 's/NAMESPACE_TO_REPLACE/$NAMESPACE/' service.yaml
sed -i 's/NAMESPACE_TO_REPLACE/$NAMESPACE/' distributor.yaml
kubectl apply -f service.yaml
kubectl apply -f distributor.yaml

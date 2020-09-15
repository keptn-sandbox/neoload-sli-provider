#!/usr/bin/env bash
kubectl delete secret neoload -n keptn --ignore-not-found

# Create dynatrace-service
NL_SERVICE_RELEASE="0.7.0"

echo "Delete neoload-sli-service $NL_SERVICE_RELEASE"
# to update the link
wget https://raw.githubusercontent.com/keptn-contrib/neoload-sli-provider/$NL_SERVICE_RELEASE/config/service.yaml -O service.yaml
wget https://raw.githubusercontent.com/keptn-contrib/neoload-sli-provider/$NL_SERVICE_RELEASE/config/distributor.yaml -O distributor.yaml

# to update the link
[  -z "$1" ] && NAMESPACE="keptn" || NAMESPACE=$1

sed -i 's/NAMESPACE_TO_REPLACE/$NAMESPACE/' service.yaml
sed -i 's/NAMESPACE_TO_REPLACE/$NAMESPACE/' distributor.yaml
kubectl delete -f service.yaml --ignore-not-found
kubectl delete -f distributor.yaml --ignore-not-found

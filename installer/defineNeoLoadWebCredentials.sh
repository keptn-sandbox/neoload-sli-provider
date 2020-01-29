#!/bin/bash

YLW='\033[1;33m'
NC='\033[0m'

CREDS=./creds_nl.json
rm $CREDS 2> /dev/null

echo -e "${YLW}Please enter the credentials as requested below: ${NC}"
read -p "NeoLoad Web host Web URL (by default  neoload.saas.neotys.com  for SaaS (default=$NLWEB): " NLWEBC
read -p "NeoLoad Web host API URL (by default  neoload-api.saas.neotys.com  for SaaS (default=$NLWEBAPI): " NLWEBAPIC
read -p "Neoload Web  API Token (default=$NLAPI): " NLAPIC
echo ""

if [[ $NLWEB = '' ]]
then
    NLWEB=$NLWEBC
fi

if [[ $NLWEBAPI = '' ]]
then
    NLWEBAPI=$NLWEBAPIC
fi

if [[ $NLAPI = '' ]]
then
    NLAPI=$NLAPIC
fi



echo ""
echo -e "${YLW}Please confirm all are correct: ${NC}"
echo "NL Web HOST WEB : $NLWEB"
echo "NL WEB API Host : $NLWEBAPI"
echo "NL web API token: $NLAPI"
read -p "Is this all correct? (y/n) : " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]
then
    rm $CREDS 2> /dev/null
    cat ./creds_nl.sav | sed 's~NL_WEB_HOST_PLACEHOLDER~'"$NLWEB"'~' | \
      sed 's~NL_API_HOST_PLACEHOLDER~'"$NLWEBAPI"'~' | \
      sed 's~NL_WEB_TOKEN~'"$NLAPI"'~'>> $CREDS
fi

cat $CREDS
echo ""
echo "The credentials file can be found here:" $CREDS
echo ""
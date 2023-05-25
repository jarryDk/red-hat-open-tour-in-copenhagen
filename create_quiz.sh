#!/bin/bash

set -e

if [ -z "$1" ] ; then
    echo "Default endpoint : http://localhost:8080/quizs"
    ENDPOINT=http://localhost:8080/quizs
else
    echo "Endpoint : $1"
    ENDPOINT=$1
fi

TIMESTAMP=`date +%Y-%m-%dT%H:%M:%S:%3N`

time curl -X POST $ENDPOINT \
	-H 'Accept: application/json' \
	-H 'Content-Type: application/json' \
	-d '{"name":"NoName","questionNoOne":"Content - Timestamp : '$TIMESTAMP'","questionNoTwo":"No2","questionNoTree":"No3"}' | jq

#!/bin/bash
set -e

pushd `dirname $0`

# Start the services and wait for it.
docker-compose up -d --build

STATUS=""
until [[ ${STATUS} = "healthy" ]]; do
    STATUS=`docker inspect --format='{{.State.Health.Status}}' clojure-couchbase-testdb`
    echo ${STATUS}
    sleep 5
done

docker-compose ps

# Initialize couchbase cluster, bucket
exec ./init-db.sh

popd

#!/usr/bin/env bash

cd src\main\webapp\frontend

docker rm -f currency-exchange-frontend-nginx
docker container run -d --name currency-exchange-frontend-nginx -p 80:80 -v ${PWD}:/usr/share/nginx/html nginx
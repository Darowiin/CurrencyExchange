#!/bin/bash

docker rm -f currency-exchange-frontend-nginx
docker run -d \
   --name currency-exchange-frontend-nginx \
   -p 80:80 \
   --mount type=bind,source=/c/Users/Darowiin/IdeaProjects/CurrencyExchange/src/main/webapp/frontend,target=/usr/share/nginx/html \
   --mount type=bind,source=/c/Users/Darowiin/IdeaProjects/CurrencyExchange/src/main/webapp/frontend/nginx.conf,target=/etc/nginx/conf.d/default.conf,readonly \
   nginx
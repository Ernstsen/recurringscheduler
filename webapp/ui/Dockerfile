FROM node:20-bookworm as build-stage


COPY ./admin-ui /tmp/admin-ui
COPY ./collector-ui /tmp/collector-ui

#WORKDIR /tmp/admin-ui
#RUN npm ci
#RUN npm run build
#RUN cp -r ./dist /usr/src/app/dist/admin

#WORKDIR /tmp/collector-ui
#RUN npm ci && npm run build && cp -r ./dist /usr/src/app/dist/collect

RUN mkdir -p /usr/src/app/dist/
RUN cp -r /tmp/admin-ui/dist /usr/src/app/dist/admin
RUN cp -r /tmp/collector-ui/dist /usr/src/app/dist/collect

FROM caddy:2.7.6-alpine

COPY --from=build-stage /usr/src/app/dist/admin /var/www/html/admin
COPY --from=build-stage /usr/src/app/dist/collect /var/www/html/collect

COPY ./config/Caddyfile /etc/caddy/Caddyfile

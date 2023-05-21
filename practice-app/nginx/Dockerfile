FROM nginx:1.21.1

ARG nginx_uid=0
ARG nginx_gid=0

RUN usermod -u $nginx_uid -o nginx && groupmod -g $nginx_gid -o nginx

COPY configuration/nginx.conf /etc/nginx/conf.d/default.conf
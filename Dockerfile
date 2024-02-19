FROM nginx
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY resources/public /usr/share/nginx/html

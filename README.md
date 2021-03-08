To assign domain to digital ocean droplet in porkbun add nameservers to AUTHORITATIVE NAMESERVERS and wait some time

https:
correct in init-letsencrypt.sh docker-compose paths and data folder path. To dry run use --dry-run option for certbot

docker-compose:
```shell script
version: '3'
services:
  nginx:
    container_name: nginx
    image: nginx:stable-alpine
    restart: always
    ports:
      - 80:80
      - 443:443
    volumes:
      - ../../nginx/prod:/etc/nginx/conf.d
      - ../../../data/certbot/conf:/etc/letsencrypt
      - ../../../data/certbot/www:/var/www/certbot
    command: "/bin/sh -c 'while :; do sleep 6h & wait $${!}; nginx -s reload; done & nginx -g \"daemon off;\"'"
  certbot:
    image: certbot/certbot
    volumes:
      - ../../../data/certbot/conf:/etc/letsencrypt
      - ../../../data/certbot/www:/var/www/certbot
    entrypoint: "/bin/sh -c 'trap exit TERM; while :; do certbot renew; sleep 12h & wait $${!}; done;'"
  web:
    container_name: web
    restart: always
    build: ../../../
    volumes:
      - ../../../logs:/web/logs
```
```shell script
sudo ufw allow http
sudo ufw allow https
DO firewall http https 
```
nginx.conf
```text
server {
  listen 80;
  listen [::]:80;
  server_name ~. ;

  location /.well-known/acme-challenge {
    root /var/www/certbot;
    default_type text/plain;
  }

  location / {
    return 301 https://$host$uri;
  }
}

server {
  listen 443 ssl;
  listen [::]:443 ssl;
  server_name smartbots.click;

  root /var/www;

  location / {
      proxy_pass http://web:8080;
      proxy_set_header Host $host:$server_port;
      proxy_set_header X-Forwarded-Host $server_name;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }

  ssl_certificate /etc/letsencrypt/live/smartbots.click/fullchain.pem;
  ssl_certificate_key /etc/letsencrypt/live/smartbots.click/privkey.pem;

  include /etc/letsencrypt/options-ssl-nginx.conf;
  ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;
}
```
```shell script
docker-compose build
chmod +x init-letsencrypt.sh
sudo ./init-letsencrypt.sh
````

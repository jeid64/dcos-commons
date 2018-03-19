FROM alpine

RUN apk add --no-cache bind

COPY docker_include/named.conf /etc/bind/named.conf
COPY docker_include/db.jeidisthebest.jeidtld /etc/bind/db.jeidisthebest.jeidtld
COPY docker_include/db.poison.com /etc/bind/db.poison.com

CMD ["/usr/sbin/named", "-g"]

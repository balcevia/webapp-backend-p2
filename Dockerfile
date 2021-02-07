FROM java:8
WORKDIR .
RUN mkdir -p /var/www/html/pdf
COPY webapp-assembly-1.0.jar /
CMD java -jar webapp-assembly-1.0.jar
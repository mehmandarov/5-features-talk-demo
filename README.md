# My demo app
This app is used to demo failover and interaction between several webservices, showcasing failover, metrics, health, etc.

## Building
1. Checkout the code
2. Run `mvn clean verify` the root folder

## Running all services in a containers using ´compose´
_**Prerequisite: Expects Unleash running on ´http://unlesh_web_1:4242/api´ and Unleash network called ´unleash_default´.**_

Run using Podman:
```shell script
podman-compose up -d 
```

Run using Docker:
```shell script
docker compose up -d 
```

* ´Dockerfile´s that are being used can be found in the root folder of each service.
* Add ´--build´ option if you want to force image build.
* Remove ´-d´ if you don't want to run in daemon mode.

Default ports:
* ´demo-service1´ will be available at port ´9081´, on: http://localhost:9081/hi
* ´demo-backendservice1´ will be available at port ´9082´, on: http://localhost:9082/hi

## Login to Unleash
http://localhost:4242/

## Running each service separately
Go to the desired service, run:
```shell script
./mvnw compile quarkus:dev
```

## Debugging
### 1. Attach for logs
```shell script
podman logs -f ws-client
```
Where `ws-client` is a container name.


### 2. SSH to a container
```shell script
podman exec -ti ws-client bash
```
Where `ws-client` is a container name.


### 3. Network
#### 3.1 Show IP 
```shell script
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ws-client
```
Where `ws-client` is a container name. 

#### 3.2 List networks
```shell script
docker network ls
```
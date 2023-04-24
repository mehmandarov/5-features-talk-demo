# My demo app
This app is used to demo failover and interaction between several webservices, showcasing failover, metrics, health, etc. in Java microservices using Quarkus and Jakarta EE + MicroProfile.

## Architecture

```
           demo-service1                        demo-backendservice1
         ┌────────────────┐                  ┌────────────────────────┐
         │                │                  │                        │
         │ Service A      │                  │ Service B              │
         │ =========      │                  │ =========              │
         │                │                  │                        │
         │ Endpoints:     │  HTTP            │ Endpoints:             │
"feature"│ * /hi     ─────┼───────┐          │ * /hi                  │   "feature"
┌────────┤ * /feature     │       │    ok    │ * /feature      ───────┼────────────┐
│        │                │       ├──────────► * /api/message  ───────┼────────────┤
│        │                │       └──────────► * /api/backup_message  │    "chaos" │
│        │                │            error │                        │    "delay" │
│        │                │  HTTP            │                        │            │
│        │                ◄──────────────────┤                        │            │
│        └────────────────┘                  └────────────────────────┘            │
│                                                                                  │
│                                                                                  │
│                             unleash_web_1                                        │
│                             ┌─────────────┐                                      │
│                             │             │                                      │
│                             │ Feature     │                                      │
└─────────────────────────────► Toggle      ◄──────────────────────────────────────┘
                              │ Service     │
                              │ ========    │
                              │             │
                              │ Toggles:    │
                              │ * "feature" │
                              │ * "chaos"   │
                              │ * "delay"   │
                              └─────────────┘
```

## Build this project
1. Checkout the code
2. Run `mvn clean verify` from the root folder

## Run this project

### Running all services in a containers using `compose`
_**Prerequisites:**_
* Services expect **Unleash running on** http://unleash_web_1:4242/api
  * backend service URL is [configured here](./demo-backendservice1/src/main/java/DemoApp.java)
  * client service URL is [configured here](./demo-service1/src/main/java/DemoApp.java) 
* Unleash Podman/Docker **network** called `unleash_default`
* Unleash needs to have these **toggles**: `feature`, `chaos`, and `delay`.

_**Running project:**_
* All services can be run using `Compose`. See below for commands to do so.
* `Dockerfile`s that are being used for each service can be found in the root folder of that service.
* Add `--build` option if you want to _force image build_.
* Remove `-d` if you don't want to run in _daemon mode_.

##### Run all services using Podman Compose
```shell script
podman-compose up -d 
```

##### Run all services using Docker Compose
```shell script
docker compose up -d 
```

#### Default URLs and ports, when using `Compose`:
* `demo-service1` will be available at port `9081`, on: http://localhost:9081/hi
* `demo-backendservice1` will be available at port `9082`, on: http://localhost:9082/hi

Other links:
* Health:
  * Client: http://localhost:9081/health {/ready, /live, /started}
  * Backend: http://localhost:9082/health
* Metrics:
  * Client: http://localhost:9081/metrics
  * Backend: http://localhost:9082/metrics
* OpenAPI:
  * Client: http://localhost:9081/openapi?format=json
  * Backend: http://localhost:9082/openapi?format=json


### Run each service separately and enable Quarkus dev mode
Go to the desired service folder and run Quarkus in dev mode:
```shell script
./mvnw compile quarkus:dev
```

### Login to Unleash:
http://localhost:4242/


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
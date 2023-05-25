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
## Default URLs and ports

App links:
* `demo-service1` will be available at port `9081`, on:
  * http://localhost:9081/hi
  * http://localhost:9081/feature
* `demo-backendservice1` will be available at port `9082`, on:
  * http://localhost:9082/hi
  * http://localhost:9082/feature
  * http://localhost:9082/api/message
  * http://localhost:9082/api/backup_message

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

## Building the project

1. Checkout the code
2. Run `mvn clean verify` from the root folder

## Running the project
### Running all services in a containers using `compose`

⚠️ _**Prerequisites:**_ ⚠️
You will need the Unleash containers up and running before starting this project. This typically means checking out and following instructions from Unleash repo to start the service with default settings. Then, go through the checklist below:
* Services expect **Unleash running on** http://unleash_web_1:4242/api
  * Unleash URL for backend service is [configured here](./demo-backendservice1/src/main/java/DemoApp.java)
  * Unleash URL for client service  is [configured here](./demo-service1/src/main/java/DemoApp.java) 
* Unleash Podman/Docker **network** is called `unleash_default`
* Unleash needs to have these **toggles**: `feature`, `chaos`, and `delay`.

_**Running the project:**_
* All services are run using `compose`. See below for the commands.
* `Dockerfile`s that are being used for each service can be found in the root folder for that service.

_**Options:**_
* Add `--build` option if you want to _force image build_.
* Remove `-d` if you don't want to run in _daemon mode_.

##### Run all services using Podman Compose
```shell script
podman-compose up -d 
```

##### Run all services using Docker Compose:
```shell script
docker compose up -d
```

### Development
#### Run each service separately and enable Quarkus dev mode
Go to the desired service folder and run Quarkus in dev mode:
```shell script
./mvnw compile quarkus:dev
```

#### Login to Unleash:
http://localhost:4242/

_Default credentials: admin/unleash4all_ 


## Debugging

### 1. Podman in general
**_Note:_**
If you are getting `Cannot connect to Podman` error, you will need to initialize Podman first:
```shell script
podman machine init
podman machine start
```


### 2. Attach for logs
```shell script
podman logs -f ws-client
```
Where `ws-client` is a container name.


### 3. SSH to a container
```shell script
podman exec -ti ws-client bash
```
Where `ws-client` is a container name.


### 4. Network
#### 3.1 Show IP 
```shell script
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ws-client
```
Where `ws-client` is a container name. 

#### 3.2 List networks
```shell script
docker network ls
```

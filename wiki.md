# 1. Fault Tolerance
## What happens when something you rely on fails?

### Look at [frontend: SomeWebServiceInterface](./demo-service1/src/main/java/SomeWebServiceInterface.java)     
```java
@Fallback(fallbackMethod= "getThatOtherData")
```

### ...because [backend: SomeWebServiceInterface](./demo-backendservice1/src/main/java/DemoApp.java) can fail sometimes.

# 2. Health
## What is happening inside your application?

### Look at [health package](./demo-service1/src/main/java/health):

* [Simple Health Check](./demo-service1/src/main/java/health/HealthCheckSimple.java)
* [Liveness Response Code](./demo-service1/src/main/java/health/LivenessHealthCheckResponseCode.java)
* [Liveness: Response Time](./demo-service1/src/main/java/health/LivenessHealthCheckResponseTime.java) (<---This is fun!!)
* [Readiness](./demo-service1/src/main/java/health/ReadinessHealthCheckSimple.java)


# 3. Metrics
## Define custom application metrics and expose platform metrics. On a standard endpoint, using standard formats.

Look at [backend: DemoApp.java](./demo-backendservice1/src/main/java/DemoApp.java) `otherEntryPointWithErrors()` method.

# 4. Feature toggling
## What do you have to do to make features available?

Defined in [frontend: DemoApp.java](./demo-service1/src/main/java/DemoApp.java) `DemoApp()` constructor.

Defined in [backend: DemoApp.java](./demo-backendservice1/src/main/java/DemoApp.java) `DemoApp()` constructor.

Usage example (one of many) in [backend: DemoApp.java](./demo-backendservice1/src/main/java/DemoApp.java) `featureToggle()` method. 

# 5. Environments, Configuration, Infrastructure ++
Examples:
* Terraform: https://www.terraform.io
* Pulumi: https://www.pulumi.com/
* MicroProfile Config: https://download.eclipse.org/microprofile/microprofile-config-3.0.2/microprofile-config-spec-3.0.2.html 
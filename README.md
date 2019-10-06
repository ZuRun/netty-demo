# netty-demo
netty练习



# 构建命令
```shell
gradle clean -Ppackage.environment=dev :mock-server:clean :mock-server:build
gradle clean -Ppackage.environment=dev :mock-client:clean :mock-client:build
gradle clean -Ppackage.environment=dev :mock-api-gateway:clean :mock-api-gateway:build

```
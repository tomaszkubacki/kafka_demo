## Simple demo of Kafka Producer and Consumer api usage

## build 
```shell
gradle build-fat
```

## pre-run

modify [app.properties](app%2Fsrc%2Fmain%2Fresources%2Fapp.properties)

## run producer
```shell
cd app/build/libs
java -jar kafka_demo.jar -p 
```

## run consumer
```shell
cd app/build/libs
java -jar kafka_demo.jar -c
```
 

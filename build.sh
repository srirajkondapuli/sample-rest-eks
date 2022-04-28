#!/bin/bash
echo "SpringBoot REST EKS Build!!"
gradle clean build -x test --info
echo "SpringBoot REST EKS App create Docker Image!!"
datetime=`date "+%Y-%m-%d-%H%M%S"`
echo $name
echo $datetime
docker build -t sample-rest-eks:$datetime .
echo "SpringBoot REST EKS App create Tag Image!!"
docker tag sample-rest-eks:$datetime skondapuli/sample-rest-eks:$datetime
echo "SpringBoot REST EKS App Push to Docker Registry !!"
docker push skondapuli/sample-rest-eks:$datetime


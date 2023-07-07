FROM amazoncorretto:17-alpine-jdk
RUN mkdir -p /app/agent
RUN mkdir -p /app/logs
RUN chmod 777 /app/logs
WORKDIR /app
COPY src/lib/opentelemetry-javaagent-1.12.1.jar opentel-agent.jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
#CMD ["java","-javaagent:opentel-agent.jar","-Dotel.exporter.endpoint=http://localhost:4317","-Dotel.javaagent.debug=false","-Dotel.metrics.exporter=none","-Dotel.resource.attributes=service.name=sample-spring-boot-tracing-jre","-jar","app.jar"]
CMD ["java", "-jar", "app.jar"]


EXPOSE 8000

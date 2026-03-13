FROM eclipse-temurin:17-jdk as stage1
WORKDIR /app
# 폴더는 폴더명 적어줘야함
COPY gradle gradle
COPY src src
# 파일은 경로만 적어주면 됨 
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .
RUN chmod +x gradlew
RUN ./gradlew bootjar

# 두번째 스테이지 : 이미지 경량화를 위해 스테이지 분리작업
FROM eclipse-temurin:17-jdk as stage2
WORKDIR /app
COPY --from=stage1 /app/build/libs/*.jar /app/build/libs/ordersystem.jar
# ENTRYPOINT [ "java","-jar","build/libs/ordersystem-0.0.1-SNAPSHOT.jar" ]
ENTRYPOINT [ "java","-jar","/app/build/libs/ordersystem.jar" ]
# 도커이미지 빌드
# docker build -t yunieee/myordersystem:latest .

# 도커컨테이너 실행시점에 컨테이너 밖 localhost로 환경변수 수정하여 주입
# docker run --name myspring -d -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/ordersystem?useSSL=true -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=test1234 -e SPRING_REDIS_HOST=host.docker.internal yunieee/myordersystem:latest

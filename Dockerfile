FROM registry.cn-hangzhou.aliyuncs.com/noyi/open-jdk-8:1.0.0
ENV project="notion-text-snippets"

WORKDIR /docker
COPY ./target/${project}*.jar ./app.jar
EXPOSE 65000
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-Duser.timezone=GMT+08","-jar","./app.jar","-c"]
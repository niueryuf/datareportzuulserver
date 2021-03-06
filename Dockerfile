FROM arm64v8/openjdk

#构建参数
ARG JAR_FILE
ARG WORK_PATH="/opt/mmcflying"
# 环境变量
ENV JAVA_OPTS="" \
    JAR_FILE=${JAR_FILE}

COPY target/$JAR_FILE $WORK_PATH/

WORKDIR $WORK_PATH

ENTRYPOINT exec java $JAVA_OPTS -jar $JAR_FILE

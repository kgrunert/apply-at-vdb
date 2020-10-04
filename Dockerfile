FROM oracle/graalvm-ce AS builder
WORKDIR /app/vdb
RUN gu install native-image
RUN gu install native-image
RUN curl https://bintray.com/sbt/rpm/rpm > bintray-sbt-rpm.repo \
	&& mv bintray-sbt-rpm.repo /etc/yum.repos.d/ \
	&& yum install -y sbt
COPY . /app/vdb
WORKDIR /app/vdb
RUN sbt "graalvm-native-image:packageBin"

FROM oraclelinux:7-slim
COPY --from=builder /app/vdb/target/graalvm-native-image/apply-at-vdb ./app/
CMD ./app/apply-at-vdb

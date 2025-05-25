# syntax=docker/dockerfile:1.7

# ---------- Build Stage ----------
ARG MAVEN_VERSION=3.9.9
ARG JDK_VERSION=21

FROM --platform=$BUILDPLATFORM maven:${MAVEN_VERSION}-eclipse-temurin-${JDK_VERSION} AS builder
LABEL stage=builder

WORKDIR /workspace

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2/repository \
      mvn dependency:go-offline -B

COPY src ./src

RUN --mount=type=cache,target=/root/.m2/repository \
      mvn -T1C -B clean package -DskipTests

# ---------- Healthcheck Stage ----------
FROM busybox:1.36.1-glibc AS probe

# ---------- Runtime Stage ----------
FROM gcr.io/distroless/java21-debian12:nonroot AS runtime
LABEL stage=runtime

WORKDIR /app

COPY --from=builder /workspace/target/bank-account-transfers-*.jar ./app.jar
COPY --from=probe /bin/wget /bin/wget

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT [ "java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=80", "-jar", "/app/app.jar" ]
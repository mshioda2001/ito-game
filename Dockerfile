# ビルドステージ
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 実行ステージ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ito-game-1.0.0.jar app.jar

# ポートを環境変数から取得（Renderが自動設定）
ENV PORT=8080
EXPOSE ${PORT}

# アプリケーション起動
ENTRYPOINT ["java", "-jar", "app.jar"]

# Made with Bob

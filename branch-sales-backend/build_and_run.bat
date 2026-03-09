call mvnw.cmd clean package -DskipTests > build.log 2>&1
java -jar target\branch-sales-backend-0.0.1-SNAPSHOT.jar > run.log 2>&1

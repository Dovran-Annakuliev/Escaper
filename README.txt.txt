mvn -f ChaseLogic clean install && mvn -f Game clean package

java -jar Game/target/Game.jar --enemiesCount=10 --wallsCount=10 --size=30 --profile=production

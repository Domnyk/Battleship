## Battleship

### Description
This is Battleship game. Two users connect to one server. Communication is based on sockets. GUI made with JavaFX. 


### Code organization
1. Client - client code
2. Server - server code
3. Shared - shared part of code, data model and communication protocol

Project was created as gradle multi-project build
  
### How to run

#### Client
    ./gradlew buildClientJar
    java -jar  ./Client/build/jfx/app/BattleshipClient.jar

#### Server
    ./gradlew buildServerJar
    java -jar  ./Server/build/jfx/app/BattleshipServer.jar

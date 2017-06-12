## Battleship

### Opis
Gra w statki. Dwóch użytkowników i jeden serwer. Komunikacja opartach na socketach. GUI zrealizowane przy pomocy JavaFX. 


### Organizacja kodu
1. Client - kod klienta
2. Server - kod serwera
3. Shared - współdzielona część kodu, zarówno model jak i protokół komunikacji

Projekt został zorganizowany przy użyciu Gradle'a jako "multi-project build"
  
### Uruchomienie programu

#### Klient
    ./gradlew buildClientJar
    java -jar  ./Client/build/jfx/app/BattleshipClient.jar

#### Serwer
    ./gradlew buildServerJar
    java -jar  ./Server/build/jfx/app/BattleshipServer.jar
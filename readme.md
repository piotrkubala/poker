# Poker dobierany pięciokartowy - aplikacja klient - serwer

Zasady zgodne z zasadami pokera pięciokartowego: bez all in, z obsługą showdown w dowolnym momencie betowania

Możliwość obstawiania, zaimplementowano mechanikę small i big blind

Obsługa do 5 graczy

Możliwość rozpoczęcia kolejnych rund gry

Możliwość rozpoczęcia nowej gry

# Sposób uruchomienia:
## Klient:
```java -jar poker-client-1.0-SNAPSHOT-jar-with-dependencies.jar <server address> <port>```

## Serwer:
```java -jar poker-server-1.0-SNAPSHOT-jar-with-dependencies.jar <number of players> <address> <port> <money per player>```

# Możliwe komunikaty klienta:
help - pokazuje dostępne komendy

username - ustawia nazwę użytkownika, konieczne przed rozpoczęciem gry

exit - opuszcza grę dla wszystkich użytkowników

showdown - pokazuje wszystkich graczy i ich karty

raise <amount> - podbija stawkę do podanej kwoty

fold - pasuje

change <card number 1> [<card number 2>] ... [<card number 5>] - zmienia karty

nextround - umożliwia rozpoczęcie kolejnej tury rozgrywki, następnie trzeba pokazać gotowość przy użyciu komendy start

show - pokazuje karty i inne potrzebne informacje

start - rozpoczyna rozgrywkę

Każdy komunikat może być wysłany w dowolnym momencie gry, w przypadku nieodpowiedniego momentu gry zostanie wysłana informacja o błędzie

# Możliwe komunikaty serwera:
## Klient:
```
raise 20
```
## Serwer:
```
You have raised the bet to 20
jan has raised the bet to 20
The first round of betting has ended
You can now change your cards
```

## Klient:
```
change 1 2 3
```
## Serwer:
```
You have raised the bet to 20
jan has raised the bet to 20
The first round of betting has ended
You can now change your cards
```

## Klient
```
show
```
## Serwer
```
Player: jan
Current game state: Cards changing
(0) Ace of Clubs
(1) Ace of Hearts
(2) Two of Spades
(3) Seven of Spades
(4) Nine of Hearts
Small Blind: piotr has 80$ left, current bet: 20$
Big Blind:   jan has 80$ left, current bet: 20$
```
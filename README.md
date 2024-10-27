# Pacman
## How to Run
There are two steps to run the game.
1. Build the game using:
```
gradle build
```
This may take some time if this is the first time the game is being built.

2. Run the game using:
```
gradle run
```

## Design Patterns

### Observer Pattern
- Subject
  - Subject
- Observer
  - Observer
- ConcreteSubject
  - GameModel
- Concrete Observer
  - GameOverView
  - LifeView
  - ReadyView
  - ScoreView
  - YouWinView

### Factory Pattern
- Product: 
  - Ghost
  - Controllable
  - Collectable
  - StaticEntity
- ConcreteProduct:
  - GhostImpl
  - Pacman
  - Pellet
  - Wall
- Factory:
  - ConcreteEntityFactory
- ConcreteFactory:
  - GhostFactory
  - PacmanFactory
  - PelletFactory
  - WallFactory

### Command Pattern
- Command
  - MoveCommand
- ConcreteCommand
  - MoveDownCommand
  - MoveLeftCommand
  - MoveRightCommand
  - MoveUpCommand
- Client
  - KeyBoardInputHandler
- Invoker
  - MovementInvoker
- Receiver
  - GameEngine

This project comes from University of Sydney 2024 S2 SOFT2201.

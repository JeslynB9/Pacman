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

## Implemented Design Patterns

### Strategy
- Strategy
  - ChaseMovementStrategy
- ConcreteStrategy
  - BlinkyChaseStrategy
  - ClydeChaseStrategy
  - InkyChaseStratgy
  - PinkyChaseStratgy
- Context
  - GhostImpl

### State
- Context
  - GhostImpl
- State
  - GhostModeState
- ConcreteStates
  - ScatterMode
  - ChaseMode
  - FrightenedMode

### Decorator
- Component
  - Controllable
- ConcreteComponent
  - Pacman
- Decorator
  - PacmanDecorator
- ComcreteDecorator
  - PoweredPacmanDecorator

This project comes from University of Sydney 2024 S2 SOFT2201.

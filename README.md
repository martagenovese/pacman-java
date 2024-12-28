# Pacman Java

This project is a Java implementation of the classic Pacman game. It includes the core game mechanics, graphical user interface, and sound effects to provide an engaging gaming experience.

Here is a description of the game: [Pacman](./src/other/infos.md)
## Features

- **Classic Pacman Gameplay**: Navigate Pacman through a maze, eat dots, and avoid ghosts.
- **Ghost AI**: Ghosts have different behaviors and can become scared when Pacman eats a super food.
- **Power-ups**: Includes dots, super food, and fruits that provide various effects.
- **Sound Effects**: Plays sound effects for different game events.
- **Graphical Interface**: Uses Java Swing for the graphical user interface.

## Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/martagenovese/pacman-java.git
    cd pacman-java
    ```

2. **Build the project**:
   Open the project in IntelliJ IDEA and build it using the provided build configurations.

## Usage

   Execute the main class to start the game. You can control Pacman using the arrow keys.

    - **Arrow Keys**: Move Pacman in the respective direction.

## Code Structure

- `src/mcv/EventManager.java`: Handles game events, key inputs, and updates the game state.
- `src/mcv/Model.java`: Contains the game logic and data models.
- `src/mcv/Table.java`: Manages the graphical user interface and rendering.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

## Acknowledgements

- Inspired by the original Pacman game.
- Sound effects and images are sourced from various free resources (my friends' voices and online memes).
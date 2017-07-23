Retro Reversi
======
**Retro Reversi** is an Android app of the board game Reversi (also known as Othello) that features online matchmaking and a retro look. Made with [LibGDX](https://libgdx.badlogicgames.com/).

#### Screenshots
<img width="250px" alt='Main Menu' src='https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/screenshots/main-menu.png?raw=true'/><img width="250px" alt='Game Screen' src='https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/screenshots/in-game.png?raw=true'/>

## Compatibility
#### Works on
* Android 4.0.3 and up

#### Does not work on
* Devices it doesn't work with

## Contributors
### Artwork
* Huge shout-out to [Kenney](https://kenney.nl/assets) for most of the used artwork
* Achievements' icons from [Flaticon](https://www.flaticon.com/)

### Developers
* [**António Almeida**](https://github.com/antonioalmeida)
* [**Cyril Damas**](https://github.com/cyrilico)

### Third party libraries
* [LibGDX](https://libgdx.badlogicgames.com/)
* [Play Games Services](https://developers.google.com/games/services/) to cover Achievements and online Mulltiplayer

## Developement
### Design Patterns
* **Strategy** - An AI's moves are chosen through an algorithm. Different AI difficulties have different algorithms for the way it chooses a move
* **Model-View-Presenter** - To separate the components' representation from its logic and relations and facilitate unit testing
* **Memento** - Implement 'Undo last play' functionality (only on local gameplay)

### Relevant Design Decisions
 - Choosing MVP over MVC as the main architectural design pattern turned out to be very useful, given the 'button-based' interface for both in-game and menu controls
 - Having the game logic implemented seperately from the rest of the application allowed to easily write tests to check its functionalities
 - The early on research and structural design regading viewports, layouts and other libGDX related visual elemements allowed us to properly make a responsive app that works across multiple devices
 - The well-design separation of concerns regarding the main components made the AI integration alongside the user controls seamless
  - The same applied to the online multiplayer integration, however the Play Games API integration was a lot of work
 - Having each visual board position handle its logic allowed us to gradually implement additional visual functionalities, such as animations and placement suggestions, while leaving the game logic intact.

## How-to use this code
* see [INSTRUCTIONS](https://github.com/username/appname/blob/master/INSTRUCTIONS.md) file

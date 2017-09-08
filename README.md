Retro Reversi
<img src='https://bettercodehub.com/edge/badge/cyrilico/LPOO1617_T1G7?branch=master&token=628dabbd02be15b31f0908f603e84e21d00e6a1a'>
======
**Retro Reversi** is an Android app of the board game Reversi (also known as Othello) that features online matchmaking and a retro look. Made with [LibGDX](https://libgdx.badlogicgames.com/).

<a href='https://play.google.com/store/apps/details?id=feup.lpoo.reversi&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="250px" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

**Note:** this is a view-only repository. For the original developement repository with more technical information, go [here](https://github.com/cyrilico/feup-lpoo).

#### Screenshots
<img width="250px" alt='Main Menu' src='https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/screenshots/main-menu.png?raw=true'/> <img width="250px" alt='Game Screen' src='https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/screenshots/in-game.png?raw=true'/> <img width="250px" alt='Multiplayer Screen' src='https://github.com/cyrilico/feup-lpoo/blob/finalRelease/screenshots/multiplayer.png?raw=true'/>

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

## Development
### Installing the development environment
* Clone this repository and open it with Android Studio. - more info [here](https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-(Eclipse,-Intellij-IDEA,-NetBeans)#setting-up-android-studio)
* Set your Run Configuration to work in android/assets folder.
* Run via emulator or USB.

**Note:** for Play Games Services usage you must sign your app according to your own linked app - more info [here](https://developers.google.com/games/services/console/enabling)

### Design Patterns
* **Strategy** - An AI's moves are chosen through an algorithm. Different AI difficulties have different algorithms for the way it chooses a move
* **Model-View-Presenter** - To separate the components' representation from its logic and relations and facilitate unit testing
* **Memento** - Implement 'Undo last play' functionality (only on local gameplay)

## UML Diagrams
### Model Diagram
 [![Model](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/model.png?raw=true)](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/model.png?raw=true)

### View & Presenter Diagram
 [![Model](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/view%20&%20presenter.png?raw=true)](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/view%20&%20presenter.png?raw=true)

### State Diagrams
#### App State Machine
[![AppStateMachine](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/app%20state.png?raw=true)](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/app%20state.png?raw=true)
#### Game Logic State Machine
 [![GameStateMachine](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/game%20logic.png?raw=true)](https://github.com/cyrilico/LPOO1617_T1G7/blob/finalRelease/uml/game%20logic.png?raw=true)

## Other Notes
### Relevant Design Decisions
 - Choosing MVP over MVC as the main architectural design pattern turned out to be very useful, given the 'button-based' interface for both in-game and menu controls
 - Having the game logic implemented seperately from the rest of the application allowed to easily write tests to check its functionalities
 - The early on research and structural design regading viewports, layouts and other libGDX related visual elemements allowed us to properly make a responsive app that works across multiple devices
 - The well-design separation of concerns regarding the main components made the AI integration alongside the user controls seamless
  - The same applied to the online multiplayer integration, however the Play Games API integration was a lot of work
 - Having each visual board position handle its logic allowed us to gradually implement additional visual functionalities, such as animations and placement suggestions, while leaving the game logic intact.

## License
* This code is licensed under MIT.

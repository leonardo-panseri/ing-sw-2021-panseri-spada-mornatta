# Master Of Renaissance

Final project for the course Software Engineering 2020/2021 at Polimi.

**Teacher:** Gianpaolo Cugola

## Project Status

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://via.placeholder.com//15/f03c15/f03c15)](#) |
| Offline Single Player | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| Editable Parameters & Game Editor | [![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#) |
| Disconnection resilience | [![RED](https://via.placeholder.com//15/f03c15/f03c15)](#) |

<!--
[![RED](https://via.placeholder.com//15/f03c15/f03c15)](#)
[![YELLOW](https://via.placeholder.com//15/ffdd00/ffdd00)](#)
[![GREEN](https://via.placeholder.com//15/44bb44/44bb44)](#)
-->

## Tests coverage

<img src="https://github.com/leonardo-panseri/ing-sw-2021-panseri-spada-mornatta/blob/main/github/coverage.png" />

# Getting started

To build and run this project you need to install Java 15 or later.

After cloning this repo you can:
- Build and run tests with `mvn package`
- Build docs with `mvn javadoc:javadoc`

## Starting the server

First you need to create a server instance to host the matches with:

`java -jar GC04-server.jar`

This will run a server on the default port(12345).

## Starting the client

Both GUI and CLI are supported and can be used to play the game.

If you want to run a new GUI client you can run:

`java -jar GC04.jar`

Otherwise, if you want to run a new CLI client you can use:

`java -jar GC04.jar -cli`

## Additional run parameters

To make use of the additional functionalities, you can run:

`java -jar GC04.jar -editor`
to open the Game Editor, where you can customize cards and so on.

Eventually, you can run:

`java -jar GC04.jar -noserver`
to play a single player match without connecting to a server.

## Team Members
* [Leonardo Panseri](https://github.com/leonardo-panseri)
* [Davide Mornatta](https://github.com/davidemornatta)
* [Edoardo Spada](https://github.com/EdoardoSpada)

## Software used
**MagicDraw** - UML diagrams

**Intellij IDEA Ultimate** - main IDE 

**Photoshop** - custom assets creation

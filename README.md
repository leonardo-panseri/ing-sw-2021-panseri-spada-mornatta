# Master Of Renaissance

Final project for the course Software Engineering 2020/2021 at Polimi.

**Teacher:** Gianpaolo Cugola  
**Grade:** 30/30 cum laude

## Project Status

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | 🟩 |
| Complete rules | 🟩 |
| Socket | 🟩 |
| GUI | 🟩 |
| CLI | 🟩 |
| Multiple games | 🟩 |
| Offline Single Player | 🟩 |
| Editable Parameters & Game Editor | 🟩 |

## Tests coverage

<img src="https://github.com/leonardo-panseri/ing-sw-2021-panseri-spada-mornatta/blob/main/github/coverage.png" />

# Getting started

To build and run this project you need to install Java 15 or later.

After cloning this repo you can find JavaDocs and JARs inside the folder "delivarables", along with UML schemas.

## Starting the server

First you need to create a server instance to host the matches with:

`java -jar GC04-server.jar`

This will run a server on the default port (12345).

## Starting the client

Both GUI and CLI are supported and can be used to play the game.

If you want to run a new GUI client you can run:

`java -jar GC04.jar`

Otherwise, if you want to run a new CLI client you can use:

`java -jar GC04.jar -cli`

***If you want to use the CLI and you are using Windows, make sure to use 
`java -Dfile.encoding="UTF-8" -jar GC04.jar -cli` to render colours properly.***

## Additional run parameters

To make use of the additional functionalities, you can run:

`java -jar GC04.jar -editor`
to open the Game Editor, where you can customize cards and so on.

Eventually, you can run:

`java -jar GC04.jar -noserver`
to play a single player match without connecting to a server.

## Test Cheat
For examination and testing purposes, if a player is named "test" or with a nick that starts with it, it will gain access to 99 resources of each type.

## Team Members
* [Leonardo Panseri](https://github.com/leonardo-panseri)
* [Davide Mornatta](https://github.com/davidemornatta)
* [Edoardo Spada](https://github.com/EdoardoSpada)

## Software used
**MagicDraw** - UML diagrams

**Intellij IDEA Ultimate** - main IDE 

**Photoshop** - custom assets creation

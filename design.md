# Settlers Design

## Server and clients

The server `Settlers` serves HTTP requests from the web-based client. Clients connect to a game instance through WebSockets, on which clients send Actions to the server to communicate player moves and the server broadcasts state-changes and player actions.

## Components

### Lobby

Displays games available for clients to join. These include games in the setup phase and games the player is a part of.

### Game

A single instance of a game of Settlers. Can be an instance of the base game or the extended game for 5-6 players. Games consume Actions to compute state-changes before broadcasting them to players.

### Actions

Messages that encapsulate player moves. These are minimally composed of an UUID, an action type and a player origin. 

#### Examples:

* Offer trade: target player, offer, demand
* Accept trade: offer trade Action ID
* Build road: road coordinates
* Roll: *none\**

##### *the actual dice roll takes place on the server

## Game state

### Players

Player state includes all players in the game, each player's resource cards, and each players development cards. Players are defined by a UUID and can set nicknames.

### Turns

A turn is defined by the current player and the turn-phase. The three main phases are the __roll__, __trade__, and __build__ phases. There is also a special __setup__ phase before the game begins with no current player.

### Board

The board consists of resource tiles, roads, and settlements/cities. Each of these pieces use an [axial coordinate system](http://www.redblobgames.com/grids/hexagons/#coordinates-axial) for placement.

## Relational database tables

*Game (__gameId__, expansion, name, owner)*

*Player (__gameId__, __playerId__, color)*

*Nickname (__playerId__, nickname)*

*Tile (__gameId__, __column__, __row__, resource, token)*

*Road (__gameId__, __column__, __row__, __direction__, playerId)*

*Settlement (__gameId__, __column__, __row__, __direction__, playerId, isUpgraded)*

# League Tracker Discord Bot
League Tracker Discord Bot (LTDB) is a Discord bot to display stats for your League of Legends Profile.

## Commands

### /Ping
Just to test if the bot works. (perhaps there's something more?)

### /stats [region] | [name] | [tag]
Displays stats of the submitted user.

### /freechamps
Shows the current Champion rotation.

### /leaderboard [region]
Displays the current top 10 highest ranked players in your region.

## Requirements
- Java 21
- Maven 3.9+
- A Discord bot token (DO NOT SHARE WITH ANYONE!!!)
- A Riot Games API key (DO NOT SHARE WITH ANYONE!!!)
- A Start.gg API token (DO NOT SHARE WITH ANYONE!!!)

## Setup
1. Clone the repository

```bash
git clone https://github.com/Hoshiguru/League-Tracker-Discord-Bot.git
cd League-Tracker-Discord-Bot
```

2. Open the properties file:

src/main/resources/application.properties

3. Add your keys:

```properties
token.discord=YOUR_DISCORD_BOT_TOKEN
token.riotGames=YOUR_RIOT_API_KEY
token.startGG=YOUR_START_GG_API_TOKEN
```

4. Build the project:

```bash
mvn clean package
```

5. Run the bot:

```bash
java -jar target/tracker-1.0-SNAPSHOT.jar
```

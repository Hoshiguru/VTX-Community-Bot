package org.example.tracker;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Startup {

        public static void main(String[] args) {
                try {
                        InputStream inputStream = Startup.class.getClassLoader()
                                        .getResourceAsStream("application.properties");
                        Properties properties = new Properties();
                        properties.load(inputStream);

                        System.out.println(properties.getProperty("token.riotGames"));
                        JDA jda = JDABuilder.createDefault(properties.getProperty("token.discord"))
                                        .addEventListeners(new CommandsListener(properties))
                                        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                                        .build();
                        jda.updateCommands().addCommands(
                                        Commands.slash("ping", "pong!"),
                                        Commands.slash("stats", "show League of Legends stats")
                                                        .addOptions(
                                                                        new OptionData(OptionType.STRING, "region",
                                                                                        "Your region", true)
                                                                                        .addChoice("BR1", "br1")
                                                                                        .addChoice("EUN1", "eun1")
                                                                                        .addChoice("EUW1", "euw1")
                                                                                        .addChoice("JP1", "jp1")
                                                                                        .addChoice("KR", "kr")
                                                                                        .addChoice("LA1", "la1")
                                                                                        .addChoice("LA2", "la2")
                                                                                        .addChoice("NA1", "na1")
                                                                                        .addChoice("OC1", "oc1")
                                                                                        .addChoice("PH2", "ph2")
                                                                                        .addChoice("RU", "ru")
                                                                                        .addChoice("SG2", "sg2")
                                                                                        .addChoice("TH2", "th2")
                                                                                        .addChoice("TR1", "tr1")
                                                                                        .addChoice("TW2", "tw2")
                                                                                        .addChoice("VN2", "vn2"),
                                                                        new OptionData(OptionType.STRING, "name",
                                                                                        "Your summoner name", true),
                                                                        new OptionData(OptionType.STRING, "tag",
                                                                                        "Your Riot Game-Tag", true)
                                                                                        .setRequiredLength(3, 5)),
                                        Commands.slash("freechamps", "current Champion rotation"),
                                        Commands.slash("leaderboard", "today's top player")
                                                        .addOptions(
                                                                        new OptionData(OptionType.STRING, "region",
                                                                                        "Your region", true)
                                                                                        .addChoice("BR1", "br1")
                                                                                        .addChoice("EUN1", "eun1")
                                                                                        .addChoice("EUW1", "euw1")
                                                                                        .addChoice("JP1", "jp1")
                                                                                        .addChoice("KR", "kr")
                                                                                        .addChoice("LA1", "la1")
                                                                                        .addChoice("LA2", "la2")
                                                                                        .addChoice("NA1", "na1")
                                                                                        .addChoice("OC1", "oc1")
                                                                                        .addChoice("PH2", "ph2")
                                                                                        .addChoice("RU", "ru")
                                                                                        .addChoice("SG2", "sg2")
                                                                                        .addChoice("TH2", "th2")
                                                                                        .addChoice("TR1", "tr1")
                                                                                        .addChoice("TW2", "tw2")
                                                                                        .addChoice("VN2", "vn2")),
                                        Commands.slash("frames", "show frame data for a character")
                                                        .addOptions(
                                                                        new OptionData(OptionType.STRING, "character",
                                                                                        "Choose a character", true)
                                                                                        .setAutoComplete(true),
                                                                        new OptionData(OptionType.STRING, "move",
                                                                                        "example: df1+2", true)))
                                        .queue();
                        jda.awaitReady();
                } catch (IOException e) {
                        System.out.println("Failed to load property!");
                } catch (InterruptedException e) {
                        System.out.println("Connection to bot Failed!");
                }
        }
}
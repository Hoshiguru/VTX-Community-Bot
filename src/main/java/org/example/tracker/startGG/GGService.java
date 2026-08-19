package org.example.tracker.startGG;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.example.tracker.startGG.models.TournamentModel;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GGService {
    private final GGApiConnection ggApiConnection;

    public GGService(Properties properties) {
        ggApiConnection = new GGApiConnection(properties);
    }

    public String tournamentsHandler(SlashCommandInteractionEvent event) {
        String location = event.getOption("location").getAsString();
        String game = event.getOption("game").getAsString();
        return tournamentsToString(game, location);
    }

    private String tournamentsToString(String game, String location) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            List<TournamentModel> tournaments = ggApiConnection.getTournament(game, location);
            
            stringBuilder.append("# Upcoming tournaments for __").append(game).append("__").append(" in __").append(location).append("__\n");
            for(TournamentModel tournament: tournaments){
                stringBuilder.append("> ## ").append(tournament.getName()).append("\n")
                    .append("> Start at: ").append(tournament.getStartAt()).append("\n")
                    .append("> City: ").append(tournament.getCity()).append("\n")
                    .append("> More infos/Register here: https://www.start.gg").append(tournament.getUrl()).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
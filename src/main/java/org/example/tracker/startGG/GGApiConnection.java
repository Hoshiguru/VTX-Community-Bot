package org.example.tracker.startGG;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.example.tracker.startGG.models.TournamentModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GGApiConnection {

    final private Properties properties;

    public GGApiConnection(Properties properties) {
        this.properties = properties;
    }

    public HttpResponse<String> apiConnection(String query, JsonObject variables) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("query", query);
        body.add("variables", variables);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.start.gg/gql/alpha"))
                .header("Authorization", "Bearer " + properties.getProperty("token.startGG"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();
        System.out.println("---------\n\n api connection \n\n---------");
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String queryBuilder(String game, String location) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("query TournamentsByCountry(")
                .append("$videogameId: ID!, ")
                .append("$cCode: String!, ")
                .append("$perPage: Int!) { ")
                .append("tournaments(query: { ")
                .append("perPage: $perPage, ")
                .append("sortBy: \"startAt asc\", ")
                .append("filter: { ")
                .append("upcoming: true, ")
                .append("videogameIds: [$videogameId], ")
                .append("countryCode: $cCode ")
                .append("} ")
                .append("}) { ")
                .append("nodes { ")
                .append("id ")
                .append("name ")
                .append("countryCode ")
                .append("startAt ")
                .append("url ")
                .append("owner { ")
                .append("player { ")
                .append("gamerTag ")
                .append("} ")
                .append("} ")
                .append("} ")
                .append("} ")
                .append("}");
        return queryBuilder.toString();
    }

    public String getGameId(String game) {
        Gson gson = new Gson();
        String query = "query VideogameQuery {"
                + "videogames(query: { filter: { name: \"" + game + "\" }, perPage: 1 }) {"
                + "nodes {"
                + "id,"
                + "name,"
                + "displayName,"
                + "}"
                + "}"
                + "}";
        try {
            JsonObject variables = new JsonObject();
            HttpResponse<String> response = apiConnection(query, variables);
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            String gameId = jsonObject.getAsJsonObject("data")
                    .getAsJsonObject("videogames")
                    .getAsJsonArray("nodes")
                    .get(0)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString();
            return gameId;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TournamentModel> getTournament(String game, String location) throws IOException, InterruptedException {
        List<TournamentModel> tournaments = new ArrayList<>();
        Gson gson = new Gson();
        String query = queryBuilder(game, location);
        JsonObject variables = new JsonObject();
        variables.addProperty("videogameId", getGameId(game));
        variables.addProperty("cCode", location);
        variables.addProperty("perPage", 5);
        HttpResponse<String> response = apiConnection(query, variables);
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

        JsonArray tournamentJson = jsonObject.getAsJsonObject("data")
                .getAsJsonObject("tournaments")
                .getAsJsonArray("nodes");

        for (JsonElement element : tournamentJson) {
            JsonObject tournamentObj = element.getAsJsonObject();

            String ownerTag = tournamentObj
                    .getAsJsonObject("owner")
                    .getAsJsonObject("player")
                    .get("gamerTag")
                    .getAsString();
            tournamentObj.remove("owner");
            TournamentModel tournamentModel = gson.fromJson(tournamentObj, TournamentModel.class);
            tournamentModel.setOwner(ownerTag);
            tournaments.add(tournamentModel);
        }
        return tournaments;
    }
}

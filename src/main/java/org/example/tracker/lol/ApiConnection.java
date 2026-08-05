package org.example.tracker.lol;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import org.example.tracker.lol.models.ChampionModel;
import org.example.tracker.lol.models.MasteryModel;
import org.example.tracker.lol.models.RankModel;
import org.example.tracker.lol.models.RiotTag;
import org.example.tracker.lol.models.SummonerModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class ApiConnection {
    Properties properties;
    public ApiConnection(Properties properties) {
        this.properties = properties;
    }

    public HttpResponse<String> apiConnection(String url) throws IOException, InterruptedException {
        System.out.println("---------\n\n" + url + "\n\n---------");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", properties.getProperty("token.riotGames"))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public SummonerModel summonerByRiotTag(String summonerName, String tag, String region) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String region2 = "";
        if (summonerName.contains(" ") || tag.contains(" ")) {
            summonerName = summonerName.replaceAll(" ", "%20");
            tag = tag.replaceAll(" ", "%20");
        }
        region2 = switch (region) {
            case "br1", "la1", "la2", "na1" -> "americas";
            case "kr", "jp1", "oc1", "th2", "tw2", "vn2", "ph2" -> "asia";
            case "euw1", "eun1", "ru" -> "europe";
            default -> region2;
        };
        RiotTag riotTag = gson.fromJson(apiConnection("https://" + region2 + ".api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + summonerName + "/" + tag).body(), RiotTag.class);
        SummonerModel summonerModel = gson.fromJson(apiConnection("https://" + region + ".api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + riotTag.getPuuid()).body(), SummonerModel.class);
        summonerModel.setName(riotTag.getGameName());
        return summonerModel;
    }

    public RiotTag summonerById(String summonerId, String region) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String region2 = "";
        region2 = switch (region) {
            case "br1", "la1", "la2", "na1" -> "americas";
            case "kr", "jp1", "oc1", "th2", "tw2", "vn2", "ph2" -> "asia";
            case "euw1", "eun1", "ru" -> "europe";
            default -> region2;
        };
        //SummonerModel summonerModel = gson.fromJson(apiConnection("https://" + region + ".api.riotgames.com/lol/summoner/v4/summoners/" + summonerId).body(), SummonerModel.class);
        RiotTag riotTag = gson.fromJson(apiConnection("https://" + region2 + ".api.riotgames.com/riot/account/v1/accounts/by-puuid/" + summonerId).body(), RiotTag.class);
        //summonerModel.setName(riotTag.getGameName());
        //summonerModel.setTag(riotTag.getTagLine());
        return riotTag;
    }

    public List<RankModel> rankStats(String encryptedSummonerId, String region) throws IOException, InterruptedException {
        Gson gson = new Gson();
        JsonArray jsonArray = JsonParser.parseString(apiConnection("https://" + region + ".api.riotgames.com/lol/league/v4/entries/by-puuid/" + encryptedSummonerId).body()).getAsJsonArray();
        Type listType = new TypeToken<List<RankModel>>(){
        }.getType();
        return gson.fromJson(jsonArray, listType);
    }

    public List<RankModel> topChallenger(String region) throws IOException, InterruptedException {
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(apiConnection("https://" + region + ".api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5").body()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("entries").getAsJsonArray();
        Type listType = new TypeToken<List<RankModel>>(){
        }.getType();
        return gson.fromJson(jsonArray, listType);
    }

    public List<MasteryModel> masteryStats(String encryptedPUUID, String region) throws IOException, InterruptedException {
        Gson gson = new Gson();
        JsonArray jsonArray = JsonParser.parseString(apiConnection("https://" + region + ".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/" + encryptedPUUID + "/top").body()).getAsJsonArray();
        Type listType = new TypeToken<List<MasteryModel>>() {
        }.getType();
        return gson.fromJson(jsonArray, listType);
    }

    public List<Long> freeChampions() throws IOException, InterruptedException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Long>>() {}.getType();
        JsonObject jsonObject = JsonParser.parseString(apiConnection("https://euw1.api.riotgames.com/lol/platform/v3/champion-rotations").body()).getAsJsonObject();
        System.out.println(jsonObject);
        return gson.fromJson(jsonObject.get("sr"), listType);
    }

    public String championSetter(long championId) throws IOException {
        InputStream inputStream = new FileInputStream("src/main/resources/champion.json");
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ChampionModel>>() {
        }.getType();
        List<ChampionModel> champions = gson.fromJson(jsonArray, listType);

        for (ChampionModel championModel : champions) {
            if (championModel.getKey() == championId) {
                return championModel.getName();
            }
        }
        return "unknown";
    }
}

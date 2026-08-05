package org.example.tracker.lol;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.example.tracker.lol.models.MasteryModel;
import org.example.tracker.lol.models.RankModel;
import org.example.tracker.lol.models.RiotTag;
import org.example.tracker.lol.models.SummonerModel;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SummonerService {
    private final ApiConnection apiConnection;

    public SummonerService(Properties properties) {
        apiConnection = new ApiConnection(properties);
    }

    public String summonerStatsHandler(SlashCommandInteractionEvent event) {
        String summonerName = event.getOption("name").getAsString();
        String region = event.getOption("region").getAsString();
        String tag;
        if (event.getOption("tag") == null) {
            tag = "";
        } else {
            tag = event.getOption("tag").getAsString();
        }

        return summonerToString(summonerName, tag, region);
    }

    public String summonerToString(String summonerName, String tag, String region) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            SummonerModel summoner = apiConnection.summonerByRiotTag(summonerName, tag, region);
            if (summoner.getName() != null) {
                stringBuilder.append("# Stats for: __").append(summoner.getName()).append("#").append(tag).append("__\n")
                        .append("## Level ").append(summoner.getSummonerLevel()).append("\n")
                        .append(rankToString(summoner.getPuuid(), region))
                        .append(masteryToString(summoner.getPuuid(), region))
                        .append(moreInfosString(region, summonerName, tag));
                return stringBuilder.toString();
            } else {
                return "Summoner not found";
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String rankToString(String encryptedSummonerId, String region) throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        List<RankModel> rank = apiConnection.rankStats(encryptedSummonerId, region);
        if (!rank.isEmpty()) {
            stringBuilder.append("## Rank stats\n");
            for (RankModel rankModel : rank) {
                if (!rankModel.getTier().isEmpty()) {
                    stringBuilder.append("> ## ").append(rankModel.getQueueType()).append(" ").append(rankModel.getTier()).append("\n")
                            .append("> ### ").append(rankModel.getTierRaw()).append(" ").append(rankModel.getRank()).append("\n")
                            .append("> ### ").append(rankModel.getLeaguePoints()).append(" LP\n")
                            .append("> Wins this season: **").append(rankModel.getWins()).append("**\n")
                            .append("> Losses this season: **").append(rankModel.getLosses()).append("**\n")
                            .append("> Win rate: **").append(rankModel.getWinRate(rankModel.getWins(), rankModel.getLosses())).append("%**\n\n");
                }
            }
        } else {
            stringBuilder.append("> This Summoner hasn't played any Rank this season\n");
        }
        return stringBuilder.toString();
    }

    public String masteryToString(String encryptedPUUID, String region) throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        List<MasteryModel> mastery = apiConnection.masteryStats(encryptedPUUID, region);
        stringBuilder.append("## Top 3 Champions\n");
        if (!mastery.isEmpty()) {
            for (int i = 0; i < mastery.size() && i < 3; i++) {
                stringBuilder.append("> ## __").append(apiConnection.championSetter(mastery.get(i).getChampionId())).append("__ \n")
                        .append("> - Mastery level: **").append(mastery.get(i).getChampionLevel()).append("**\n")
                        .append("> - Mastery points: **").append(mastery.get(i).getChampionPoints()).append("**\n");
            }
        } else {
            stringBuilder.append("> This Summoner has no Mastery stats");
        }
        return stringBuilder.toString();
    }

    public String moreInfosString(String region, String name, String tag){
        StringBuilder stringbuilder = new StringBuilder();
        if(region.contains("1") || region.contains("2")){
            region = region.replaceAll("1", "").replaceAll("2", "").toLowerCase();
        }
        if(name.contains(" ") || tag.contains(" ")){
            name = name.replace(" ", "%20");
            tag = tag.replaceAll(" ", "%20");
        }
        stringbuilder.append("## More infos here:\n")
                .append("> https://www.op.gg/summoners/").append(region).append("/").append(name).append("-").append(tag);
        return stringbuilder.toString();
    }

    public String championHandler(){
        try {
            List<Long> freeChampionIds = apiConnection.freeChampions();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("# Weekly F2P Champions\n");
            System.out.println(freeChampionIds);
            for (Long freeChampionId : freeChampionIds) {
                stringBuilder.append("> ").append(apiConnection.championSetter(freeChampionId)).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String leaderboard(SlashCommandInteractionEvent event) {
        try {
            String region = event.getOption("region").getAsString();
            StringBuilder builder = new StringBuilder();
            builder.append("# Daily Top Players in ").append(region).append("\n");
            List<RankModel> topPlayers = apiConnection.topChallenger(region);
            for (int i = 0; i < 10; i++) {
                RiotTag riotTag = apiConnection.summonerById(topPlayers.get(i).getPuuid(), region);
                builder.append("> #").append(i + 1).append(" ")
                        .append(riotTag.getGameName()).append("#").append(riotTag.getTagLine()).append("\n");
            }
            return builder.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


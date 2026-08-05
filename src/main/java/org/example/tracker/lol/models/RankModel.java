package org.example.tracker.lol.models;

public class RankModel {
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private String summonerName;
    private String puuid;

    public String getQueueType() {
        if (queueType.contains("RANKED_SOLO_5x5")) {
            queueType = "Ranked Solo/Duo";
        } else if(queueType.contains("RANKED_FLEX_SR")) {
            queueType = "Ranked Flex";
        }
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getTierRaw(){
        return tier;
    }
    public String getTier() {
        if(tier != null) {
            return switch (tier) {
                case "IRON" -> "<:iron:1257615276745429023>";
                case "BRONZE" -> "<:bronze:1257615288581619735>";
                case "SILVER" -> "<:silver:1257615323662647388>";
                case "GOLD" -> "<:gold:1257615349126266921>";
                case "PLATINUM" -> "<:platinum:1257615379438501951>";
                case "EMERALD" -> "<:emerald:1257615398195564587>";
                case "DIAMOND" -> "<:diamond:1257615422874849280>";
                case "MASTER" -> "<:master:1257615462745899061>";
                case "GRANDMASTER" -> "<:grandmaster:1257615485726621757>";
                case "CHALLENGER" -> "<:challenger:1257615499219439668>";
                default -> "<:unranked:1257615257266819155>";
            };
        }
        return "";
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWinRate(int wins, int losses) {
        System.out.println("wins: " + wins);
        System.out.println("losses: " + losses);
        int totalGames = wins + losses;
        System.out.println("total matches: " + totalGames);
        System.out.println(100 * wins / totalGames);
        return 100 * wins / totalGames;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    @Override
    public String toString() {
        return "RankModel{" +
                "queueType='" + queueType + '\'' +
                ", tier='" + tier + '\'' +
                ", rank='" + rank + '\'' +
                ", leaguePoints=" + leaguePoints +
                ", wins=" + wins +
                ", losses=" + losses +
                ", summonerName='" + summonerName + '\'' +
                ", puuid='" + puuid + '\'' +
                '}';
    }
}

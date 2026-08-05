package org.example.tracker;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.example.tracker.lol.SummonerService;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class CommandsListener extends ListenerAdapter {

    SummonerService summonerService;

    public CommandsListener(Properties properties) {
        summonerService = new SummonerService(properties);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                event.reply("pong!").addActionRow(
                        Button.primary("hollow purple", Emoji.fromFormatted("<a:JJKGojoWaving:860283604004372490>")))
                        .queue();
                break;
            case "stats":
                event.deferReply().queue();
                event.getHook().sendMessage(summonerService.summonerStatsHandler(event)).queue();
                break;
            case "freechamps":
                event.reply(summonerService.championHandler()).queue();
                break;
            case "leaderboard":
                event.deferReply().queue();
                String reply = summonerService.leaderboard(event);
                event.getHook().editOriginal(reply).queue();
                break;
            case "frames":
                break;
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("hollow purple")) {
            event.editMessage(":rightwards_pushing_hand_tone2::blue_circle:")
                    .delay(Duration.ofMillis(500))
                    .flatMap((it) -> it.editOriginal(
                            ":rightwards_pushing_hand_tone2::blue_circle::red_circle::leftwards_pushing_hand_tone2:"))
                    .delay(Duration.ofMillis(500))
                    .flatMap((it) -> it.editMessage(":pray_tone2:"))
                    .delay(Duration.ofMillis(500))
                    .flatMap((it) -> it.editMessage(":pinched_fingers_tone2::purple_circle:"))
                    .delay(Duration.ofMillis(500))
                    .flatMap((it) -> it
                            .editMessage(":palm_up_hand_tone2:                                   :purple_circle:"))
                    .queue();
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(
            CommandAutoCompleteInteractionEvent event) {
        if (!event.getName().equals("frames") || !event.getFocusedOption().getName().equals("character")) {
            return;
        }

        String input = event.getFocusedOption().getValue().toLowerCase();

        List<Command.Choice> suggestions = CHARACTERS.stream()
                .filter(character -> character.getName().toLowerCase().contains(input))
                .limit(25)
                .toList();

        event.replyChoices(suggestions).queue();
    }

    private static final List<Command.Choice> CHARACTERS = List.of(
            new Command.Choice("Alisa", "alisa"),
            new Command.Choice("Anna", "anna"),
            new Command.Choice("Armor King", "armor-king"),
            new Command.Choice("Asuka", "asuka"),
            new Command.Choice("Azucena", "azucena"),
            new Command.Choice("Bryan", "bryan"),
            new Command.Choice("Claudio", "claudio"),
            new Command.Choice("Clive", "clive"),
            new Command.Choice("Devil Jin", "devil-jin"),
            new Command.Choice("Dragunov", "dragunov"),
            new Command.Choice("Eddy", "eddy"),
            new Command.Choice("Fahkumram", "fahkumram"),
            new Command.Choice("Feng", "feng"),
            new Command.Choice("Heihachi", "heihachi"),
            new Command.Choice("Hwoarang", "hwoarang"),
            new Command.Choice("Jack 8", "jack-8"),
            new Command.Choice("Jin", "jin"),
            new Command.Choice("Jun", "jun"),
            new Command.Choice("Kazuya", "kazuya"),
            new Command.Choice("King", "king"),
            new Command.Choice("Kuma", "kuma"),
            new Command.Choice("Kunimitsu", "kunimitsu"),
            new Command.Choice("Lars", "lars"),
            new Command.Choice("Law", "law"),
            new Command.Choice("Lee", "lee"),
            new Command.Choice("Leo", "leo"),
            new Command.Choice("Leroy", "leroy"),
            new Command.Choice("Lidia", "lidia"),
            new Command.Choice("Lili", "lili"),
            new Command.Choice("Miary Zo", "miary-zo"),
            new Command.Choice("Nina", "nina"),
            new Command.Choice("Panda", "panda"),
            new Command.Choice("Paul", "paul"),
            new Command.Choice("Raven", "raven"),
            new Command.Choice("Reina", "reina"),
            new Command.Choice("Shaheen", "shaheen"),
            new Command.Choice("Steve", "steve"),
            new Command.Choice("Victor", "victor"),
            new Command.Choice("Xiaoyu", "xiaoyu"),
            new Command.Choice("Yoshimitsu", "yoshimitsu"),
            new Command.Choice("Zafina", "zafina"));
}

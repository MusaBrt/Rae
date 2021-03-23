package me.koply.rae.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.integration.impl.jda.JDACommand;
import me.koply.kcommando.internal.annotations.Commando;
import me.koply.rae.music.GuildMusicManager;
import me.koply.rae.music.PlayerManager;
import me.koply.rae.util.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Commando(name = "Sırayı Gör",
        aliases = {"queue", "q", "list"},
        description = "Müzik sırasını görmeye yarar",
        guildOnly = true)
public class QueueCommand extends JDACommand {

    public QueueCommand() {
        getInfo().setOnFalseCallback((e) -> e.getMessage().addReaction("🤔").queue());
    }

    @Override
    public boolean handle(MessageReceivedEvent e) {
        GuildVoiceState memVoiceState = e.getMember().getVoiceState();
        GuildVoiceState selfVoiceState = e.getGuild().getSelfMember().getVoiceState();

        if (memVoiceState.inVoiceChannel() && selfVoiceState.inVoiceChannel()) {
            if (memVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                final GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(e.getGuild());
                if (manager.scheduler.queue.size() == 0) {
                    e.getMessage().addReaction("🥴").queue();
                } else {
                    StringBuilder sb = new StringBuilder("```\n");
                    for (AudioTrack track : manager.scheduler.queue) {
                        sb.append(track.getInfo().title).append("\n");
                    }
                    sb.append("```");
                    e.getChannel().sendMessage(new EmbedBuilder()
                            .setColor(Utilities.randomColor())
                            .setFooter(e.getJDA().getSelfUser().getName() + "by koply", e.getJDA().getSelfUser().getAvatarUrl())
                            .setDescription(sb.toString())
                            .setTitle("Müzik Sırası 🎶")
                            .build()).queue();
                }
                return true;
            }
        }

        return false;
    }
}
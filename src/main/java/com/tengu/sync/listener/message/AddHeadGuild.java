package com.tengu.sync.listener.message;

import com.tengu.sync.Constants;
import com.tengu.sync.bean.GuildResponse;
import com.tengu.sync.bean.TenguGuild;
import com.tengu.sync.event.EventListener;
import com.tengu.sync.service.GuildService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.tengu.sync.utils.GuildConverter.convert;

@Service
public class AddHeadGuild extends BaseMessageListener implements EventListener<MessageCreateEvent> {

    @Autowired
    private GuildService guildService;


    @Override
    public String getCommand() {
        return Constants.MessageCommand.ADD_HEAD_GUILD;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        if (!isCommand(event)) {
            return Mono.just(event).then();
        }

        Guild guild = getCurrentGuild(event);
        GuildResponse response = guildService.save(convert(guild, new TenguGuild(true)));

        if (response.isError()) {
            return Mono.just(getChannel(event))
                    .flatMap(channel -> channel.createMessage(response.getErrorMessage()))
                    .then();
        }

        return Mono.just(getChannel(event))
                .flatMap(channel -> channel.createMessage(response.getSuccessMessage()))
                .then();
    }
}

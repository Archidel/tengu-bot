package com.tengu.sync.listener.message;

import com.tengu.sync.Constants;
import com.tengu.sync.event.EventListener;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class InfoGuild extends BaseMessageListener implements EventListener<MessageCreateEvent> {
    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        if (isCommand(event)) {
            Guild guild = getCurrentGuild(event);
            String responseMessage = String.format("Server info: \n  Name: %s \n  ID : %s", guild.getName(), guild.getId().asString());
            getChannel(event).createMessage(responseMessage).subscribe();
        }
        return Mono.just(event).then();
    }

    @Override
    public String getCommand() {
        return Constants.MessageCommand.INFO_GUILD;
    }
}
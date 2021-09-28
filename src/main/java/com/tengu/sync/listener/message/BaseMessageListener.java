package com.tengu.sync.listener.message;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;

public abstract class BaseMessageListener {

    public abstract String getCommand();

    protected boolean isCommand(MessageCreateEvent event) {
        return getMessageContent(event).toLowerCase().contains(getCommand().toLowerCase());
    }

    protected String removeCommandFromMessage(String message) {
        return message.replace(getCommand(), "").trim();
    }

    protected Guild getCurrentGuild(MessageCreateEvent event) {
        return event.getGuild().block();
    }

    protected Member getMember(MessageCreateEvent event) {
        return event.getMember().get();
    }

    protected String getMessageContent(MessageCreateEvent event) {
        return event.getMessage().getContent();
    }

    protected MessageChannel getChannel(MessageCreateEvent event) {
        return event.getMessage().getChannel().block();
    }

}

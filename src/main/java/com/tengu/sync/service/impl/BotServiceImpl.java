package com.tengu.sync.service.impl;

import com.tengu.sync.service.BotService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.UserUpdateEvent;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.ExtendedInvite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.CategoryCreateSpec;
import discord4j.core.spec.TextChannelCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class BotServiceImpl implements BotService {

    @Value("${bot.category}")
    private String categoryName;

    @Value("${bot.channel.command}")
    private String commandChannelName;

    @Value("${bot.channel.announcement}")
    private String announcementChannelName;

    @Autowired
    private GatewayDiscordClient gatewayDiscordClient;

    @Override
    public void memberSync() {
        String sourceServer = "595631782330892309";
        List<String> targetServersIds = Arrays.asList("886678165420916746", "886678225844064256", "886678025188548618");

        List<Member> sourceServerMembers = gatewayDiscordClient
                .getGuildMembers(Snowflake.of(sourceServer))
                .filter(member -> !member.isBot())
                .collectList()
                .block();

        List<ExtendedInvite> list = gatewayDiscordClient.getGuildById(Snowflake.of("595631782330892309")).block().getInvites().collectList().block();

        List<String> sourceServerMemberIds = sourceServerMembers.stream().map(member -> member.getId().asString()).collect(Collectors.toList());

        List<Guild> guilds = targetServersIds.stream().map(id -> gatewayDiscordClient.getGuildById(Snowflake.of(id)).block()).collect(Collectors.toList());

        guilds.forEach(guild -> {
            List<String> membersId = guild.getMembers().map(member -> member.getId().asString()).collectList().block();
            Set<String> memberIdForKick = membersId.stream().filter(id -> !sourceServerMemberIds.contains(id)).collect(Collectors.toSet());
            memberIdForKick.forEach(id -> guild.kick(Snowflake.of(id)).subscribe());
        });
    }

    @Override
    public void roleSync() {

    }

    @Override
    public void initBotChannels(final String guildId) {
        if (guildId == null) {
            throw new RuntimeException("guild id is null");
        }

        Guild guild = gatewayDiscordClient.getGuildById(Snowflake.of(guildId)).block();

        if (guild == null) {
            throw new RuntimeException("guild is null");
        }

        List<String> guildChannels = guild.getChannels()
                .map(ch -> ch.getName().toLowerCase())
                .collectList()
                .block();

        if (guildChannels == null) {
            throw new RuntimeException("guild channels is null");
        }
/*
        if (!guildChannels.contains(categoryName.toLowerCase())) {
            Consumer<CategoryCreateSpec> categoryCreateSpec = x -> x.setName(categoryName);
            categoryCreateSpec.accept(new CategoryCreateSpec());
            guild.createCategory(String.valueOf(categoryCreateSpec)).subscribe();
        }

        String categoryId = getCategoryId(guildId);

        if (!guildChannels.contains(commandChannelName.toLowerCase())) {
            Consumer<TextChannelCreateSpec> commandTextChannelSpec = textChannelCreateSpec -> {
                textChannelCreateSpec.setName(commandChannelName);
                textChannelCreateSpec.setParentId(Snowflake.of(categoryId));
            };

            commandTextChannelSpec.accept(new TextChannelCreateSpec());
            guild.createTextChannel(commandTextChannelSpec).subscribe();
        }

        if (!guildChannels.contains(announcementChannelName.toLowerCase())) {
            Consumer<TextChannelCreateSpec> announcementTextChannelSpec = textChannelCreateSpec -> {
                textChannelCreateSpec.setName(announcementChannelName);
                textChannelCreateSpec.setParentId(Snowflake.of(categoryId));

            };
            announcementTextChannelSpec.accept(new TextChannelCreateSpec());
            guild.createTextChannel(announcementTextChannelSpec).subscribe();
        }*/
    }

    private String getCategoryId(String guildId) {
        return gatewayDiscordClient.getGuildById(Snowflake.of(guildId))
                .block()
                .getChannels()
                .toStream()
                .filter(ch -> ch.getName().equalsIgnoreCase(categoryName))
                .findAny()
                .get()
                .getId()
                .asString();
    }

}

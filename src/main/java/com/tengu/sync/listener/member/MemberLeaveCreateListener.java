package com.tengu.sync.listener.member;

import com.tengu.sync.bean.TenguGuild;
import com.tengu.sync.event.EventListener;
import com.tengu.sync.service.GuildService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MemberLeaveCreateListener implements EventListener<MemberLeaveEvent> {

    @Autowired
    private GuildService guildService;

    @Override
    public Class<MemberLeaveEvent> getEventType() {
        return MemberLeaveEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberLeaveEvent event) {
        Guild guild = event.getGuild().block();
        Member member = event.getMember().get();
        TenguGuild tenguGuild = guildService.findByGuildId(guild.getId().asString());

        if (!tenguGuild.isHead()) {
            return Mono.just(event).then();
        }

        GatewayDiscordClient gatewayDiscordClient = event.getClient();

        List<Guild> guilds = tenguGuild.getTails().stream().map(g -> gatewayDiscordClient.getGuildById(Snowflake.of(g.getGuildId())).block()).collect(Collectors.toList());

        guilds.forEach(g -> {
            boolean foundMember = g.getMembers().filter(isNotBot()).any(isExistInGuild(member)).block();
            if (foundMember) {
                g.kick(member.getId(), "You were kicked out due to server sync ").subscribe();
            }
        });

        return Mono.just(event).then();
    }

    private Predicate<Member> isExistInGuild(Member member) {
        return m -> m.getId().equals(member.getId());
    }

    private Predicate<Member> isNotBot() {
        return m -> !m.isBot();
    }
}


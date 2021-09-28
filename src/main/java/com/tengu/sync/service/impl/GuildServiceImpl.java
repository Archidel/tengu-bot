package com.tengu.sync.service.impl;

import com.tengu.sync.bean.GuildResponse;
import com.tengu.sync.bean.TenguGuild;
import com.tengu.sync.repository.GuildRepository;
import com.tengu.sync.service.GuildService;
import com.tengu.sync.utils.GuildConverter;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GuildServiceImpl implements GuildService {

    @Autowired
    private GuildRepository guildRepository;

    @Override
    public GuildResponse addTail(Guild headGuild, Guild tailGuild, Member member) {
        GuildResponse response = new GuildResponse();
        String headId = headGuild.getId().asString();
        String tailId = tailGuild.getId().asString();

        if (!isMemberExistInBothGuilds(headGuild, tailGuild, member)) {
            response.setError(true);
            response.setErrorMessage("The user does not have both requested discords");
            return response;
        }

        boolean isExistHeadGuild = guildRepository.existsByGuildId(headId);
        if (!isExistHeadGuild) {
            response.setError(true);
            response.setErrorMessage("Head server does not exist in application. Please add head server and then you can add tail server.");
            return response;
        }

        TenguGuild tenguHeadGuild = guildRepository.findByGuildId(headId);
        TenguGuild tenguTailGuild;

        boolean isExistTailGuild = guildRepository.existsByGuildId(tailId);
        if (isExistTailGuild) {
            tenguTailGuild = guildRepository.findByGuildId(tailId);
        } else {
            tenguTailGuild = guildRepository.save(GuildConverter.convert(tailGuild, new TenguGuild()));
        }

        Set<TenguGuild> tenguGuildTeils = tenguHeadGuild.getTails();
        if (tenguGuildTeils == null) {
            tenguHeadGuild.setTails(new HashSet<>());
        }

        tenguHeadGuild.getTails().add(tenguTailGuild);
        tenguHeadGuild = guildRepository.save(tenguHeadGuild);
        response.setSuccessMessage(String.format("The server [%s] successfully added as tail for [%s]", tailId, headId));
        return response;
    }

    @Override
    public List<TenguGuild> findAllHeadGuilds() {
        return findAllHeadGuilds();
    }

    @Override
    public TenguGuild findByGuildId(String id) {
        return guildRepository.findByGuildId(id);
    }

    @Override
    public GuildResponse save(TenguGuild guild) {
        GuildResponse response = new GuildResponse();

        String id = guild.getGuildId();
        boolean isExist = guildRepository.existsByGuildId(id);

        if (isExist) {
            guild = findByGuildId(id);

            response.setGuild(guild);
            response.setError(true);
            response.setErrorMessage(String.format("The server %s [%s] is already HEAD server", guild.getName(), guild.getGuildId()));
            return response;
        }

        guild = guildRepository.save(guild);

        response.setGuild(guild);
        response.setSuccessMessage("Server was successfully added as HEAD server");

        return response;
    }

    @Override
    public void addExeptionalRole(TenguGuild guild, Role role) {

    }

    private boolean isMemberExistInBothGuilds(Guild first, Guild second, Member member) {
        List<String> firstMemberList = first.getMembers().map(m -> m.getId().asString()).collectList().block();
        List<String> secondMemberList = second.getMembers().map(m -> m.getId().asString()).collectList().block();
        String memberId = member.getId().asString();
        return firstMemberList.contains(memberId) && secondMemberList.contains(memberId);
    }

}

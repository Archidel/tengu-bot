package com.tengu.sync.service;

import com.tengu.sync.bean.GuildResponse;
import com.tengu.sync.bean.TenguGuild;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;

import java.util.List;

public interface GuildService {

    GuildResponse addTail(Guild headGuild, Guild tailGuild, Member member);

    List<TenguGuild> findAllHeadGuilds();

    TenguGuild findByGuildId(String id);

    GuildResponse save(TenguGuild guild);

    void addExeptionalRole(TenguGuild guild, Role role);

}

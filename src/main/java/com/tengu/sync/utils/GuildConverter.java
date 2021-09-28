package com.tengu.sync.utils;

import com.tengu.sync.bean.TenguGuild;
import discord4j.core.object.entity.Guild;

public final class GuildConverter {

    private GuildConverter() {
    }

    public static TenguGuild convert(Guild source, TenguGuild target) {
        target.setGuildId(source.getId().asString());
        target.setName(source.getName());
        return target;
    }
}

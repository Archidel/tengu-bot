package com.tengu.sync.repository;

import com.tengu.sync.bean.TenguGuild;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GuildRepository extends MongoRepository<TenguGuild, String> {

    List<TenguGuild> findByIsHead(boolean isHead);

    TenguGuild findByGuildId(String guildId);

    boolean existsByGuildId(String guildID);
}

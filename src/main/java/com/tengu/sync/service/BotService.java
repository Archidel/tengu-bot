package com.tengu.sync.service;

public interface BotService {

    void roleSync();

    void initBotChannels(String guildId);

    void memberSync();

}

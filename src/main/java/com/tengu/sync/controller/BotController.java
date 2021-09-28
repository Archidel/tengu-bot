package com.tengu.sync.controller;

import com.tengu.sync.service.BotService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BotController {

    @Autowired
    private GatewayDiscordClient gatewayDiscordClient;

    @Autowired
    private BotService botService;

    @GetMapping("/guilds")
    public HashMap<String, String> getGuilds() {
        HashMap<String, String> guilds = new HashMap<>();
        gatewayDiscordClient.getGuilds().toStream().forEach(g -> guilds.put(g.getId().asString(), g.getName()));
        return guilds;
    }

    @GetMapping("/sync")
    public List<String> doSync() {
        return gatewayDiscordClient.getGuilds().toStream().map(g -> g.getName()).collect(Collectors.toList());
    }

    @GetMapping("/sync-role")
    public List<String> doRoleSync() {
        /*String main = "595631782330892309";
        List<String> list = Arrays.asList("886678165420916746", "886678225844064256", "886678025188548618");

        Guild mainGuild = gatewayDiscordClient.getGuildById(Snowflake.of(main)).block();
        List<Role> mainRoles = mainGuild.getRoles().collectList().block();
        List<Consumer<RoleCreateSpec>> consumers = new ArrayList<>();

        mainRoles.forEach(role -> {
            Consumer<RoleCreateSpec> roleSpec = roleCreateSpec -> {

                roleCreateSpec.setColor(role.getColor());
                roleCreateSpec.setName(role.getName());
                roleCreateSpec.setMentionable(role.isMentionable());
            };
            consumers.add(roleSpec);
        });

        List<Guild> targetGuilds = list.stream()
                .map(guildId -> gatewayDiscordClient.getGuildById(Snowflake.of(guildId))
                        .block()).collect(Collectors.toList());

        Guild guild = targetGuilds.get(0);
        consumers.forEach(roleCreateSpecConsumer -> {
            roleCreateSpecConsumer.accept(new RoleCreateSpec());
            guild.createRole(roleCreateSpecConsumer).subscribe();
        });*/

        return null;
    }


    @GetMapping("/sync-member")
    public List<String> doMemberSync() {
        botService.memberSync();
        return null;
    }


    @GetMapping("/test")
    public List<String> doTest() {
        Guild guild = gatewayDiscordClient.getGuilds().filter(g -> g.getId().asString().equalsIgnoreCase("595631782330892309")).blockFirst();
        //gatewayDiscordClient.getGuildById("595631782330892309").block().createCategory()

        botService.initBotChannels("595631782330892309");

        return guild.getRoles().toStream().map(role -> role.getName()).collect(Collectors.toList());
    }


}

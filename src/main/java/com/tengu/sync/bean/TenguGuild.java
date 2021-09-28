package com.tengu.sync.bean;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Document(collection = "TenguGuild")
public class TenguGuild {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String guildId;

    private Set<TenguGuild> tails;
    @DBRef
    private List<TenguRole> exceptionalRoles;

    @DBRef
    private List<TenguMember> exceptionalMember;

    private boolean isHead;

    public TenguGuild() {
    }

    public TenguGuild(String guildId, boolean isHead) {
        this.guildId = guildId;
        this.isHead = isHead;
    }

    public TenguGuild(boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenguGuild that = (TenguGuild) o;
        return Objects.equals(id, that.id) && Objects.equals(guildId, that.guildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guildId);
    }
}

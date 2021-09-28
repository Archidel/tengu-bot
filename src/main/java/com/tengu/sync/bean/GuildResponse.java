package com.tengu.sync.bean;

import lombok.Data;

@Data
public class GuildResponse {
    private TenguGuild guild;
    private String successMessage;
    private boolean isError;
    private String errorMessage;
}

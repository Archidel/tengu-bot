package com.tengu.sync.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "TenguRole")
public class TenguRole {

    @Id
    private String id;
    private String roleId;


}

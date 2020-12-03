package net.check321.databasedemo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private Long id;

    private String userCode;

    private String userName;

    private String password;
}

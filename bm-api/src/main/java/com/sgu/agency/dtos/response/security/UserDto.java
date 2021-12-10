package com.sgu.agency.dtos.response.security;

import com.sgu.agency.common.enums.UserModelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String fullname;
    private String email;
    private String password;
    private List<String> permissions;
    private UserModelEnum userModel;
    public Boolean isLimited;
}

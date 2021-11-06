package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.BlockStatusEnum;
import com.sgu.agency.common.enums.UserModelEnum;
import com.sgu.agency.dtos.response.security.UserDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EmployeeFullDto {
    private String id;
    @NotEmpty(message = "Tên nhân viên không được trống")
    private String name;
    @NotEmpty(message = "Số điện thoại không được trống")
    private String phone;
    @NotEmpty(message = "Email nhân viên không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotEmpty(message = "Mật khẩu không được trống")
    private String password;
    private Date birthDate;
    @NotNull(message = "Chức vụ không được trống")
    private RoleFullDto role;
    private Date createdDate;
    private Date updatedDate;

    public UserDto toUserDto() {
        List<String> permissions = new ArrayList<>();
        List<GrantPermissionDto> grantPermissionDtos = this.role.getGrantPermissions();
        for (GrantPermissionDto grantPermissionDto : grantPermissionDtos) {
            String permission = grantPermissionDto.getPermission().getCode();
            if(!permissions.contains(permission)) {
                permissions.add(permission);
            }
        }

        return new UserDto(this.name, this.email, this.password, permissions, UserModelEnum.EMPLOYEE, null);
    }
}

package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.UserModelEnum;
import com.sgu.agency.dtos.response.security.UserDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CustomerDto {
    private String id;
    @NotEmpty(message = "Tên khách hàng không được trống")
    private String name;
    private Date birthDate;
    @NotEmpty(message = "Số điện thoại không được trống")
    private String phone;
    @NotEmpty(message = "Email khách hàng không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotEmpty(message = "Mật khẩu không được trống")
    private String password;
    @NotEmpty(message = "Địa chỉ không được trống")
    private String address;
    private Boolean isValid;
    private RoleDto role;
    private Date createdDate;
    private Date updatedDate;

    public UserDto toUserDto() {
        //List<String> permissions = new ArrayList<>();
        //List<GrantPermissionDto> grantPermissionDtos = this.role.getGrantPermissions();
        //for (GrantPermissionDto grantPermissionDto : grantPermissionDtos) {
        //    String permission = grantPermissionDto.getPermission().getCode();
        //    if(!permissions.contains(permission)) {
        //       permissions.add(permission);
        //   }
        //}

        return new UserDto(this.name, this.email, this.password, new ArrayList<>(), UserModelEnum.CUSTOMER, null);
    }
}

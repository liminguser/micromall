package com.example.micromall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户信息更新参数")
public class UserUpdateDTO {
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别：0-保密，1-男，2-女")
    private Integer gender;

    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;
} 
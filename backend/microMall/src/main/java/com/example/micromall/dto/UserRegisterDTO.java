package com.example.micromall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户注册参数")
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名长度必须在4-16个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和连字符")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码", required = true)
    private String confirmPassword;

    @Size(max = 20, message = "昵称长度不能超过20个字符")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @NotBlank(message = "请阅读并同意用户协议和隐私政策")
    @ApiModelProperty(value = "是否同意用户协议", required = true)
    private String agreement;
} 
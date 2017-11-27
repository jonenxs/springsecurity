package com.nxs.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQueryCondition {

    private String username;

    @ApiModelProperty(value = "用户年龄起始字段")
    private int age;

    @ApiModelProperty(value = "用户年龄终止字段")
    private int ageTo;

    private String xxx;

}

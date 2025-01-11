package com.example.micromall.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页参数")
public class PageParam {
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    @ApiModelProperty(value = "是否升序", example = "true")
    private Boolean isAsc = true;
} 
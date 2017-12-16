package com.jishi.reservation.controller.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sloan on 2017/10/26.
 */

@Data
@ApiModel("日记内容")
public class DiaryContentVO {


    @ApiModelProperty("1是文本  0是图片")
    private Integer type;   //1是文本  0是图片
    @ApiModelProperty("图片url")
    private String url;
    @ApiModelProperty("图片高度")
    private Integer height;
    @ApiModelProperty("图片宽度")
    private Integer width;
    @ApiModelProperty("文本内容")
    private String text;
    @ApiModelProperty("字体")
    private String fontName;
    @ApiModelProperty("字体大小")
    private Integer fontSize;
    @ApiModelProperty("字体颜色")
    private String textColor;
    @ApiModelProperty("字体行间距")
    private Integer lineSpace;

    @ApiModelProperty("每个内容对象的唯一识别id")
    private String contentId;



}

package com.jishi.reservation.dao.models;

import com.jishi.reservation.controller.protocol.DiaryContentVO;
import com.jishi.reservation.controller.protocol.ImageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by sloan on 2017/10/17.
 */

@Data
@ApiModel("日记表")
@Table(name = "diary")
public class Diary {

    @Id
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("发布者idid")
    private Long accountId;
    @ApiModelProperty("昵称id")
    private String nick;
    @ApiModelProperty("h5 url 暂为空")
    private String url;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("状态 0 正常")
    private Integer enable;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("内容 json")
    private String content;
    @ApiModelProperty("简介 供首页列表显示")
    private String brief;
    @ApiModelProperty("审核状态 0 已通过，1待审核 2未通过")
    private Integer status;
    @ApiModelProperty("是否锁定 锁定0 只有自己能看  ，没有锁定1 公布")
    private Integer isLock;



    @Transient
    private Boolean isTop;

    @Transient
    @ApiModelProperty("日记内容列表")
    private List<DiaryContentVO> contentVOList;

    @Transient
    @ApiModelProperty("用户头像")

    private String avatar;

    @Transient
    @ApiModelProperty("浏览次数")
    private Integer scanNum;
    @Transient
    @ApiModelProperty("点赞次数")
    private Integer likedNum;

    @Transient
    @ApiModelProperty("首页列表展示的图片list")
    private List<ImageVO> imgList;

}

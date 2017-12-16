package com.jishi.reservation.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.mapper.BannerMapper;
import com.jishi.reservation.dao.models.Banner;
import com.jishi.reservation.service.enumPackage.DisplayEnum;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class HomeService {

    @Autowired
    BannerMapper bannerMapper;

    /**
     * 增加banner
     * @param bannerUrl
     * @param orderNumber
     */
    public void addBanner(String name,String bannerUrl, String jumpUrl,Integer orderNumber) {
        log.info("增加banner"+" url:"+bannerUrl+" orderNumber:"+orderNumber);
        if(Helpers.isNull(bannerUrl) || Helpers.isNull(orderNumber) || Helpers.isNull(name))
            return;
        Banner addBanner = new Banner();
        addBanner.setName(name);
        addBanner.setBannerUrl(bannerUrl);
        addBanner.setJumpUrl(jumpUrl);
        addBanner.setOrderNumber(orderNumber);
        addBanner.setEnable(EnableEnum.EFFECTIVE.getCode());
        addBanner.setDisplay(DisplayEnum.SHOW.getCode());
        bannerMapper.insert(addBanner);
    }

    /**
     * 修改banner
     * @param bannerId
     * @param bannerUrl
     * @param orderNumber
     */
    public void modifyBanner(Long bannerId, String name,String bannerUrl,String jumpUrl, Integer orderNumber) {
        log.info("修改banner:"+bannerId+" ,url:"+bannerUrl+",jumpUrl:"+jumpUrl+" ,orderNumber:"+orderNumber);
        if(Helpers.isNull(bannerId))
            return;
        Banner oldBanner = bannerMapper.selectByPrimaryKey(bannerId);
        if (!Helpers.isNull(oldBanner)) {
            Banner newBanner = new Banner();
            newBanner.setId(bannerId);
            newBanner.setName(Helpers.isNullOrEmpty(name)?oldBanner.getName():name);
            newBanner.setJumpUrl(Helpers.isNullOrEmpty(jumpUrl)?oldBanner.getJumpUrl():jumpUrl);
            newBanner.setOrderNumber(Helpers.isNullOrEmpty(orderNumber) ? oldBanner.getOrderNumber() : orderNumber);
            newBanner.setBannerUrl(Helpers.isNullOrEmpty(bannerUrl) ? oldBanner.getBannerUrl() : bannerUrl);
            Preconditions.checkState(bannerMapper.updateByPrimaryKeySelective(newBanner) ==1,"更新失败");
        }
    }

    /**
     * 查询banner
     * @param bannerId
     * @return
     */
    public List<Banner> queryBanner(Long bannerId,String name,Integer enable) {
        log.info("查询banner:"+bannerId);

        return bannerMapper.queryBanner(bannerId,name,enable);

    }

    /**
     * 查询banner,分页
     * @param bannerId
     * @param paging
     * @return
     */
    public PageInfo<Banner> queryBannerPageInfo(Long bannerId,String name,Integer enable, Paging paging){
        if(paging != null)
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize(),paging.getOrderBy());
        return new PageInfo(queryBanner(bannerId,name,enable));
    }

    /**
     * 删除单个banner
     * @param bannerId
     */
    public void deleteBanner(Long bannerId) {
        log.info("删除banner:"+bannerId);
        if(Helpers.isNull(bannerId))
            return;
        bannerMapper.deleteByPrimaryKey(bannerId);
    }

    public void deleteBannerBatch(String bannerIdList) {
        log.info("删除banner:"+bannerIdList);
        String[] split = bannerIdList.split(",");
        List<Long> idList = new ArrayList<>();
        for (String id : split) {
            idList.add(Long.valueOf(id));
        }

        bannerMapper.deleteBannerBatch(idList);

    }

    public void hideOrShowBanner(Long bannerId) {
        log.info("显示/隐藏banner:"+bannerId);
        List<Banner> bannerList = this.queryBanner(bannerId, null, null);
        if(Helpers.isNull(bannerId) || Helpers.isNull(bannerList))
            return;
        bannerList.get(0).setDisplay(bannerList.get(0).getDisplay().equals(0)?DisplayEnum.HIDE.getCode():
                DisplayEnum.SHOW.getCode());
        bannerMapper.updateByPrimaryKey(bannerList.get(0));

    }

    public void sortBanner(Long bannerId,Integer sort) {
        Banner queryBanner = new Banner();
        queryBanner.setId(bannerId);
        Banner select = bannerMapper.selectOne(queryBanner);

        if( Helpers.isNull(select))
            return;
        select.setOrderNumber(sort);
        bannerMapper.updateByPrimaryKeySelective(select);

    }
}

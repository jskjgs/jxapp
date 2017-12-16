package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.doraemon.base.redis.RedisOperation;
import com.doraemon.base.util.MD5Encryption;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.mapper.ManagerMapper;
import com.jishi.reservation.dao.mapper.PermissionMapper;
import com.jishi.reservation.dao.models.Manager;
import com.jishi.reservation.dao.models.Permission;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class ManagerService {

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    private RedisOperation redisOperation;

    //保存登陆信息
    public final static String ADD_TOKEN = ""
            + " local token = redis.call('get', KEYS[1]); "
            + " if token then "
            + "     redis.call('del',token); "
            + " end "
            + " redis.call('set',KEYS[1],KEYS[2]); "
            + " redis.call('set',KEYS[2],KEYS[1]); "
            + " return 1 ";


    //注销用户
    public final static String DEL_TOKEN = ""
            + " local token = redis.call('get',KEYS[1]); "
            + " redis.call('del',token); "
            + " redis.call('del',KEYS[1]); "
            + " return 1 ";


    public Manager findAccountByAccount(String account) {

        Manager manager =  managerMapper.findAccountByUserName(account);
        Preconditions.checkNotNull(account,"该账户不存在，请检查账号");
        return  manager;
    }

    public String loginByAccountAndPwd(Long id) throws Exception {

        String token = createToken(id);
        List<String> keys = new ArrayList<String>();
        keys.add(String.valueOf(id));
        keys.add(token);
        Preconditions.checkState(Integer.valueOf(String.valueOf(redisOperation.usePool().eval(ADD_TOKEN,keys,new ArrayList<String>()))) == 1,"保存登陆信息失败.");
        //设置token过期时间
        redisOperation.usePool().expire(token,28800);
        redisOperation.usePool().expire(String.valueOf(id),28800);
        return token;
    }

    public Long returnIdByToken(HttpServletRequest request) throws Exception {
        Enumeration<String> headerNames = request.getHeaderNames();

        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(Constant.ADMIN_TOKEN)){
                token = cookie.getValue();
            }
        }

        log.info("admin_token：" + token);
        if(token == null || "".equals(token) || "null".equals(token)){
            log.info("token為空...");
            return -1L;

        }
        String strId = redisOperation.usePool().get(token);
        return (strId == null || strId.isEmpty() ? Long.valueOf(-1) : Long.valueOf(strId));


    }

    /**
     * 创建token值
     * @param user
     * @return
     * @throws Exception
     */
    private static String createToken(Long user) throws Exception {
        Random r = new Random((new Date().getTime()));
        return MD5Encryption.getMD5(user + (new Date()).getTime() + String.valueOf(r.nextInt(100000000)));
    }

    /**
     * 注销用户
     * @param user
     * @throws Exception
     */
    public  void logout(String user) throws Exception {
        List<String> keys = new ArrayList<String>();
        String s = redisOperation.get(user);
        if(!"".equals(s) && s!=null){
            keys.add(user);
            Preconditions.checkState(Integer.valueOf(String.valueOf(redisOperation.usePool().eval(DEL_TOKEN,keys,new ArrayList<String>()))) == 1,"注销用户登陆信息失败.");

        }
    }

    public void create(String account, String password, String permission) throws NoSuchAlgorithmException {

        Manager manager = new Manager();
        manager.setAccount(account);
        manager.setEnable(EnableEnum.EFFECTIVE.getCode());
        manager.setPassword(MD5Encryption.getMD5(password));
        manager.setPermission(permission);

        managerMapper.insertReturnId(manager);

    }

    public PageInfo<Manager> queryByPage(Paging paging) {

        if(!Helpers.isNullOrEmpty(paging))
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize(),paging.getOrderBy());
        return new PageInfo(queryByPage());
    }

    private List queryByPage() {

        Gson gson = new Gson();

        List<Manager> managerList = managerMapper.selectEnableManager();
        for (Manager manager : managerList) {
            manager.setPassword(null);
            List<Permission> permissionList = new ArrayList<>();
            List<String> list = gson.fromJson(manager.getPermission(),
                    new TypeToken<List<String>>() {
                    }.getType());
            for (String key : list) {
                Permission permission = permissionMapper.queryByKey(key);
                permissionList.add(permission);
            }
            manager.setPermissionList(permissionList);
        }
        return managerList;

    }

    public void changeInfo(Long id, String permission, String password) throws NoSuchAlgorithmException {

        Manager manager = managerMapper.findById(id);
        Preconditions.checkNotNull(manager,"查不到该管理账号");

        manager.setPassword(password!=null&&!"".equals(password)?MD5Encryption.getMD5(password):manager.getPassword());
        manager.setPermission(permission!=null&&!"".equals(permission)?permission:manager.getPermission());
    }

    public void deleteSoft(Long id) {
        Manager manager = managerMapper.findById(id);
        Preconditions.checkNotNull(manager,"查不到该管理账号");

        manager.setEnable(EnableEnum.DELETE.getCode());
        managerMapper.updateByPrimaryKeySelective(manager);
    }
}

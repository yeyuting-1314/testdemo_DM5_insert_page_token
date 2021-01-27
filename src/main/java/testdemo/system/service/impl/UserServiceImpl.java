package testdemo.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import testdemo.base.Result;
import testdemo.constants.Constants;
import testdemo.system.dao.UserMapper;
import testdemo.system.dto.User;
import testdemo.system.service.UserService;
import testdemo.util.JedisUtil;
import testdemo.util.PageBean;
import testdemo.util.Results;
import testdemo.util.TokenUtil;
import testdemo.util.interceptor.AuthenticationInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper ;

    @Autowired
    TokenUtil tokenUtil ;

    @Autowired
    JedisUtil jedisUtil ;

    @Override
    public User selectOne(int id){

        return userMapper.selectOne(id) ;
    }

    /*
     * 根据用户名查询某一条数据
     * */
    public User selectByName(String name){

        return userMapper.selectByName(name) ;
    }

    @Override
    public List<User> select(){
        return userMapper.select();
    }

    @Override
    public Integer insertOne(User user){
        return userMapper.insertOne(user);
    }

    @Override
    public Integer insertMany(List<User> userList){
        return userMapper.insertMany(userList) ;
    }

    public Integer updateById(User user){
        return userMapper.updateById(user ) ;
    }
    @Override
    public Integer deleteOne(int id){
        return userMapper.deleteOne(id) ;
    }

    @Override
    public List<User> selectForPage1(int startIndex , int pageSize){
        return userMapper.selectForPage1(startIndex , pageSize) ;

    }

    public List<User> selectForPage2(Map<String, Object> map){

        return userMapper.selectForPage2(map);
    }

    public Integer selectCount(){
        return userMapper.selectCount() ;
    }

    public List<User> selectForPage3(PageBean pageBean){
        return userMapper.selectForPage3(pageBean) ;
    }

    public List<User> selectForPage4(Map<String, Object> map){
        return userMapper.selectForPage4(map) ;
    }

    public Integer selectCount2(String keywords){
        return userMapper.selectCount2(keywords) ;
    }

    public Result loginCheck(User user , HttpServletResponse response){
        User user1 = userMapper.selectByName(user.getUserName()) ;
        if(user1 == null ){
            //response.sendRedirect("/login");
            return Results.failure("用户不存在,") ;
        }
        if(!user1.getPassword().equals(user.getPassword())){
            return Results.failure("密码输入错误") ;
        }
        String token = tokenUtil.generateToken(user1) ;
        System.out.println("token:" + token);
        user.setToken(token);

        // 设置cookie的值
        Cookie cookie = new Cookie("token" , token) ;

        cookie.setPath("/");
        response.addCookie(cookie);
        System.out.println("cookie:"+cookie);

        return Results.successWithData(user) ;
    }

    public Result loginWithRedis(User user ){
        User user1 = userMapper.selectByName(user.getUserName()) ;

        if(user1 == null ){
            //response.sendRedirect("/login");
            return Results.failure("用户不存在,") ;
        }
        if(!user1.getPassword().equals(user.getPassword())){
            return Results.failure("密码输入错误") ;
        }
        //将原有的token值全部干掉，防止重复登陆
        Jedis jedis = jedisUtil.getResource();
         //存入键值对
        String jedisKey = jedis.get(user1.getUserName()) ;
        if(jedisKey != null){
            jedisUtil.delString(user1.getUserName());
        }
        //生成新的token
        String token = tokenUtil.generateToken(user1) ;
        user1.setToken(token);
        //将新生成的token放到redis中
        jedisUtil.tokenToJedis(user1);

        return Results.successWithData(user1) ;
    }

    //后面加角色 三个角色 实现页面访问
    //控制器  用户管理  code==多少得时候，才可以做新增、删除、修改等
    // 一个类做user类操作
    // 另一个类可以操作user类和角色类操作 名称
}

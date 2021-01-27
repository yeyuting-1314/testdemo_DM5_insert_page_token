package testdemo.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import testdemo.constants.Constants;
import testdemo.system.dto.User;

import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/19
 */
@Component
public class JedisUtil {
    public void tokenToJedis(User user){
        Jedis jedis = getResource() ;
        jedis.set(user.getUserName() , user.getToken()) ;
        jedis.expire(user.getUserName() , Constants.TOKEN_EXPIRE_TIME) ;
        //存储对象
        jedis.set(user.getToken() , user.getUserName()) ;
        jedis.expire(user.getToken() , Constants.TOKEN_EXPIRE_TIME) ;
        Long currentTime =System.currentTimeMillis() ;
        jedis.set(user.getUserName()+user.getToken() , currentTime.toString()) ;
        //用完关闭
        jedis.close();
        System.out.println("redis中token值为：" + jedis.get(user.getUserName()));
        System.out.println("redis中用户信息值为：" + jedis.get(user.getToken()));
    }

    /**
    *2021/1/19 15:59 
    * * @param key
    * * @return : void
    */
    public void delString(String key) {
       try{
           Jedis jedis = getResource();
           /*//存入键值对
           String jedisKey = jedis.get(key) ;

           if(jedisKey.equals(null)){
               return;
           }*/
           ScanParams scanParams = new ScanParams() ;
           StringBuilder paramKey = new StringBuilder("*").append(key).append("*") ;
           scanParams.match(paramKey.toString()) ;
           scanParams.count(1000) ;
           ScanResult<String> sr = jedis.scan("0" , scanParams) ;
           List<String> a = sr.getResult() ;
           for(String delkey : a){
               jedis.del(delkey) ;
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    /**
    *2021/1/19 15:59 
    * * @param 
    * * @return : redis.clients.jedis.Jedis
    */
    public Jedis getResource(){
        return new Jedis("localhost" , 6379) ;
    }
}

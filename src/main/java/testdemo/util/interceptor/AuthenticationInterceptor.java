package testdemo.util.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;
import testdemo.base.Result;
import testdemo.constants.Constants;
import testdemo.util.Results;
import testdemo.util.TokenUtil;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义拦截器类

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired TokenUtil tokenUtil ;

    @Override
    public boolean preHandle (HttpServletRequest request ,
                              HttpServletResponse response , Object o )throws Exception{

        String token = request.getHeader("token") ;

        if(StringUtils.isBlank(token)){
            responseJson(response , "未登录的请求" , "401");
            return false ;
        }
        Jedis jedis = new Jedis("localhost" , 6379) ;
        String username = "" ;
        username = jedis.get(token) ;
        System.out.println("redis中用户信息值为：" + jedis.get(token));
        //不能重复登陆的问题
        //密码加密存到数据库
        if(StringUtils.isBlank(username)){
            jedis.close();
            responseJson(response,"token无效" , "401");
            return false ;
        }
            Long tokenBirthTime = Long.valueOf(jedis.get(username+token)) ;
            Long diff = System.currentTimeMillis() - tokenBirthTime ;
            //修改为时间低于某一个值得时候进行刷新
            if (diff > Constants.TOKEN_RESET_TIME){
                jedis.expire(username , Constants.TOKEN_EXPIRE_TIME) ;
                jedis.expire(token , Constants.TOKEN_EXPIRE_TIME) ;
                System.out.println("token 有效时间成功更新");
                Long newBirthTime =System.currentTimeMillis();
                jedis.set(token+username , newBirthTime.toString()) ;
            }
            jedis.close();
            return true ;
    }

    private void responseJson(HttpServletResponse response , String msg , String code) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Result result = Results.failure(code, msg) ;
        response.getWriter().println(JSON.toJSONString(result));
    }


}

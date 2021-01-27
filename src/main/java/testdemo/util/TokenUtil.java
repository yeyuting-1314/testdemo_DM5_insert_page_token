package testdemo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import testdemo.system.dto.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;


@Component
public class TokenUtil {
    /*
    * 生成token
    * */
    public String generateToken(User user){
        Date start  = new Date() ;
        long currentTime = System.currentTimeMillis() + 60*60*1000 ; //一小时的有效时间
        Date end = new Date(currentTime) ;
        String token = "" ;
        token = JWT.create()
                .withAudience(Integer.toString(user.getId()))
                .withAudience(user.getUserName())
                .withIssuedAt(start)
                .withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token ;
    }

    /*
    * 获取指定token中某个属性值
    * */
    public static String get(String token , String key){
        List<String> list = JWT.decode(token).getAudience() ;
        String userId = list.get(0) ;
        return userId ;
    }
    /*
    * 获取request
    * */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest() ;
    }

    /*
    * 获取token
    * */
    public String getToken(HttpServletRequest request)  {
        //这里不能塞到token中去，必须要放到session中，或者放到redis中去
        //token放到redis库里面  token表
        // Enumeration<String> headerNames 后期改为存入redis缓存
        //用request里面的token和redis里面的token对比，如果对的上的话就放行，不然就不给过
        /*Enumeration<String> headerNames = request.getHeaderNames() ;
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement() ;
            if(name.equals("token")){
                return request.getHeader("token") ;
            }
        }
*/
        /*
        Cookie[] cookies = request.getCookies() ;

        for(Cookie c : cookies){
            if(c.getName().equals("token")){
                return c.getValue() ;
            }
        }*/
        return null ;
    }

}

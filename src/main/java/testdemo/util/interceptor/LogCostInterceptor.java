package testdemo.util.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//拦截器实现 完成和过滤器一样的功能 需要实现HandlerInterceptor这个接口
public class LogCostInterceptor implements HandlerInterceptor {
    long start = System.currentTimeMillis() ;

    //preHandle是请求执行前执行的
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse ,
                             Object o) throws Exception{
        start = System.currentTimeMillis() ;
        return true ;
    }

    //postHandler是请求结束执行的，但只有preHandle方法返回true的时候才会执行
    @Override
    public void postHandle(HttpServletRequest httpServletRequest ,
                              HttpServletResponse httpServletResponse ,
                              Object o , ModelAndView modelAndView)throws Exception{
        System.out.println("Interceptor cost1 = "  + (System.currentTimeMillis()-start));
    }

    //afterCompletion是视图渲染完成后才执行，同样需要preHandle返回true，该方法通常用于清理资源等工作
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest ,
                                HttpServletResponse httpServletResponse ,
                                Object o , Exception e)throws Exception{

    }

}

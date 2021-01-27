package testdemo.util;


import javax.servlet.*;
import java.io.IOException;

//过滤器生成
public class LogCostFilter implements Filter {

    //init()方法用来初始化过滤器，可以在init()方法中获取Filter中的初始化参数。
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    /*
     * doFilter()方法完成过滤操作。当请求发过来的时候，过滤器将执行doFilter方法。
     *在HttpServletRequest 执行doFilter()之前，根据需要检查 HttpServletRequest ，同时也可以修改HttpServletRequest 请求头和数据。
     *在HttpServletResponse 执行doFilter()之后，根据需要检查 HttpServletResponse ，同时也可以修改HttpServletResponse响应头和数据。
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        //方法执行前先记录时间戳
        long start = System.currentTimeMillis() ;
        //通过过滤器链完成请求的执行
        filterChain.doFilter(servletRequest , servletResponse);
        //在返回结果之间计算执行的时间
        System.out.println("Executor cost = " + (System.currentTimeMillis()-start));

    }

    /*
     * destroy()
     *Filter对象创建后会驻留在内存，当web应用移除或服务器停止时调用destroy()方法进行销毁。
     * 在Web容器卸载 Filter 对象之前被调用。destroy()方法在Filter的生命周期中仅执行一次。
     * 通过destroy()方法，可以释放过滤器占用的资源。
     *
     * */
    @Override
    public void destroy() {

    }
}

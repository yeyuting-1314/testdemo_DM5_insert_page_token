package testdemo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import testdemo.util.LogCostFilter;

//@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean registerFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean() ;
        //实例化Filter类
        registration.setFilter(new LogCostFilter());
        //指定url的匹配模式
        registration.addUrlPatterns("/*");
        //设置过滤器名称和执行顺序
        registration.setName("LogCostFilter");
        registration.setOrder(1);
        return registration ;

    }
}

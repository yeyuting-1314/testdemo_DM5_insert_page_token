package testdemo.system.service;

import testdemo.base.Result;
import testdemo.system.dto.User;
import testdemo.util.PageBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface UserService {
    /*
     * 通过id查询用户
     * */
    public List<User> select();

    /*
    * 根据id查询某一条数据
    * */
    public User selectOne(int id);

    /*
     * 根据用户名查询某一条数据
     * */
    public User selectByName(String name);

    /*
     * 新增一个用户
     * */
    public Integer insertOne(User user);

    /*
     * 新增多个用户
     * */
    public Integer insertMany(List<User> userList);

    /*
    * 更新数据
    * */
    public Integer updateById(User user) ;

    /*
     * 删除某一条数据
     * */
    public Integer deleteOne(int id);

    /*
    * 分页查询1
    * @param 起始页面
    * @param 数据条数
    * */
    public List<User> selectForPage1(int startIndex , int pageSize);

    /*
    * 分页查询2
    * @param map类型
    * */
    public List<User> selectForPage2(Map<String, Object> map);

    //查询总条数
    public Integer selectCount();

    /*
    * 分页查询3 对象
    * */
    public List<User> selectForPage3(PageBean pageBean);

    //模糊查询
    public List<User> selectForPage4(Map<String, Object> map) ;

    public Integer selectCount2(String keywords) ;

    public Result loginCheck(User user , HttpServletResponse response);

    public Result loginWithRedis(User user ) ;




}

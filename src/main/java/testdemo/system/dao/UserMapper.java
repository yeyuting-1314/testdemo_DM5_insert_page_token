package testdemo.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import testdemo.system.dto.User;
import testdemo.util.PageBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserMapper {

    public List<User> select();

    public User selectOne(int id);

    /*
     * 根据用户名查询某一条数据
     * */
    public User selectByName(String name) ;

    public Integer insertOne(User user);

    public Integer insertMany(@Param("list") List<User> userList);

    public Integer updateById(User user) ;


    public Integer deleteOne(int id);

    //分页查询 通过开始页和数据条数进行查询
    public List<User> selectForPage1(int startIndex , int pageSize) ;

    //分页查询  通过map进行查询
    public List<User> selectForPage2(Map<String, Object> map);

    public Integer selectCount();

    public List<User> selectForPage3(PageBean pageBean);

    //模糊查询符合条件条数
    public Integer selectCount2(String keywords);

    //分页加模糊查询
    public List<User> selectForPage4(Map<String, Object> map);



}

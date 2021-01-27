package testdemo.system.controller;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;
import testdemo.base.Result;
import testdemo.constants.BaseEnums;
import testdemo.system.dto.User;
import testdemo.system.service.UserService;
import testdemo.util.Md5Utils;
import testdemo.util.PageBean;
import testdemo.util.Results;

import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class userController {


    @Autowired
    UserService userService ;

    /*
    * 分页查询所有条数据
    * */
    @GetMapping("/selectStartIndexAndPageSize")
    public Result select(@RequestParam("currentPage")int currentPage ,
                         @RequestParam("pageSize") int pageSize){
        //PageHelper.startPage(1,2);
        //List<User> userList = userService.select();
        List<User> selectForPage = userService.
                selectForPage1((currentPage-1)*pageSize, pageSize) ;
        for (User user : selectForPage) {
            System.out.println(user);
        }
        return Results.successWithData(selectForPage,
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());

    }

    /*
     * 查询所有条数据
     * */
    @GetMapping("/selectByMap")
    public Result select1(@RequestParam("currentPage")int currentPage ,
                          @RequestParam("pageSize") int pageSize){
        //PageHelper.startPage(1,2);
        //List<User> userList = userService.select();
        Map<String , Object> map=new HashMap<>();
        map.put("startIndex" , (currentPage-1)*pageSize);
        map.put("pageSize" , pageSize) ;
        List<User> selectForPage2 = userService.selectForPage2(map) ;
        for (User user : selectForPage2) {
            System.out.println(user);
        }
        return Results.successWithData(selectForPage2,
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());

    }

    /*
     * 查询所有条数据
     * */
    @GetMapping("/selectByPageBean")
    public Result selectByPageBean(@RequestParam("currentPage")int currentPage ,
                          @RequestParam("pageSize") int pageSize){
        PageBean pageBean = new PageBean() ;
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //查询总条数
        Integer count = userService.selectCount() ;
        //放到pageBean中
        pageBean.setTotalCount(count);
        List<User> userList = userService.selectForPage3(pageBean) ;
        for (User user : userList) {
            System.out.println(user);
        }
        System.out.println("当前第"+pageBean.getCurrentPage()+"页，共"+count+"条");
        return Results.successWithData(userList,
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
    }

    /*
     * 模糊查询后分页查询
     * */
    @GetMapping("/selectByLike")
    public Result selectByLike(@RequestParam("keywords")String keywords ,
            @RequestParam("currentPage")int currentPage ,
            @RequestParam("pageSize") int pageSize){
        PageBean pageBean = new PageBean() ;
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //查询总条数
        Integer count = userService.selectCount2(keywords) ;
        //放到pageBean中
        pageBean.setTotalCount(count);
        Map<String , Object> map = new HashMap<>() ;
        map.put("startIndex" , (currentPage-1)*pageSize);
        map.put("pageSize" , pageSize) ;
        map.put("keywords" , keywords) ;
        List<User> userList = userService.selectForPage4(map) ;
        for (User user : userList) {
            System.out.println(user);
        }
        System.out.println("当前第"+pageBean.getCurrentPage()+"页，共"+count+"条");
        return Results.successWithData(userList,
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
    }



    /*
    * 查询某条数据
    * */
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam("id") int id){

        return Results.successWithData(userService.selectOne(id),
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
    }


    /*
    * 插入一条数据
    * */
    @PostMapping("/InsertOne")
    public Result InsertOne(@RequestBody User user){
        return Results.successWithData(userService.insertOne(user),
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
    }

    /*
    * 插入多条数据
    * */
    @PostMapping("/InsertMany")
    public Result InsertMany(@RequestBody List<User> userlist){
        return Results.successWithData(userService.insertMany(userlist),
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
    }

    /*
    *按照id更新数据
    * */
    @PostMapping("/UpdateById")
    public Result UpdateById(@RequestBody User user){
        User user1 = userService.selectOne(user.getId()) ;
        System.out.println(user1);

        if(user1!=null){
            return Results.successWithData(userService.updateById(user),
                    BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
        }else{
            return Results.failure() ;
        }


    }

    /*
    * 删除某条数据
    * */
    @PostMapping("/DeleteOne")
    public Result DeleteOne(@RequestParam("id") int id){

        return Results.successWithData(userService.deleteOne(id),
                BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
    }

    @GetMapping("/login")
    public String test1(){
        System.out.println(PropertiesUtil.class.getClassLoader().
                getResource("").getPath());
        return "登陆界面" ;
    }

    @PostMapping("/loginCheck")
    @ResponseBody
    public  Result login(@RequestBody User user , HttpServletResponse response){
        Result result = userService.loginCheck(user , response) ;
        return result ;
    }

    @PostMapping("/loginWithRedis")
    @ResponseBody
    public  Result loginWithRedis(@RequestBody User user){
        Result result = userService.loginWithRedis(user) ;
        return result ;
    }
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        String password = Md5Utils.code(user.getPassword()) ;
        System.out.println(password);
        user.setPassword(password);
        userService.insertOne(user) ;
        return Results.success("成功注册") ;
    }

    @GetMapping("/testString")
    public void testString(){
       for(int i = 0 ; i<10 ; i++){
           for (int j = 0 ;j<i ; i++){
               if(j%2 == 0 ){
                   continue;
               }
           }
       }

    }





}

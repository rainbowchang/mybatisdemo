package com.example.mybatisdemo;

import com.example.mybatisdemo.entity.User;
import com.example.mybatisdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MybatisdemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void test(){
//        User user = userService.Sel("2");
//        System.out.println(user.toString());
//        userService.del(2);
//        userService.queryUser();

//        {
//            System.out.println(userService.get("2"));
//        }

        {
            User user = new User();
            user.setId("1");
            System.out.println(userService.get(user));
        }

        {
            User user = new User();
            user.setCorpCode("T");
            List<User> userList = userService.findList(user);
            userList.forEach(System.out::println);
        }

        {
            User user = new User();
            user.setUserName("zzsdfff");
            user.setPassWord("1fgr67");
            user.setRealName("zzsdfff");
            user.setCorpCode("DFG");
            long l = userService.insert(user);
            System.out.println("Insert " + l);
        }

        {
            User user = new User();
            user.setId("1377524724629827584");
            long l = userService.delete(user);
            System.out.println("delete " + l);
        }

        {
            User user = new User();
            user.setId("3");
            user.setPassWord("weiottuuuuweerer");
            long l = userService.update(user);
            System.out.println("update " + l);
        }

        {
            User user = userService.Sel("1");
            System.out.println(user);
        }
    }

}

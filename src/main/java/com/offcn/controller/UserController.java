package com.offcn.controller;

import com.offcn.dao.UserRespository;
import com.offcn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 该方法用于注册用户
     * */
    @PostMapping("/signup")
    @ResponseBody
    public String signUp(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userRespository.save(user);
            return "成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "失败";
        }
    }
}

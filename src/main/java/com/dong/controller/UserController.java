package com.dong.controller;


import com.dong.Vo.UserVo;
import com.dong.common.R;
import com.dong.pojo.User;
import com.dong.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public R<String> login(@RequestBody UserVo user, HttpServletRequest request){
        String code = user.getCode();
        String phone = user.getPhone();
       code= DigestUtils.md5DigestAsHex(code.getBytes());
       User user1= userService.selectByCode(phone);
        if (user1==null){
            return R.error("用户不存在");
        }
        if (!user1.getPassword().equals(code)){
            return R.error("密码错误");
        }
        request.getSession().setAttribute("user",user1.getId());
        return R.success("登录成功");
    }
}

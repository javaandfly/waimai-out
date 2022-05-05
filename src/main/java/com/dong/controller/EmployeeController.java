package com.dong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.Vo.LoginVo;
import com.dong.common.R;
import com.dong.pojo.Employee;
import com.dong.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.time.LocalDateTime;

/**
 * @author LJM
 * @create 2022/4/15
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping("/login") //使用restful风格开发
    @ResponseBody
    public R<Employee> login(HttpServletRequest request, @RequestBody LoginVo loginVo) {//接收前端的json数据,这个json数据是在请求体中的
        //这里为什么还有接收一个request对象的数据?
        //登陆成功后，我们需要从请求中获取员工的id，并且把这个id存到session中，这样我们想要获取登陆对象的时候就可以随时获取

        //1、将页面提交的密码password进行md5加密处理
        String password = loginVo.getPassword();//从前端用户登录拿到的用户密码
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对用户密码进行加密

        //2、根据页面提交的用户名username查询数据库
      Employee emp= employeeService.selectByUsername(loginVo.getUsername());

        //3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("用户不存在");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            //emp.getPassword()用户存在后从数据库查询到的密码(加密状态的)  password是前端用户自己输入的密码(已经加密处理)
            return R.error("密码不正确");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        //把从数据库中查询到的用户返回出去
        return R.success(emp);
    }

    /**
     * 退出功能
     * ①在controller中创建对应的处理方法来接受前端的请求，请求方式为post；
     * ②清理session中的用户id
     * ③返回结果（前端页面会进行跳转到登录页面）
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 添加用户
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping
    @ResponseBody
    public R<String> addEmployee(@RequestBody LoginVo loginVo,HttpServletRequest request){

        return R.success(employeeService.addEmployee(loginVo,request));
    }

    /**
     *分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ResponseBody
    public R<Page> pageR(int page,int pageSize,String name){
        //这里之所以是返回page对象(mybatis-plus的page对象)，是因为前端需要这些分页的数据(比如当前页，总页数)
        //在编写前先测试一下前端传过来的分页数据有没有被我们接受到
        //log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器  就是page对象
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器  就是动态的封装前端传过来的过滤条件  记得加泛型
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //根据条件查询  注意这里的条件是不为空
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加一个排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //
        //执行查询  这里不用封装了mybatis-plus帮我们做好了
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 更改状态
     * @param employee
     * @param request
     * @return
     */
    @PutMapping
    @ResponseBody
    public R<String> updateEmployee(@RequestBody Employee employee,HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("employee");
        if (id==null){
            return R.error("用户未登录");
        }
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(id);
        employeeService.updateById(employee);
        return R.success("信息更改成功");
    }
    /**
     * 根据前端传过来的员工id查询数据库进行数据会显给前端
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee) ;
        }
        return R.error("没有查询到该员工信息");

    }
}
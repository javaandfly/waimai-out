package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.Vo.LoginVo;
import com.dong.pojo.Employee;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 根据username查找
     * @param username
     * @return
     */
    Employee selectByUsername(String username);

    /**
     * 添加员工
     * @param loginVo
     * @return
     */
    String addEmployee(LoginVo loginVo, HttpServletRequest request);

}

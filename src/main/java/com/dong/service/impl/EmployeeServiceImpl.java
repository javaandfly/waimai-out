package com.dong.service.impl;


import com.dong.Vo.LoginVo;
import com.dong.mapper.EmployeeMapper;
import com.dong.pojo.Employee;
import com.dong.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
  private   EmployeeMapper employeeMapper;
    @Override
    public Employee selectByUsername(String username) {

        return  employeeMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public String addEmployee(LoginVo loginVo, HttpServletRequest request) {
        String username = loginVo.getUsername();
       String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        String phone = loginVo.getPhone();
        String sex = loginVo.getSex();
        String name = loginVo.getName();
        String idNumber = loginVo.getIdNumber();
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPassword(password);
        employee.setSex(sex);
        employee.setUsername(username);
        employee.setPhone(phone);
        employee.setIdNumber(idNumber);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(id);
//        employee.setCreateUser(id);
            employeeMapper.insert(employee);

        return "添加成功";
    }
}

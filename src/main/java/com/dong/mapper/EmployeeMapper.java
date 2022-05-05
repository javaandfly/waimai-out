package com.dong.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
        /***
         * 根据username查找
         * @param username
         * @return
         */
        Employee selectByUsername(String username);
}

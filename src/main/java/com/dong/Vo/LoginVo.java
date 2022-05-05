package com.dong.Vo;

import com.dong.pojo.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo extends Employee {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String idNumber;

}

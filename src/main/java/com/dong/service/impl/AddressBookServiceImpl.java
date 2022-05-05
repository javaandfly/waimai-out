package com.dong.service.impl;


import com.dong.mapper.AddressBookMapper;
import com.dong.pojo.AddressBook;
import com.dong.service.IAddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

}

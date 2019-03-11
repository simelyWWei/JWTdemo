package com.ww.example.systemdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.example.systemdemo.domain.SysPerson;

public interface SysPersonService extends IService<SysPerson> {
    SysPerson getByUserName(String userName);
}

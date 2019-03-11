package com.ww.example.systemdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.example.systemdemo.dao.SysPersonMapper;
import com.ww.example.systemdemo.domain.SysPerson;
import com.ww.example.systemdemo.service.SysPersonService;
import org.springframework.stereotype.Service;

@Service
public class SysPersonServiceImpl extends ServiceImpl<SysPersonMapper,SysPerson> implements SysPersonService {

    @Override
    public SysPerson getByUserName(String userName) {
        QueryWrapper<SysPerson> wrapper = new QueryWrapper<>();
        wrapper.eq("account",userName);
        return this.getOne(wrapper);
    }
}

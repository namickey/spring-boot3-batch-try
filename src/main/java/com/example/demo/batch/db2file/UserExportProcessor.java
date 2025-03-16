package com.example.demo.batch.db2file;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;

@Component
public class UserExportProcessor implements ItemProcessor<Users, Users> {

    @Override
    public Users process(@SuppressWarnings("null") Users users) throws Exception {
        return users;
    }
}

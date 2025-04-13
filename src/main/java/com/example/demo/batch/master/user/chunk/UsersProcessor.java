package com.example.demo.batch.master.user.chunk;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;

/** プロセッサー */
@Component
public class UsersProcessor implements ItemProcessor<Users, Users> {

    @Override
    public Users process(@SuppressWarnings("null") Users users) throws Exception {
        return users;
    }
}

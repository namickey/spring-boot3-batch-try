package com.example.demo.batch.file2db.csv01;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;
import com.example.demo.common.mapper.UsersMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserImportWriter implements ItemWriter<Users> {
    
    private final UsersMapper usersMapper;

    @Override
    public void write(@NonNull Chunk<? extends Users> list) throws Exception {

        for (Users users: list) {
            usersMapper.regist(users);
        }
    }
}

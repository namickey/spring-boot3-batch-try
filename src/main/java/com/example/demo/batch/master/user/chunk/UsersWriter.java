package com.example.demo.batch.master.user.chunk;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;

import lombok.RequiredArgsConstructor;

/** ライター */
@Component
@RequiredArgsConstructor
public class UsersWriter implements ItemWriter<Users> {
    
    @Override
    public void write(@NonNull Chunk<? extends Users> list) throws Exception {

        System.out.println("********* start ************");

        for (Users users: list) {
            System.out.println(users);
        }

        System.out.println("********* end **************");
    }
}

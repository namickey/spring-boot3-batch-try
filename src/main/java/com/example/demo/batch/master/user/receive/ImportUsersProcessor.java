package com.example.demo.batch.master.user.receive;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;
import com.example.demo.core.exception.AppException;

@Component
public class ImportUsersProcessor implements ItemProcessor<ImportUsersItem, Users> {

    @Override
    public Users process(@NonNull ImportUsersItem csvUser) throws Exception {
        if ("営業".equals(csvUser.getDepartment())) {
            throw new AppException("営業は登録できません");
        }
        return csvUser.toUser();
    }
}

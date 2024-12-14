package com.example.demo.batch.file2db.csv01;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;
import com.example.demo.core.exception.AppException;

@Component
public class UserImportProcessor implements ItemProcessor<CsvUser, Users> {

    @Override
    public Users process(@NonNull CsvUser csvUser) throws Exception {
        if ("営業".equals(csvUser.getDepartment())) {
            throw new AppException("営業は登録できません");
        }
        return csvUser.toUser();
    }
}

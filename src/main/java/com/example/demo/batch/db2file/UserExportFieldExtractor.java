package com.example.demo.batch.db2file;

import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.stereotype.Component;

import com.example.demo.common.entity.Users;

@Component
public class UserExportFieldExtractor implements FieldExtractor<Users> {

    @SuppressWarnings("null")
    @Override
    public Object[] extract(Users item) {
        return new Object[] {
                item.getId().toString(),
                item.getName(),
                item.getDepartment(),
                item.getCreatedAt()
        };
    }
    
}

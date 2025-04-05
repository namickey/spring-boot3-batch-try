package com.example.demo.batch.master.user.receive;

import java.time.LocalDate;
import com.example.demo.common.entity.Users;

import lombok.Data;

@Data
public class ImportUsersItem {

    private Integer id;
    private String name;
    private String department;
    private String createdAt;

    public Users toUser() {

        Users user = new Users();
        user.setId(id);
        user.setName(name);
        user.setDepartment(department);
        user.setCreatedAt(LocalDate.parse(createdAt));

        return user;
    }
}

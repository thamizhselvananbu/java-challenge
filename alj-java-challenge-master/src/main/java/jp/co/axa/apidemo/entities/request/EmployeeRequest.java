package jp.co.axa.apidemo.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) AXA. All Rights Reserved
 * User: Thamizh
 * Date: 2023/02/14
 * Time: 20:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    private long id;
    private String name;
    private int salary;
    private String department;
}

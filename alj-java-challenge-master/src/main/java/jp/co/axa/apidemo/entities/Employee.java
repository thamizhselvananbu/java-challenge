package jp.co.axa.apidemo.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name="EMPLOYEE")
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_NAME")
    @NonNull
    @NotEmpty
    private String name;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_SALARY")
    @NonNull
    private int salary;

    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    @NonNull
    @NotEmpty
    private String department;

}

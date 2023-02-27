package com.ringme.cms.model;

import com.ringme.cms.annotation.Email;
import com.ringme.cms.annotation.Name;
import com.ringme.cms.annotation.Password;
import com.ringme.cms.annotation.Phone;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Email
    private String email;
    @Column(name = "user_name")
    @NotNull(message = "Not empty")
    private String userName;
    @Column(name = "password")
    private String password;
    @Name
    @Column(name = "full_name")
    private String fullName;
    @Column
    private String address;
    @Column
    @Phone
    private String phone;
    @Column(name = "user_type")
    private String userType;
    @Column(name = "id_province")
    private Long idProvince;

    private boolean active;
}

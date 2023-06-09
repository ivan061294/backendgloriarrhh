package pe.com.centro.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.com.centro.domain.enumeration.Role;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private String employeeCode;

    private String email;

    private String employeeFirstName;

    private String employeeFatherFamilyName;

    private String employeeMotherFamilyName;

    private String societyCode;

    private String societyName;

    private String divisionCode;

    private String divisionName;

    private String physicalLocationCode;

    private String physicalLocationName;

    private Role role;

    private int roleCode;

    private String fullName;
}

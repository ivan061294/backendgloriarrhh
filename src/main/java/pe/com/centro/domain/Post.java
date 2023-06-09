package pe.com.centro.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Post {
    private String code;

    private String societyCode;

    private String societyName;

    private String divisionCode;

    private String divisionName;

    private Long managementId;

    private String managementName;

    private String organizationalUnitCode;

    private String organizationalUnitName;

    private String physicalLocationCode;

    private String physicalLocationName;

    private String costCenterCode;

    private String costCenterName;

    private String employeeCode;

    private String employeeFirstName;

    private String employeeFatherFamilyName;

    private String employeeMotherFamilyName;

    private String postTypeId;

    private String postTypeName;

    private Integer codRol;

    private Integer flgAvailable;
}

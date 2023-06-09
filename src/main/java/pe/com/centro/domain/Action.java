package pe.com.centro.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.com.centro.domain.enumeration.RequestType;
import pe.com.centro.domain.enumeration.RequestUserActionType;

import java.time.Instant;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Action {

    // This is only used when there's an approval
    private Long requestId;
    // This is only used when there's an approval
    private RequestType requestType;

    private String userEmployeeCode;

    private RequestUserActionType actionType;

    private String comment;

    private Instant date;

    private String employeeFirstName;

    private String employeeFatherFamilyName;

    private String employeeMotherFamilyName;
}

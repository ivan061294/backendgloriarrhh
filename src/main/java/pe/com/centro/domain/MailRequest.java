package pe.com.centro.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MailRequest {
    private String to;
    private String url;
    private String requiredPosition;
    private MailTemplate mailTamplete;
    private String employeeToReplace;
    private String numberofRequirements;
    private String destinationUserName;
    
}

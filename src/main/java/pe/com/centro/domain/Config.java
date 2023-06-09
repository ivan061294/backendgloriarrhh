package pe.com.centro.domain;

import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Config implements Serializable {
    
    private List<Configchildren> user;

    private List<Configchildren> userProfile;

    private List<Configchildren> society;

    private List<Configchildren> profile;
}

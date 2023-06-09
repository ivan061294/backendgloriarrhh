package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RequestUserActionType {
    CREATED(0),
    APPROVED(1),
    REJECTED(2),
    OBSERVED(3),
    RELEASED(4);

    @Getter
    private final int value;
}

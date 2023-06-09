package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ESearchType {
    INNER(0),
    OUTER(1),
    BOTH(2);

    @Getter
    private final int value;
}

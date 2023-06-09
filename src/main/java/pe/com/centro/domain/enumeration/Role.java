package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {
    REQUESTER(0),
    HUMAN_MANAGEMENT(1),
    GENERAL_MANAGER(2),
    CORPORATE_DIRECTORATE(3),
    ORGANIZATIONAL_EFFECTIVITY(4),
    CORPORATE_MANAGEMENT(5),
    PRESIDENCY(6),
    ADMINISTRATOR(7);

    @Getter
    private final int value;
}

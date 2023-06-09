package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RequestType {
    /**
     * New Planned Staff Requirement
     */
    NEW_PLANNED(0),
    /**
     * New Not Planned Staff Requirement
     */
    NEW_NOT_PLANNED(1),
    /**
     * Replacement Staff Requirement
     */
    REPLACEMENT(2);

    @Getter
    private final int value;
}

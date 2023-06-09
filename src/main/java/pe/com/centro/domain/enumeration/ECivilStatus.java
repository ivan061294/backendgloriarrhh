package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ECivilStatus {
    /**
     * Single civil status
     */
    SINGLE(0),
    /**
     * Married civil status
     */
    MARRIED(1),
    /**
     * Widowed civil status
     */
    WIDOWED(2),
    /**
     * Divorced civil status
     */
    DIVORCED(3),
    /**
     * Indifferent
     */
    INDIFFERENT(4);

    @Getter
    private final int value;
}

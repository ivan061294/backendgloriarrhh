package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EContractType {
    /**
     * Fixed Term Contract Type
     */
    FIXED_TERM(0),
    /**
     * Indeterminate Term Contract Type
     */
    INDETERMINATE_TERM(1),
    /**
     * Training Agreement Contract Type
     */
    TRAINING_AGREEMENT(2);

    @Getter
    private final int value;
}

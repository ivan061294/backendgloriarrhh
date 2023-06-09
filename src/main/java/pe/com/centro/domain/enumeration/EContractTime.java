package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EContractTime {
    /**
     * For Indeterminate Term Contract Type
     */
    NOT_DEFINED (0),
    /**
     * For Fixed Term Contract Type
     */
    TWO_MONTHS(1),
    /**
     * For Fixed Term Contract Type
     */
    THREE_MONTHS(2),
    /**
     * For Fixed Term Contract Type
     */
    SIX_MONTHS(3),
    /**
     * For Fixed Term Contract Type & Training Agreement
     */
    TWELVE_MONTHS(4),
    /**
     * For Fixed Term Contract Type
     */
    ONE_YEAR(5),
    /**
     * For Fixed Term Contract Type
     */
    THREE_YEARS(6);

    @Getter
    private final int value;
}

package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EAgeRange {
    /**
     * Until 25 years
     */
    UNTIL_TWENTY_FIVE(0),
    /**
     * Until 35 years
     */
    UNTIL_THIRTY_FIVE(1),
    /**
     * Until 45 years
     */
    UNTIL_FORTY_FIVE(2);

    @Getter
    private final int value;
}

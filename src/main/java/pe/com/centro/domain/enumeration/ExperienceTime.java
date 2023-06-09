package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExperienceTime {
    /**
     * 1 to 3 years.
     */
    ONE_THREE_YEARS(0),
    /**
     * 3 to 5 years.
     */
    THREE_FIVE_YEARS(1),
    /**
     * 5 to 10 years.
     */
    FIVE_TEN_YEARS(2),
    /**
     * 10 to 15 years.
     */
    TEN_FIFTEEN_YEARS(3),
    /**
     * more than 15 years.
     */
    MORE_FIFTEEN_YEARS(4);

    @Getter
    private final int value;
}

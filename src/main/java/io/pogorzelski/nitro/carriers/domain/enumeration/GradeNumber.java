package io.pogorzelski.nitro.carriers.domain.enumeration;

/**
 * The GradeNumber enumeration.
 */
public enum GradeNumber {

    DEF_YES(6), YES(5), FINE(4), NO(3), DEF_NO(2), BLACK_LIST(1);
    int value;

    GradeNumber(int value) {
        this.value = value;
    }}

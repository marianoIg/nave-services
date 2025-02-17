package com.mariano.spacecrafts;

import com.mariano.spacecrafts.core.domain.Spacecraft;

public class Fixture {
    public static Spacecraft getSpacecraft() {
        return new Spacecraft(
                1L,
                "Enterprise",
                "TOS",
                "Shuttle",
                100,
                200000.0
        );
    }
    public static Spacecraft getSpacecraft_not_valid() {
        return new Spacecraft(
                1L,
                "",
                "TOS",
                "Shuttle",
                100,
                200000.0
        );
    }
}

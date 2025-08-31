package dev.nitron.usm.saves;

import dev.nitron.usm.data.SSystem;

import java.awt.*;

public class USMAllSystems {
    public static void init(){}

    public static final SSystem ASTRYA = new SSystem.Builder()
            .canSeeSpaceGas(true)
            .spaceGasColor(new Color(153, 178, 255))
            .starDensity(100)
            .spaceGasPower(0.25F)
            .build();
}

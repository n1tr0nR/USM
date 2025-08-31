package dev.nitron.usm.data;

import java.awt.*;

public class SSystem {
    private final boolean canSeeSpaceGas;
    private final Color spaceGasColor;
    private final float spaceGasPower;
    private final float starDensity;

    private SSystem(Builder builder) {
        this.canSeeSpaceGas = builder.canSeeSpaceGas;
        this.spaceGasColor = builder.spaceGasColor;
        this.spaceGasPower = builder.spaceGasPower;
        this.starDensity = builder.starDensity;
    }

    public boolean isCanSeeSpaceGas(){
        return canSeeSpaceGas;
    }

    public Color getSpaceGasColor() {
        return spaceGasColor;
    }

    public float getSpaceGasPower() {
        return spaceGasPower;
    }

    public float getStarDensity(){
        return starDensity;
    }

    public static class Builder {
        private boolean canSeeSpaceGas = false;
        private Color spaceGasColor = Color.WHITE;
        private float spaceGasPower = 0.5f;
        private float starDensity = 1.0f;

        public Builder canSeeSpaceGas(boolean canSeeSpaceGas) {
            this.canSeeSpaceGas = canSeeSpaceGas;
            return this;
        }

        public Builder spaceGasColor(Color spaceGasColor) {
            this.spaceGasColor = spaceGasColor;
            return this;
        }

        public Builder spaceGasPower(float spaceGasPower) {
            this.spaceGasPower = spaceGasPower;
            return this;
        }

        public Builder starDensity(float starDensity) {
            this.starDensity = starDensity;
            return this;
        }

        public SSystem build() {
            return new SSystem(this);
        }
    }
}


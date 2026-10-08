package com.example.evcharging.strategy;

import com.example.evcharging.exception.BadRequestException;

import java.util.List;

public abstract class TieredTariffStrategy implements TariffStrategy {
    private final double minimumCharge;
    private final List<TariffSlab> slabs;

    protected TieredTariffStrategy(double minimumCharge, List<TariffSlab> slabs) {
        if (minimumCharge < 0.0) {
            throw new IllegalArgumentException("Minimum charge cannot be negative");
        }
        if (slabs == null || slabs.isEmpty()) {
            throw new IllegalArgumentException("At least one tariff slab is required");
        }
        this.minimumCharge = minimumCharge;
        this.slabs = List.copyOf(slabs);
    }

    @Override
    public double calculate(double energyKwh) {
        if (energyKwh < 0.0) {
            throw new BadRequestException("Energy delivered cannot be negative");
        }

        double remainingEnergy = energyKwh;
        double previousLimit = 0.0;
        double amount = 0.0;

        for (TariffSlab slab : slabs) {
            if (remainingEnergy <= 0.0) {
                break;
            }

            double slabSize = Math.min(remainingEnergy, slab.upToKwh() - previousLimit);
            if (slabSize > 0.0) {
                amount += slabSize * slab.ratePerKwh();
                remainingEnergy -= slabSize;
            }
            previousLimit = slab.upToKwh();
        }

        return Math.max(minimumCharge, amount);
    }
}

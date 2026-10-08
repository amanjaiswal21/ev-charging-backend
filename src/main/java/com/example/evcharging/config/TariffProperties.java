package com.example.evcharging.config;

import com.example.evcharging.strategy.TariffSlab;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Objects;

@ConfigurationProperties(prefix = "charging.tariffs")
public record TariffProperties(Tariff ac, Tariff dc) {
    public TariffProperties {
        Objects.requireNonNull(ac, "AC tariff configuration is required");
        Objects.requireNonNull(dc, "DC tariff configuration is required");
    }

    public record Tariff(Double minimumCharge, List<Slab> slabs) {
        public Tariff {
            Objects.requireNonNull(minimumCharge, "Minimum charge is required");
            if (!Double.isFinite(minimumCharge) || minimumCharge < 0.0) {
                throw new IllegalArgumentException("Minimum charge must be finite and non-negative");
            }
            if (slabs == null || slabs.isEmpty()) {
                throw new IllegalArgumentException("At least one tariff slab is required");
            }
            slabs = List.copyOf(slabs);
            double previousLimit = 0.0;
            for (int i = 0; i < slabs.size(); i++) {
                Double limit = slabs.get(i).upToKwh();
                if (i == slabs.size() - 1) {
                    if (limit != null) {
                        throw new IllegalArgumentException("Final tariff slab must omit up-to-kwh");
                    }
                } else {
                    if (limit == null || !Double.isFinite(limit) || limit <= previousLimit) {
                        throw new IllegalArgumentException("Tariff slab limits must be finite and strictly increasing");
                    }
                    previousLimit = limit;
                }
            }
        }

        public List<TariffSlab> toTariffSlabs() {
            return slabs.stream()
                    .map(slab -> new TariffSlab(
                            slab.upToKwh() == null ? Double.POSITIVE_INFINITY : slab.upToKwh(),
                            slab.ratePerKwh()))
                    .toList();
        }
    }

    public record Slab(Double upToKwh, Double ratePerKwh) {
        public Slab {
            Objects.requireNonNull(ratePerKwh, "Tariff slab rate is required");
            if (!Double.isFinite(ratePerKwh) || ratePerKwh < 0.0) {
                throw new IllegalArgumentException("Tariff slab rate must be finite and non-negative");
            }
        }
    }
}

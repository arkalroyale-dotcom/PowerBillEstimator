package com.arkal.powerbill;

import java.util.Locale;

/**
 * Pure calculation logic for the electricity bill (no Android dependencies).
 *
 * Block tariff:
 *   1   - 200 kWh  : 21.8 sen/kWh
 *   201 - 300 kWh  : 33.4 sen/kWh
 *   301 - 600 kWh  : 51.6 sen/kWh
 *   601 - 1000 kWh : 54.6 sen/kWh
 */
public final class BillCalculator {

    public static final int MIN_UNITS = 1;
    public static final int MAX_UNITS = 1000;
    public static final double MIN_REBATE = 0.0;
    public static final double MAX_REBATE = 5.0;

    /** Size of each block in kWh (the last block covers 601 - 1000). */
    private static final int[] BLOCK_SIZES = {200, 100, 300, 400};
    /** Rate of each block in RM per kWh. */
    private static final double[] BLOCK_RATES = {0.218, 0.334, 0.516, 0.546};
    private static final String[] BLOCK_NAMES = {
            "1-200", "201-300", "301-600", "601-1000"
    };

    private BillCalculator() {
    }

    public static boolean isValidUnits(int units) {
        return units >= MIN_UNITS && units <= MAX_UNITS;
    }

    public static boolean isValidRebate(double rebatePercent) {
        return rebatePercent >= MIN_REBATE && rebatePercent <= MAX_REBATE;
    }

    /** Total charges in RM before rebate. */
    public static double calculateTotalCharges(int units) {
        if (!isValidUnits(units)) {
            throw new IllegalArgumentException("Units must be between 1 and 1000");
        }
        double total = 0;
        int remaining = units;
        for (int i = 0; i < BLOCK_SIZES.length && remaining > 0; i++) {
            int used = Math.min(remaining, BLOCK_SIZES[i]);
            total += used * BLOCK_RATES[i];
            remaining -= used;
        }
        return total;
    }

    /** Final cost = Total charges - (Total charges x rebate %). */
    public static double calculateFinalCost(double totalCharges, double rebatePercent) {
        if (!isValidRebate(rebatePercent)) {
            throw new IllegalArgumentException("Rebate must be between 0% and 5%");
        }
        return totalCharges - (totalCharges * rebatePercent / 100.0);
    }

    /** Human-readable, block-by-block breakdown of the charges. */
    public static String buildBreakdown(int units) {
        StringBuilder sb = new StringBuilder();
        int remaining = units;
        for (int i = 0; i < BLOCK_SIZES.length && remaining > 0; i++) {
            int used = Math.min(remaining, BLOCK_SIZES[i]);
            double charge = used * BLOCK_RATES[i];
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(String.format(Locale.US, "%s kWh: %d x RM %.3f = RM %.3f",
                    BLOCK_NAMES[i], used, BLOCK_RATES[i], charge));
            remaining -= used;
        }
        return sb.toString();
    }

    /** Formats an amount as "RM 12.34". */
    public static String formatRM(double amount) {
        return String.format(Locale.US, "RM %.2f", amount);
    }
}

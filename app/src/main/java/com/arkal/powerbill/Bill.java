package com.arkal.powerbill;

/** One saved bill record. */
public class Bill {

    private long id;
    private int monthIndex;      // 0 = January ... 11 = December
    private String month;
    private int units;
    private double rebatePercent;
    private double totalCharges;
    private double finalCost;

    public Bill(long id, int monthIndex, String month, int units,
                double rebatePercent, double totalCharges, double finalCost) {
        this.id = id;
        this.monthIndex = monthIndex;
        this.month = month;
        this.units = units;
        this.rebatePercent = rebatePercent;
        this.totalCharges = totalCharges;
        this.finalCost = finalCost;
    }

    public Bill(int monthIndex, String month, int units,
                double rebatePercent, double totalCharges, double finalCost) {
        this(-1, monthIndex, month, units, rebatePercent, totalCharges, finalCost);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getMonthIndex() { return monthIndex; }
    public void setMonthIndex(int monthIndex) { this.monthIndex = monthIndex; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public double getRebatePercent() { return rebatePercent; }
    public void setRebatePercent(double rebatePercent) { this.rebatePercent = rebatePercent; }

    public double getTotalCharges() { return totalCharges; }
    public void setTotalCharges(double totalCharges) { this.totalCharges = totalCharges; }

    public double getFinalCost() { return finalCost; }
    public void setFinalCost(double finalCost) { this.finalCost = finalCost; }

    public double getRebateAmount() { return totalCharges - finalCost; }
}

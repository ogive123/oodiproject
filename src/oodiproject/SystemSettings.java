package oodiproject;

public class SystemSettings implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /*correct systemsettings to have
    fine rate too
    */
    private int maxLoanLimit;
    private int maxRenewals;
    private double fineThreshold;
    private int maxRenewalDuration;
    private double fineRate;

    public SystemSettings(){
        this.maxLoanLimit = 20;
        this.maxRenewals = 5;
        this.fineThreshold = 10;
        this.maxRenewalDuration = 14;
        this.fineRate = 0.25;
    }
    
    //maxLoanLimit
    public int getMaxLoanLimit() {
        return maxLoanLimit;
    }
    public void setMaxLoanLimit(int maxLoanLimit) {
        this.maxLoanLimit = maxLoanLimit;
    }

    //maxRenewals
    public int getMaxRenewals() {
        return maxRenewals;
    }
    public void setMaxRenewals(int maxRenewals) {
        this.maxRenewals = maxRenewals;
    }
    
    //fineThreshold
    public double getFineThreshold() {
        return fineThreshold;
    }
    public void setFineThreshold(double fineThreshold) {
        this.fineThreshold = fineThreshold;
    }
    
    //maxRenewalDuration
    public int getMaxRenewalDuration() {
        return maxRenewalDuration;
    }
    public void setMaxRenewalDuration(int maxRenewalDuration) {
        this.maxRenewalDuration = maxRenewalDuration;
    }
    
    //fineRate
    public double getFineRate() {
        return fineRate;
    }
    public void setFineRate(double fineRate) {
        this.fineRate = fineRate;
    }
}

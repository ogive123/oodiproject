package oodiproject;

import java.util.ArrayList;
import java.util.List;

public class Finances {

    /*finances is probably dependent on loanhistory too*/
    private double revenue = 0;
    private double expenditure = 0;
    
    //revenue
    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    
    //expenditure
    public double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }
    
}

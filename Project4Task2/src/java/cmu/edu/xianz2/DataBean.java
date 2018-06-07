/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.xianz2;

/**
 *
 * @author zhangxian
 */
public class DataBean {
    
    private int serNo;
    private String userType;
    private String userIP;
    private String baseCurrency;
    private String searchCurrency;
    private String rates;
    private String lastAccessed;

    public DataBean() {}
    
    public DataBean(
            int serNo1,
            String userType1,
            String userIP1,
            String baseCurrency1,
            String searchCurrency1,
            String rates1,
            String lastAccessed1){
        serNo =serNo1;
        userType = userType1;
        userIP =userIP1;
        baseCurrency = baseCurrency1;
        searchCurrency = searchCurrency1;
        rates = rates1;
        lastAccessed = lastAccessed1;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void SetserNo(int serNo) {
        this.serNo = serNo;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setSearchCurrency(String searchCurrency) {
        this.searchCurrency = searchCurrency;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public void setLastAccessed(String lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public int getSerNo() {
        return serNo;
    }
        
    public String getUserType() {
        return userType;
    }

    public String getUserIP() {
        return userIP;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getSearchCurrency() {
        return searchCurrency;
    }

    public String getRates() {
        return rates;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }
    
    
}

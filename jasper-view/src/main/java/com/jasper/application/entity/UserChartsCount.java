package com.jasper.application.entity;

/**
 * @author misterWei
 * @create 2019年11月29号:22点37分
 * @mailbox mynameisweiyan@gmail.com
 */
public class UserChartsCount {
    private String companyName;
    private Long companyCount;

    public UserChartsCount(String companyName, Long companyCount) {
        this.companyName = companyName;
        this.companyCount = companyCount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(Long companyCount) {
        this.companyCount = companyCount;
    }
}

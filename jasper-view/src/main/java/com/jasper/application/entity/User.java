package com.jasper.application.entity;

/**
 * @author misterWei
 * @create 2019年11月29号:21点03分
 * @mailbox mynameisweiyan@gmail.com
 */
public class User {

    private Integer id;
    private String userName;
    private String dep;
    private String company;
    private String gunba;

    public User(Integer id, String userName, String dep, String company, String gunba) {
        this.id = id;
        this.userName = userName;
        this.dep = dep;
        this.company = company;
        this.gunba = gunba;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGunba() {
        return gunba;
    }

    public void setGunba(String gunba) {
        this.gunba = gunba;
    }
}

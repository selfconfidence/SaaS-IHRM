package com.weiyan.actvariable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actvariable
 * @date 2019/7/23 15:31
 *
 * 业务系统请假单数据映射实体类
 * 如果在Aciviti中的流程变量使用到 实体类属性,必须序列化该对象.
 */
public class HolidayEntity implements Serializable {

    private String id;
    private String holidayName;
    private String orgName;
    private Date startDate;
    private Date endDate;
    private Float holidayNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getHolidayNum() {
        return holidayNum;
    }

    public void setHolidayNum(Float holidayNum) {
        this.holidayNum = holidayNum;
    }
}

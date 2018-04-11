package com.ebiz.to;

/**
 * Created by Rajesh Kumar on 04-Feb-17.
 */

public class EnqListTO {
    public String studName;
    public String enqDate;
    public String mobileNo;
    public String courseName;
    public String studId;


    public String keyId;

    public EnqListTO(String studName, String enqDate, String mobileNo, String courseName, String studId,String keyId) {
        this.studName = studName;
        this.enqDate = enqDate;
        this.mobileNo = mobileNo;
        this.courseName = courseName;
        this.studId = studId;
        this.keyId = keyId;
    }

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    public String getEnqDate() {
        return enqDate;
    }

    public void setEnqDate(String enqDate) {
        this.enqDate = enqDate;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

}

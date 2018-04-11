package com.ebiz.to;

import java.util.Date;

/**
 * Created by KARTHI on 06-12-2016.
 */

public class StudentEnquiryTO {

    private int locId;
    private int studentId;
    private String mobileNo;
    private String name;
    private String emailId;
    private String courseName;
    private int courseId;
    private Date dateOfBirth;
    private String dateOfBirthDisp;
    private Date enquiryDt;
    private String enquiryDtDisp;
    private String gender;
    private String sourceOfEnq;
    private int sourceOfEnqId;


    private String doNotCall;



    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirthDisp() {
        return dateOfBirthDisp;
    }

    public void setDateOfBirthDisp(String dateOfBirthDisp) {
        this.dateOfBirthDisp = dateOfBirthDisp;
    }

    public Date getEnquiryDt() {
        return enquiryDt;
    }

    public void setEnquiryDt(Date enquiryDt) {
        this.enquiryDt = enquiryDt;
    }

    public String getEnquiryDtDisp() {
        return enquiryDtDisp;
    }

    public void setEnquiryDtDisp(String enquiryDtDisp) {
        this.enquiryDtDisp = enquiryDtDisp;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSourceOfEnq() {
        return sourceOfEnq;
    }

    public void setSourceOfEnq(String sourceOfEnq) {
        this.sourceOfEnq = sourceOfEnq;
    }

    public int getSourceOfEnqId() {
        return sourceOfEnqId;
    }

    public void setSourceOfEnqId(int sourceOfEnqId) {
        this.sourceOfEnqId = sourceOfEnqId;
    }

    public String getDoNotCall() {  return doNotCall; }

    public void setDoNotCall(String doNotCall) { this.doNotCall = doNotCall;}


}

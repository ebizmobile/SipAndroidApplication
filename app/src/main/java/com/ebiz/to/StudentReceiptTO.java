package com.ebiz.to;

/**
 * Created by Rajesh Kumar on 05-Feb-17.
 */

public class StudentReceiptTO {
    private String key_id;
    private String loc_id;
    private String student_name;

    private String student_id;
    private String sync_status;
    private String invoiceId;
    private String date;
    private String amount;
    private String balAmt;
    private String paid;
    private String paidStatus;
    private String mobileNo;
    private String month;
    private String invoiceNo;
    private String year;

    private String monthYear;
    private String dueDt;

    public StudentReceiptTO(String key_id, String loc_id, String student_name, String student_id, String sync_status, String invoiceId, String date, String amount, String balAmt, String paid, String paidStatus, String mobileNo, String month, String invoiceNo, String year, String monthYear, String dueDt) {
        this.key_id = key_id;
        this.loc_id = loc_id;
        this.student_name = student_name;
        this.student_id = student_id;
        this.sync_status = sync_status;
        this.invoiceId = invoiceId;
        this.date = date;
        this.amount = amount;
        this.balAmt = balAmt;
        this.paid = paid;
        this.paidStatus = paidStatus;
        this.mobileNo = mobileNo;
        this.month = month;
        this.invoiceNo = invoiceNo;
        this.year = year;
        this.monthYear = monthYear;
        this.dueDt = dueDt;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(String balAmt) {
        this.balAmt = balAmt;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getDueDt() {
        return dueDt;
    }

    public void setDueDt(String dueDt) {
        this.dueDt = dueDt;
    }
}

package model;

import util.Constant;

/**
 * Created by wpa6 on 29/8/16.
 */

public class CasesListClass {

    String id = "";
    String ContactNo = "";
    String Bank = "";
    String RefNo = "";
    String Address = "";
    String ApplicantName = "";

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String applicantName) {
        ApplicantName = applicantName;
    }
}

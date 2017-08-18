package model;

/**
 * Created by Pranav on 29/8/16.
 */

public class DashBoardListClass {


    String Status = "";
    String Lable = "";
    String CaseCount = "";

    public String getLable() {
        return Lable;
    }

    public void setLable(String lable) {
        Lable = lable;
    }



    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCaseCount() {
        return CaseCount;
    }

    public void setCaseCount(String caseCount) {
        CaseCount = caseCount;
    }
}

package com.smallworld.data;

import java.util.List;

public class Transaction {

    private String mtn;
    private Double amount;
    private String senderFullName;
    private Integer senderAge;
    private String beneficiaryFullName;
    private Integer beneficiaryAge;
    private List<Issue> issuesList;

    public String getMtn() {
        return mtn;
    }

    public void setMtn(String mtn) {
        this.mtn = mtn;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public Integer getSenderAge() {
        return senderAge;
    }

    public void setSenderAge(Integer senderAge) {
        this.senderAge = senderAge;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public void setBeneficiaryFullName(String beneficiaryFullName) {
        this.beneficiaryFullName = beneficiaryFullName;
    }

    public Integer getBeneficiaryAge() {
        return beneficiaryAge;
    }

    public void setBeneficiaryAge(Integer beneficiaryAge) {
        this.beneficiaryAge = beneficiaryAge;
    }

    public List<Issue> getIssuesList() {
        return issuesList;
    }

    public void setIssuesList(List<Issue> issuesList) {
        this.issuesList = issuesList;
    }
}

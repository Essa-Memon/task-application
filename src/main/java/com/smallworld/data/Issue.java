package com.smallworld.data;

public class Issue {

    private Long issueId;
    private Boolean issueSolved;
    private String issueMessage;

    public Issue(Long issueId, Boolean issueSolved, String issueMessage) {
        this.issueId = issueId;
        this.issueSolved = issueSolved;
        this.issueMessage = issueMessage;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Boolean getIssueSolved() {
        return issueSolved;
    }

    public void setIssueSolved(Boolean issueSolved) {
        this.issueSolved = issueSolved;
    }

    public String getIssueMessage() {
        return issueMessage;
    }

    public void setIssueMessage(String issueMessage) {
        this.issueMessage = issueMessage;
    }
}

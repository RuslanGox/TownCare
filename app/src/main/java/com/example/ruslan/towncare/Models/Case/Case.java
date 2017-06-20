package com.example.ruslan.towncare.Models.Case;

/**
 * Created by omrih on 27-May-17.
 */

public class Case {
    private String caseId;
    private String caseTitle;
    private String caseDate;
    private int caseLikeCount;
    private String caseType;
    private String caseStatus;
    private String caseOpenerPhone;
    private String caseOpenerId;
    private String caseTown;
    private String caseAddress;
    private String caseDesc;
    private String caseImageUrl;

    public Case() {
    }

    public Case(String caseId, String caseTitle, String caseDate, int caseLikeCount, String caseType, String caseStatus, String caseOpenerPhone, String caseOpenerId, String caseTown, String caseAddress, String caseDesc, String caseImageUrl) {
        this.caseId = caseId;
        this.caseTitle = caseTitle;
        this.caseDate = caseDate;
        this.caseLikeCount = caseLikeCount;
        this.caseType = caseType;
        this.caseStatus = caseStatus;
        this.caseOpenerPhone = caseOpenerPhone;
        this.caseOpenerId = caseOpenerId;
        this.caseTown = caseTown;
        this.caseAddress = caseAddress;
        this.caseDesc = caseDesc;
        this.caseImageUrl = caseImageUrl;
    }


    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public int getCaseLikeCount() {
        return caseLikeCount;
    }

    public void setCaseLikeCount(int caseLikeCount) {
        this.caseLikeCount = caseLikeCount;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseOpenerPhone() {
        return caseOpenerPhone;
    }

    public void setCaseOpenerPhone(String caseOpenerPhone) {
        this.caseOpenerPhone = caseOpenerPhone;
    }

    public String getCaseImageUrl() {
        return caseImageUrl;
    }

    public void setCaseImageUrl(String caseImageUrl) {
        this.caseImageUrl = caseImageUrl;
    }

    public String getCaseOpenerId() {
        return caseOpenerId;
    }

    public void setCaseOpenerId(String caseOpenerId) {
        this.caseOpenerId = caseOpenerId;
    }

    public String getCaseTown() {
        return caseTown;
    }

    public void setCaseTown(String caseTown) {
        this.caseTown = caseTown;
    }

    public String getCaseAddress() {
        return caseAddress;
    }

    public void setCaseAddress(String caseAddress) {
        this.caseAddress = caseAddress;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }
}

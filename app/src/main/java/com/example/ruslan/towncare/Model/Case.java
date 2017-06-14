package com.example.ruslan.towncare.Model;

/**
 * Created by omrih on 27-May-17.
 */

public class Case {
    private String caseId;
    private String caseTitle;
    private String caseDate;
    private int caseLikeCount;
    private int caseUnLikeCount;
    private String caseType;
    private String caseStatus;
    private String caseOpenerPhone;
    private int caseOpener;
    private String caseAddress;
    private String caseDesc;

    private String caseImageUrl;

//    public Case(int caseId, String caseTitle, String caseDate, int caseLikeCount, int caseUnLikeCount, String caseType, String caseStatus, String caseOpenerPhone, int caseOpener, String caseAddress, String caseDesc, String caseImageUrl) {
//        this.caseId = caseId;
//        this.caseTitle = caseTitle;
//        this.caseDate = caseDate;
//        this.caseLikeCount = caseLikeCount;
//        this.caseUnLikeCount = caseUnLikeCount;
//        this.caseType = caseType;
//        this.caseStatus = caseStatus;
//        this.caseOpenerPhone = caseOpenerPhone;
//        this.caseOpener = caseOpener;
//        this.caseAddress = caseAddress;
//        this.caseDesc = caseDesc;
//        this.caseImageUrl = caseImageUrl;
//    }

    public Case(String caseId, String caseTitle, String caseDate, int caseLikeCount, int caseUnLikeCount, String caseType, String caseStatus, String caseOpenerPhone, int caseOpener, String caseAddress, String caseDesc, String caseImageUrl) {
        this.caseId = caseId;
        this.caseTitle = caseTitle;
        this.caseDate = caseDate;
        this.caseLikeCount = caseLikeCount;
        this.caseUnLikeCount = caseUnLikeCount;
        this.caseType = caseType;
        this.caseStatus = caseStatus;
        this.caseOpenerPhone = caseOpenerPhone;
        this.caseOpener = caseOpener;
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

    public int getCaseUnLikeCount() {
        return caseUnLikeCount;
    }

    public void setCaseUnLikeCount(int caseUnLikeCount) {
        this.caseUnLikeCount = caseUnLikeCount;
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

    public int getCaseOpener() {
        return caseOpener;
    }

    public void setCaseOpener(int caseOpener) {
        this.caseOpener = caseOpener;
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

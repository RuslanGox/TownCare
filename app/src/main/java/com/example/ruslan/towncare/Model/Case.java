package com.example.ruslan.towncare.Model;

/**
 * Created by omrih on 27-May-17.
 */

public class Case {
    private int caseId;
    private String caseTitle;
    private String caseDate;
    private int caseLikeCount;
    private int caseUnLikeCount;
    private String caseType;
    private String caseStatus;
    private String caseMessage;
    private String caseOpenerPhone;
    private String caseImageUrl;

    public Case(String caseTitle, String caseDate, int caseLikeCount, int caseUnLikeCount, String caseType, String caseStatus, String caseMessage, String caseOpenerPhone, String caseImageUrl, int caseOpener) {
        this.caseTitle = caseTitle;
        this.caseDate = caseDate;
        this.caseLikeCount = caseLikeCount;
        this.caseUnLikeCount = caseUnLikeCount;
        this.caseType = caseType;
        this.caseStatus = caseStatus;
        this.caseMessage = caseMessage;
        this.caseOpenerPhone = caseOpenerPhone;
        this.caseImageUrl = caseImageUrl;
        this.caseOpener = caseOpener;
//        this.caseId = TODO
    }

    public Case(String caseTitle, String caseDate, int caseLikeCount, int caseUnLikeCount, String caseType, String caseStatus, String caseImageUrl) {
        this.caseTitle = caseTitle;
        this.caseDate = caseDate;
        this.caseLikeCount = caseLikeCount;
        this.caseUnLikeCount = caseUnLikeCount;
        this.caseType = caseType;
        this.caseStatus = caseStatus;
        this.caseImageUrl = caseImageUrl;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
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

    public String getCaseMessage() {
        return caseMessage;
    }

    public void setCaseMessage(String caseMessage) {
        this.caseMessage = caseMessage;
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

    private int caseOpener;


}

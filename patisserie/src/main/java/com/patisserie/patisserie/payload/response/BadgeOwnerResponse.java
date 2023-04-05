package com.patisserie.patisserie.payload.response;

public class BadgeOwnerResponse {
    private String clientTelNumber;
    private String clientName;
    private String badgeName;
    private Integer discountPercentage;

    public String getClientTelNumber() {
        return clientTelNumber;
    }

    public void setClientTelNumber(String clientTelNumber) {
        this.clientTelNumber = clientTelNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}

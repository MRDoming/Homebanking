package com.mindhub.homebanking.dtos;

public class PaymentsApplicationDTO {

    public String number;

    public Integer cvv;

    public Double amount;

    public String description;

    public PaymentsApplicationDTO(){};

    public PaymentsApplicationDTO(String number, Integer cvv, Double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double monto) {
        this.amount = monto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PaymentsApplicationDTO{" +
                "number='" + number + '\'' +
                ", cvv=" + cvv +
                ", monto=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}

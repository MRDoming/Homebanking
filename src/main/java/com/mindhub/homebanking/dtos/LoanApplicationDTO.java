package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private long id;

    private Double amount;

    private Integer payments;

    private String account;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(Long id, Double amount, Integer payments, String account) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "LoanApplicationDTO{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", payments=" + payments +
                ", account='" + account + '\'' +
                '}';
    }
}

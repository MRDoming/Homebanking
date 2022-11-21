package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private Long loanId;
    private String name;
    private Double amount;
    private Integer payments;

    private String status;

    private String cuenta;

    public ClientLoanDTO(){};
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getNameLoan();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.status = clientLoan.getStatus();
        this.cuenta = clientLoan.getCuenta();
    }

    public long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String loan) {
        this.name = loan;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }


    @Override
    public String toString() {
        return "ClientLoanDTO{" +
                "id=" + id +
                ", loanId=" + loanId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", payments=" + payments +
                '}';
    }
}

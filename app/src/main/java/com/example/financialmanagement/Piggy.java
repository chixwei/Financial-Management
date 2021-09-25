package com.example.financialmanagement;

public class Piggy {

    private double expense_amount;
    private String user_id, expense_category_name, expense_memo;

    public Piggy() {
    }

    public double getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(double expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExpense_category_name() {
        return expense_category_name;
    }

    public void setExpense_category_name(String expense_category_name) {
        this.expense_category_name = expense_category_name;
    }

    public String getExpense_memo() {
        return expense_memo;
    }

    public void setExpense_memo(String expense_memo) {
        this.expense_memo = expense_memo;
    }
}

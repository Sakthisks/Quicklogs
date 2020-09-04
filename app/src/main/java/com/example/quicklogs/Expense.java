//expense class to get the values from the object
package com.example.quicklogs;

class Expense {
    private String description;
    private String type;
    private float amount;
    private String date;

    Expense (String description, String type, String date, float amount) {
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    float getAmount() {
        return amount;
    }

    String getDate() {
        return date;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setType(String type) {
        this.type = type;
    }

    void setAmount(float amount) {
        this.amount = amount;
    }

    void setDate(String date) {
        this.date = date;
    }
}

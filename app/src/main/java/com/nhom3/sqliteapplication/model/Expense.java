package com.nhom3.sqliteapplication.model;

public class Expense {
    private int id;
    private String name ;
    private String content;
    private Integer money;
    private String day ;

    public Expense() {
    }

    public Expense(int id, String name, String content, String day, Integer money) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.day = day;
        this.money = money;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

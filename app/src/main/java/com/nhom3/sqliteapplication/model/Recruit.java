package com.nhom3.sqliteapplication.model;

public class Recruit {
        private int recruitid;
        private int id;
        private String title;
        private String content;

        public Recruit(){

        }
        public Recruit(int recruitid, int id, String title, String content){
            this.recruitid=recruitid;
            this.id=id;
            this.title=title;
            this.content=content;
        }

    public int getRecruitid() {
        return recruitid;
    }

    public void setRecruitid(int recruitid) {
        this.recruitid = recruitid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

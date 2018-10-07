package com.example.capdtalk.etc;

public class Comment {
    private String wname,wdate,ccontent,cname,cdate;

    public Comment(String wname, String wdate, String ccontent, String cname, String cdate) {
        this.wname = wname;
        this.wdate = wdate;
        this.ccontent = ccontent;
        this.cname = cname;
        this.cdate = cdate;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getWdate() {
        return wdate;
    }

    public void setWdate(String wdate) {
        this.wdate = wdate;
    }

    public String getCcontent() {
        return ccontent;
    }

    public void setCcontent(String ccontent) {
        this.ccontent = ccontent;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }
}

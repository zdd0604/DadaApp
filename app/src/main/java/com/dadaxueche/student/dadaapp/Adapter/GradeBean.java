package com.dadaxueche.student.dadaapp.Adapter;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-09-22
 * Time: 10:47
 */
public class GradeBean {
    private String title;
    private String begintime;
    private String usetime;
    private String grade;
    private String evaluate;
    /**
     * 标识是否可以删除
     */
    public boolean canRemove;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    public GradeBean(String title, boolean canRemove) {
        this.title = title;
        this.canRemove = canRemove;
    }

    public GradeBean() {
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getUsetime() {
        return usetime;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
}
package com.example.ugochukwu.hyperspender;

/**
 * Created by ugochukwu on 5/30/2015.
 */
public class NavDrawerItem {
    private String title;
    private int icon;
    public NavDrawerItem() {
    }

    public NavDrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
    public NavDrawerItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}

package com.cpsdna.app.mykotlinapplication.module;

public class HomeModuleBean {

    //0：学习新时代 1：法制初心 牢记使命 2：新媒体矩阵 3：人民法院报 4：网站集群 5：人民司法 6：司法公开 7：其它
    public String type;
    public String title;
    public String imageUrl;
    public String jumpUrl;
    public Boolean click;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public Boolean getClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }
}
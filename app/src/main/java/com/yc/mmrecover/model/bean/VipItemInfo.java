package com.yc.mmrecover.model.bean;

/**
 * Created by suns  on 2019/12/14 09:28.
 */
public class VipItemInfo {


    /**
     * id : 54
     * app_id : 16
     * name : 金牌会员
     * valid_second : 7776000
     * market_price : 99.00
     * price : 69.00
     * add_time : 1576235251
     * desp : 还原最近半年的聊天记录
     * img : /uploads/images/Card/20191213/f0004b8246743d9dc24f1dd8405d6a49.jpg
     * privilege_img : /uploads/images/Card/20191213/9d72e81e15532489cb4f5ddbaf33534a.jpg
     * limit_type : all
     * limit_type_val :
     * is_open : 0
     * is_del : 0
     */

    private int id;
    private int app_id;
    private String name;
    private int valid_second;
    private String market_price;
    private String price;
    private int add_time;
    private String desp;
    private String img;
    private String privilege_img;
    private String limit_type;
    private String limit_type_val;
    private int is_open;
    private int is_del;

    private int level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValid_second() {
        return valid_second;
    }

    public void setValid_second(int valid_second) {
        this.valid_second = valid_second;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrivilege_img() {
        return privilege_img;
    }

    public void setPrivilege_img(String privilege_img) {
        this.privilege_img = privilege_img;
    }

    public String getLimit_type() {
        return limit_type;
    }

    public void setLimit_type(String limit_type) {
        this.limit_type = limit_type;
    }

    public String getLimit_type_val() {
        return limit_type_val;
    }

    public void setLimit_type_val(String limit_type_val) {
        this.limit_type_val = limit_type_val;
    }

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

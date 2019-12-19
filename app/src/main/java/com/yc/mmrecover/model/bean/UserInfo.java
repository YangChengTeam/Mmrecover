package com.yc.mmrecover.model.bean;

/**
 * Created by suns  on 2019/12/12 12:03.
 */
public class UserInfo {


    /**
     * id : 2
     * name :
     * nickname : 0ds9iw7a1576218846
     * pwd :
     * imeil : hjgf423421
     * mobile :
     * app_id : 16
     * agent_id : 1
     * agent_pid : 0
     * device_type : android
     * sv :
     * login_time : 1576224951
     * login_date : 2019-12-13
     * login_ip : 61.183.177.210
     * reg_ip : 61.183.177.210
     * reg_time : 1576218846
     * reg_date : 2019-12-13
     * status : 0
     * soft_id : 0
     */

    private String id;

    private String app_type;
    private String app_id;
    private String ip;
    private String name;
    private String nickname;
    private String pwd;
    private String imeil;
    private String mobile;

    private int agent_id;
    private int agent_pid;
    private String device_type;
    private String sv;
    private int login_time;
    private String login_date;
    private String login_ip;
    private String reg_ip;
    private int reg_time;
    private String reg_date;
    private int status;
    private int soft_id;

    private int isVip; //0不是会员 1:低级别会员 2:高级别会员

    private String wx;
    private String qq;

    private String vip_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getImeil() {
        return imeil;
    }

    public void setImeil(String imeil) {
        this.imeil = imeil;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getAgent_pid() {
        return agent_pid;
    }

    public void setAgent_pid(int agent_pid) {
        this.agent_pid = agent_pid;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getSv() {
        return sv;
    }

    public void setSv(String sv) {
        this.sv = sv;
    }

    public int getLogin_time() {
        return login_time;
    }

    public void setLogin_time(int login_time) {
        this.login_time = login_time;
    }

    public String getLogin_date() {
        return login_date;
    }

    public void setLogin_date(String login_date) {
        this.login_date = login_date;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getReg_ip() {
        return reg_ip;
    }

    public void setReg_ip(String reg_ip) {
        this.reg_ip = reg_ip;
    }

    public int getReg_time() {
        return reg_time;
    }

    public void setReg_time(int reg_time) {
        this.reg_time = reg_time;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSoft_id() {
        return soft_id;
    }

    public void setSoft_id(int soft_id) {
        this.soft_id = soft_id;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }
}

package com.yc.mmrecover.model.bean;

/**
 * Created by caokun on 2019/11/14 14:03.
 */

public class ObjResponseVo {

    /**
     * obj : /upload/amr/msg_470917111419af041c64955103.mp3
     * _time : 1573711268871
     * _sign : F718681E7BCC3C2D31F4CBE2869CCD0F
     * msg : 操作成功
     * success : 1
     */

    public String obj;
    public long _time;
    public String _sign;
    public String msg;
    public String success;


    /*public class ObjResponseVo<T> {
        private String msg;
        private T obj;
        private String success;

        public ObjResponseVo(String str, String str2, T t) {
            this.success = str;
            this.msg = str2;
            this.obj = t;
        }

        public T getObj() {
            return this.obj;
        }
    }*/
}

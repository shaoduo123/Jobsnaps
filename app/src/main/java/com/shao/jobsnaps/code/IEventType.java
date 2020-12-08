package com.shao.jobsnaps.code;

/**
 *  事件类型
 * Created by shaoduo on 2017-07-19.
 */

public interface IEventType {

    public static final byte EVENT_REGIST_SUCCESS = 0X00 ;
    public static final byte EVENT_REGIST_FAIL = 0X01 ;
    public static final byte EVENT_LOGIN_SUCCESS = 0X02 ;
    public static final byte EVENT_LOGIN_FAIL = 0x03;
    public static final byte EVENT_LOGOUT_SUCCESS = 0x04;
    public static final byte EVENT_LOGOUT_FAIL = 0x05;
    public static final byte EVENT_ADD_PROJCET_SUCCESS = 0x06 ;
    public static final byte EVENT_ADD_PROJCET_FAIL = 0x07 ;
    public static final byte EVENT_REQUEST_TAKEPHOTO = 0x08 ;
    public static final byte EVENT_REQUEST_TAKEPHOTO_SUCCESS=0x09 ;
    public static final byte EVENT_REQUEST_TAKEPHOTO_FAIL=0x0A ;
    public static final byte EVENT_REQUEST_ALBUM = 0x0B ;
    public static final byte EVENT_REQUEST_ALBUM_SUCCESS = 0x0C ;
    public static final byte EVENT_REQUEST_ALBUM_FAIL= 0x0D ;
    public static final byte EVENT_REQUEST_SELECT_PIC_KITKAT_ALBUM = 0x0E ;
    public static final byte EVENT_REQUEST_SELECT_FILE = 0x0F ;

    public static final byte EVENT_COPY = 0x010 ;
    public static final byte EVENT_CUT = 0x012 ;
    public static final byte EVENT_MOVE = 0x013 ;
    public static final byte EVENT_PASE_SUCCESS = 0x014 ;
    public static final byte EVENT_SHOW_IMG =0x015 ;

    //int REQUESTCODE_FROM_ACTIVITY = 1000;
    //   public static final byte EVENT_COMFIRM_ORDER_SUCCESS = 0x06;
//    public static final byte EVENT_COMFIRM_ORDER_FAIL = 0x07;
   // public static final byte EVENT_PAY_ORDER_SUCCESS = 0x08;
  //  public static final byte EVENT_PAY_ORDER_FAIL = 0x09;
   // public static final byte EVENT_CHARGE_SUCCESS = 0x0A;
/*    public static final byte EVENT_CHARGE_FAIL = 0x0B;
    public static final byte EVENT_REMARK_ORDER_SUCCESS = 0x0C;
    public static final byte EVENT_REMARK_ORDER_FAIL = 0x0D;*/
    //public static final byte EVENT_CHANGE_MAIN_PAGER_ITEM = 0x0E;
/*    public static final byte EVENT_REQUEST_LOCATION = 0x0F;
    public static final byte EVENT_INTERNET_CONNECTED = 0x10;
    public static final byte EVENT_INTERNET_DISCONNECT = 0x11;
    public static final byte EVENT_UPDATE_SLIPS = 0x12;*/

}



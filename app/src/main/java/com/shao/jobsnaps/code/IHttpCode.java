package com.shao.jobsnaps.code;

public interface IHttpCode {
	public static final byte HTTP_INTERNET_ERROR = 0x00;
	public static final byte HTTP_INVAILD_CHECKSUM = 0x01;
	public static final byte HTTP_INVAILD_UID = 0x02;
	public static final byte HTTP_UID_OUT_OF_DATE = 0x03;

	public static final byte HTTP_REGIST_SUCCESS = 0x04;
	public static final byte HTTP_USERNAME_EXISTS = 0x05;

	public static final byte HTTP_LOGIN_SUCCESS = 0x06;
	public static final byte HTTP_ALREADY_ONLINE = 0x07;
	public static final byte HTTP_INVAILD_PASSWORD = 0x08;
	public static final byte HTTP_USERNAME_NOT_EXISTS = 0x09;

	public static final byte HTTP_LOGOUT_SUCCESS = 0x0A;

	public static final byte HTTP_GET_USER_SUCCESS = 0x0B;
	public static final byte HTTP_GET_RECIPIENT_SUCCESS = 0x0C;
	public static final byte HTTP_MODIFY_SUCCESS = 0x0D;

	public static final byte HTTP_LIST_ORDER_SUCCESS = 0x0E;
	public static final byte HTTP_CREATE_ORDER_SUCCESS = 0x0F;
	public static final byte HTTP_DETAIL_ORDER_SUCCESS = 0x10;
	public static final byte HTTP_CANCLE_ORDER_SUCCESS = 0x11;

	public static final byte HTTP_PAY_SUCCESS = 0x12;
	public static final byte HTTP_CHARGE_SUCCESS = 0x13;
	public static final byte HTTP_INSUFFICIENT_BALANCE = 0x14;

	public static final byte HTTP_REMARK_SUCCESS = 0x15;
}

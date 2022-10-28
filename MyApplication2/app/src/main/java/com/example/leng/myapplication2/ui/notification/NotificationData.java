package com.example.leng.myapplication2.ui.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ming.weim@ymm56.com on 2018/10/8.
 */

public class NotificationData implements Parcelable {
    public static final String NOTIFICATION_TYPE_ORDER = "notification_type_order";
    public static final String NOTIFICATION_TYPE_CARGO = "notification_type_cargo";
    public static final String NOTIFICATION_TYPE_ORDER_NAV = "notification_type_order_nav";

    private String id;

    private String type;

    private String content;

    private long createTime;

    private String mobile;

    private String chatFromUser;

    private String cargoStartCity;

    private String cargoDestinationCity;

    private int conversationType;

    private boolean resetNotificationFlag;

    private long ts;
    private  String orderId;
    private long expireAt;
    private String message;
    private boolean navigable;
    private String clickJumpUrl;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public NotificationData() {
    }

    public String getChatFromUser() {
        return chatFromUser;
    }

    public void setChatFromUser(String chatFromUser) {
        this.chatFromUser = chatFromUser;
    }

    public String getCargoStartCity() {
        return cargoStartCity;
    }

    public void setCargoStartCity(String cargoStartCity) {
        this.cargoStartCity = cargoStartCity;
    }

    public String getCargoDestinationCity() {
        return cargoDestinationCity;
    }

    public void setCargoDestinationCity(String cargoDestinationCity) {
        this.cargoDestinationCity = cargoDestinationCity;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNavigable() {
        return navigable;
    }

    public void setNavigable(boolean navigable) {
        this.navigable = navigable;
    }

    public String getClickJumpUrl() {
        return clickJumpUrl;
    }

    public void setClickJumpUrl(String clickJumpUrl) {
        this.clickJumpUrl = clickJumpUrl;
    }

    public boolean getResetNotificationFlag() {
        return resetNotificationFlag;
    }

    public void setResetNotificationFlag(boolean resetNotificationFlag) {
        this.resetNotificationFlag = resetNotificationFlag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeLong(this.createTime);
        dest.writeString(this.mobile);
        dest.writeString(this.chatFromUser);
        dest.writeString(this.cargoStartCity);
        dest.writeString(this.cargoDestinationCity);
        dest.writeInt(this.conversationType);
        dest.writeByte(this.resetNotificationFlag ? (byte) 1 : (byte) 0);
        dest.writeLong(this.ts);
        dest.writeString(this.orderId);
        dest.writeLong(this.expireAt);
        dest.writeString(this.message);
        dest.writeByte(this.navigable ? (byte) 1 : (byte) 0);
        dest.writeString(this.clickJumpUrl);
    }

    protected NotificationData(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.createTime = in.readLong();
        this.mobile = in.readString();
        this.chatFromUser = in.readString();
        this.cargoStartCity = in.readString();
        this.cargoDestinationCity = in.readString();
        this.conversationType = in.readInt();
        this.resetNotificationFlag = in.readByte() != 0;
        this.ts = in.readLong();
        this.orderId = in.readString();
        this.expireAt = in.readLong();
        this.message = in.readString();
        this.navigable = in.readByte() != 0;
        this.clickJumpUrl = in.readString();
    }

    public static final Creator<NotificationData> CREATOR = new Creator<NotificationData>() {
        @Override
        public NotificationData createFromParcel(Parcel source) {
            return new NotificationData(source);
        }

        @Override
        public NotificationData[] newArray(int size) {
            return new NotificationData[size];
        }
    };
}

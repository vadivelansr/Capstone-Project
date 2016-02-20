package com.akhooo.coofde.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vadivelansr on 1/8/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferBean implements Parcelable {
    private String code;
    private Long coupon;
    private String desc;
    private String title;
    private String url;
    private String type;
    private Long views;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCoupon() {
        return coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public OfferBean() {
    }

    public OfferBean(Parcel in) {
        this.code = in.readString();
        this.coupon = in.readLong();
        this.desc = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.type = in.readString();
        this.views = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeLong(coupon);
        dest.writeString(desc);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(type);
        dest.writeLong(views);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<OfferBean> CREATOR = new Parcelable.Creator<OfferBean>() {
        @Override
        public OfferBean[] newArray(int size) {
            return new OfferBean[size];
        }

        @Override
        public OfferBean createFromParcel(Parcel source) {
            return new OfferBean(source);
        }
    };
}

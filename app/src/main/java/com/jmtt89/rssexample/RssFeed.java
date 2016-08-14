package com.jmtt89.rssexample;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RssFeed implements Parcelable{
    private String name;
    private String url;
    private List<String> topics;

    public RssFeed(String name, String url, List<String> topics) {
        this.name = name;
        this.url = url;
        this.topics = new ArrayList<String>(topics);
    }

    public RssFeed(String url, String name) {
        this.url = url;
        this.name = name;
        this.topics = new ArrayList<String>();
    }

    protected RssFeed(Parcel in) {
        name = in.readString();
        url = in.readString();
        topics = new ArrayList<>();
        in.readStringList(topics);
    }

    public static final Creator<RssFeed> CREATOR = new Creator<RssFeed>() {
        @Override
        public RssFeed createFromParcel(Parcel in) {
            return new RssFeed(in);
        }

        @Override
        public RssFeed[] newArray(int size) {
            return new RssFeed[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List getTopics() {
        return topics;
    }

    public void addTopic(String topic) {
        this.topics.add(topic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssFeed rssFeed = (RssFeed) o;

        return name.equals(rssFeed.name) && url.equals(rssFeed.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", topics=" + topics +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeStringList(topics);
    }
}

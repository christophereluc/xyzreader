package com.example.xyzreader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;

/**
 * @author chris
 *         6/26/16
 */
public class Article implements Parcelable {

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    public String body;
    public String author;
    public String title;
    public long published_date;
    public String imageUrl;

    public Article() {

    }

    protected Article(Parcel in) {
        body = in.readString();
        author = in.readString();
        title = in.readString();
        published_date = in.readLong();
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeLong(published_date);
        dest.writeString(imageUrl);
    }

    public String getAuthorByLine() {
        return DateUtils.getRelativeTimeSpanString(
                published_date,
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString() + " by " + author;
    }

    public Spanned getFormattedBody() {
        return Html.fromHtml(body);
    }
}

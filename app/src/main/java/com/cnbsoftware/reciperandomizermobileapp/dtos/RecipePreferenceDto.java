package com.cnbsoftware.reciperandomizermobileapp.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipePreferenceDto  implements Parcelable {

    public RecipePreferenceDto() {

    }

    @SerializedName("id")
    @Expose
    public int Id;
    @SerializedName("name")
    @Expose
    public String Name;
    @SerializedName("type")
    @Expose
    public int Type;
    @SerializedName("excluded")
    @Expose
    public boolean Excluded;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Name);
        parcel.writeInt(Type);
        //parcel.writeValue(Excluded);
    }

    public static final Parcelable.Creator<RecipePreferenceDto> CREATOR
            = new Parcelable.Creator<>() {
        public RecipePreferenceDto createFromParcel(Parcel in) {
            return new RecipePreferenceDto(in);
        }

        public RecipePreferenceDto[] newArray(int size) {
            return new RecipePreferenceDto[size];
        }
    };

    private RecipePreferenceDto(Parcel in) {
        this.Id = in.readInt();
        this.Name = in.readString();
        this.Type = in.readInt();
        //this.Excluded = (Boolean)in.readValue(null);
    }
}

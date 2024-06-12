package com.m3u8.parser.model;

import java.util.Objects;

public class TrackData {

    private String mUri;
    private TrackInfo mTrackInfo;
    private EncryptionData mEncryptionData;
    private String mProgramDateTime;
    private boolean mHasDiscontinuity;

    public void setmTrackInfo(TrackInfo mTrackInfo) {
        this.mTrackInfo = mTrackInfo;
    }


    public void setmProgramDateTime(String mProgramDateTime) {
        this.mProgramDateTime = mProgramDateTime;
    }


    private TrackData(String uri, TrackInfo trackInfo, EncryptionData encryptionData, String programDateTime, boolean hasDiscontinuity) {
        mUri = uri;
        mTrackInfo = trackInfo;
        mEncryptionData = encryptionData;
        mProgramDateTime = programDateTime;
        mHasDiscontinuity = hasDiscontinuity;
    }

    public String getUri() {
        return mUri;
    }

    public boolean hasTrackInfo() {
        return mTrackInfo != null;
    }

    public TrackInfo getTrackInfo() {
        return mTrackInfo;
    }

    public boolean hasEncryptionData() {
        return mEncryptionData != null;
    }

    public boolean isEncrypted() {
        return hasEncryptionData() &&
                mEncryptionData.getMethod() != null &&
                mEncryptionData.getMethod() != EncryptionMethod.NONE;
    }

    public boolean hasProgramDateTime() {
        return mProgramDateTime != null && mProgramDateTime.length() > 0;
    }

    public String getProgramDateTime() {
        return mProgramDateTime;
    }

    public void setDiscontinuity(boolean discontinuity) {
        this.mHasDiscontinuity = discontinuity;
    }

    public void setUri(String mUri) {
        this.mUri = mUri;
    }

    public boolean hasDiscontinuity() {
        return mHasDiscontinuity;
    }

    public EncryptionData getEncryptionData() {
        return mEncryptionData;
    }

    public Builder buildUpon() {
        return new Builder(getUri(), mTrackInfo, mEncryptionData, mHasDiscontinuity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackData trackData = (TrackData) o;
        return mHasDiscontinuity == trackData.mHasDiscontinuity &&
                Objects.equals(mUri, trackData.mUri) &&
                Objects.equals(mTrackInfo, trackData.mTrackInfo) &&
                Objects.equals(mEncryptionData, trackData.mEncryptionData) &&
                Objects.equals(mProgramDateTime, trackData.mProgramDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUri, mTrackInfo, mEncryptionData, mProgramDateTime, mHasDiscontinuity);
    }

    @Override
    public String toString() {
        return "TrackData{" +
                "mUri='" + mUri + '\'' +
                ", mTrackInfo=" + mTrackInfo +
                ", mEncryptionData=" + mEncryptionData +
                ", mProgramDateTime='" + mProgramDateTime + '\'' +
                ", mHasDiscontinuity=" + mHasDiscontinuity +
                '}';
    }

    public static class Builder {
        private String mUri;
        private TrackInfo mTrackInfo;
        private EncryptionData mEncryptionData;
        private String mProgramDateTime;
        private boolean mHasDiscontinuity;

        public Builder() {
        }

        private Builder(String uri, TrackInfo trackInfo, EncryptionData encryptionData, boolean hasDiscontinuity) {
            mUri = uri;
            mTrackInfo = trackInfo;
            mEncryptionData = encryptionData;
            mHasDiscontinuity = hasDiscontinuity;
        }

        public Builder withUri(String url) {
            mUri = url;
            return this;
        }

        public Builder withTrackInfo(TrackInfo trackInfo) {
            mTrackInfo = trackInfo;
            return this;
        }

        public Builder withEncryptionData(EncryptionData encryptionData) {
            mEncryptionData = encryptionData;
            return this;
        }

        public Builder withProgramDateTime(String programDateTime) {
            mProgramDateTime = programDateTime;
            return this;
        }

        public Builder withDiscontinuity(boolean hasDiscontinuity) {
            mHasDiscontinuity = hasDiscontinuity;
            return this;
        }

        public TrackData build() {
            return new TrackData(mUri, mTrackInfo, mEncryptionData, mProgramDateTime, mHasDiscontinuity);
        }
    }
}

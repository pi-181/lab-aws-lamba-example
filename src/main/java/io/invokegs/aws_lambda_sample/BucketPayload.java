package io.invokegs.aws_lambda_sample;

import java.util.Objects;

public class BucketPayload {
    public final String bucketName;
    public final String key;
    public final String payload;

    public BucketPayload(String bucketName, String key, String payload) {
        this.bucketName = bucketName;
        this.key = key;
        this.payload = payload;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKey() {
        return key;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BucketPayload that = (BucketPayload) o;
        return bucketName.equals(that.bucketName) && key.equals(that.key) && payload.equals(that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bucketName, key, payload);
    }

    @Override
    public String toString() {
        return "BucketPayload{" +
               "bucketName='" + bucketName + '\'' +
               ", key='" + key + '\'' +
               ", payload='" + payload + '\'' +
               '}';
    }
}

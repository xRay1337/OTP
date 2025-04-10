package org.example.service;

public class Config {
    private int codeLength;
    private int lifetime;

    public Config(int codeLength, int lifetime) {
        this.codeLength = codeLength;
        this.lifetime = lifetime;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int timeout) {
        this.lifetime = timeout;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    @Override
    public String toString() {
        return "Config{" +
                "codeLength=" + codeLength +
                ", lifetime=" + lifetime +
                '}';
    }
}
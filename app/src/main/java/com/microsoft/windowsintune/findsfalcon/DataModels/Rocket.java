package com.microsoft.windowsintune.findsfalcon.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Observable;

public class Rocket{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("total_no")
    @Expose
    private Integer totalNo;
    @SerializedName("max_distance")
    @Expose
    private Integer maxDistance;
    @SerializedName("speed")
    @Expose
    private Integer speed;
    private Integer availableUnits = totalNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalNo() {
        return totalNo;
    }

    public void setTotalNo(Integer totalNo) {
        this.totalNo = totalNo;
    }

    public Integer getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Integer maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getAvailableUnits() {
        return availableUnits;
    }
    public void incr(){
        availableUnits++;
    }

    public void decr(){
        availableUnits--;
    }
    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }
}
package com.brianbeech.android.software.stats;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bbeech
 * Date: 2/8/12
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class HuntStats {

    double totalDistance;
    Date huntStartTime;
    Date huntEndTime;
    Date startTrackTime;
    Date endTrackTime;
    double totalAltitude;
    double ascentAmount;
    double descentAmount;

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        if(this.totalDistance == 0){
            this.totalDistance = totalDistance;
        } else{
            this.totalDistance += totalDistance;
        }
    }

    public Date getHuntStartTime() {
        return huntStartTime;
    }

    public void setHuntStartTime(Date huntStartTime) {
        this.huntStartTime = huntStartTime;
    }

    public Date getHuntEndTime() {
        return huntEndTime;
    }

    public void setHuntEndTime(Date huntEndTime) {
        this.huntEndTime = huntEndTime;
    }

    public Date getStartTrackTime() {
        return startTrackTime;
    }

    public void setStartTrackTime(Date startTrackTime) {
        this.startTrackTime = startTrackTime;
    }

    public Date getEndTrackTime() {
        return endTrackTime;
    }

    public void setEndTrackTime(Date endTrackTime) {
        this.endTrackTime = endTrackTime;
    }

    public double getTotalAltitude() {
        return totalAltitude;
    }

    public void setTotalAltitude(double totalAltitude) {
        this.totalAltitude = totalAltitude;
    }

    public double getAscentAmount() {
        return ascentAmount;
    }

    public void setAscentAmount(double ascentAmount) {
        this.ascentAmount = ascentAmount;
    }

    public double getDescentAmount() {
        return descentAmount;
    }

    public void setDescentAmount(double descentAmount) {
        this.descentAmount = descentAmount;
    }
}

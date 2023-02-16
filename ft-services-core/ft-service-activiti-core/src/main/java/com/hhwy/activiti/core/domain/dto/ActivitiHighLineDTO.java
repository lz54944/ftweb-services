package com.hhwy.activiti.core.domain.dto;

import java.util.Set;

/**
 * <br>描 述： ActivitiHighLineDTO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:36
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class ActivitiHighLineDTO {
    private Set<String> highPoint;
    private Set<String> highLine;
    private Set<String> waitingToDo;
    private Set<String> iDo;

    public Set<String> getHighPoint() {
        return highPoint;
    }

    public void setHighPoint(Set<String> highPoint) {
        this.highPoint = highPoint;
    }

    public Set<String> getHighLine() {
        return highLine;
    }

    public void setHighLine(Set<String> highLine) {
        this.highLine = highLine;
    }

    public Set<String> getWaitingToDo() {
        return waitingToDo;
    }

    public void setWaitingToDo(Set<String> waitingToDo) {
        this.waitingToDo = waitingToDo;
    }

    public Set<String> getiDo() {
        return iDo;
    }

    public void setiDo(Set<String> iDo) {
        this.iDo = iDo;
    }
}

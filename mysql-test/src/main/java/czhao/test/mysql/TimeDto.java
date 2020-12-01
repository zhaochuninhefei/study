package czhao.test.mysql;

/**
 * @author zhaochun
 */
public class TimeDto {
    private long prepareDataTime;

    private long truncateTime;

    private long insertOrdTime;
    private long insertCstTime;
    private long insertPrdTime;
    private long insertWhsTime;

    private long selectOrdConnTime;
    private long selectOrdExecTime;
    private long selectOrdLoopTime;
    private long selectOrdAllTime;
    private long ordSize;

    private long selectCstConnTime;
    private long selectCstExecTime;
    private long selectCstLoopTime;
    private long selectCstAllTime;
    private long cstSize;

    private long selectPrdConnTime;
    private long selectPrdExecTime;
    private long selectPrdLoopTime;
    private long selectPrdAllTime;
    private long prdSize;

    private long selectWhsConnTime;
    private long selectWhsExecTime;
    private long selectWhsLoopTime;
    private long selectWhsAllTime;
    private long whsSize;

    private long selectOcConnTime;
    private long selectOcExecTime;
    private long selectOcLoopTime;
    private long selectOcAllTime;
    private long ocSize;

    private long selectOpConnTime;
    private long selectOpExecTime;
    private long selectOpLoopTime;
    private long selectOpAllTime;
    private long opSize;

    private long selectOwConnTime;
    private long selectOwExecTime;
    private long selectOwLoopTime;
    private long selectOwAllTime;
    private long owSize;

    private long selectOobConnTime;
    private long selectOobExecTime;
    private long selectOobLoopTime;
    private long selectOobAllTime;
    private long oobSize;

    public String createStaticsInfo() {
        return String.format(
                "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                prepareDataTime,
                truncateTime,
                insertOrdTime,
                insertCstTime,
                insertPrdTime,
                insertWhsTime,
                selectOrdConnTime,
                selectOrdExecTime,
                selectOrdLoopTime,
                selectOrdAllTime,
                ordSize,
                selectCstConnTime,
                selectCstExecTime,
                selectCstLoopTime,
                selectCstAllTime,
                cstSize,
                selectPrdConnTime,
                selectPrdExecTime,
                selectPrdLoopTime,
                selectPrdAllTime,
                prdSize,
                selectWhsConnTime,
                selectWhsExecTime,
                selectWhsLoopTime,
                selectWhsAllTime,
                whsSize,
                selectOcConnTime,
                selectOcExecTime,
                selectOcLoopTime,
                selectOcAllTime,
                ocSize,
                selectOpConnTime,
                selectOpExecTime,
                selectOpLoopTime,
                selectOpAllTime,
                opSize,
                selectOwConnTime,
                selectOwExecTime,
                selectOwLoopTime,
                selectOwAllTime,
                owSize,
                selectOobConnTime,
                selectOobExecTime,
                selectOobLoopTime,
                selectOobAllTime,
                oobSize
                );
    }

    public long getPrepareDataTime() {
        return prepareDataTime;
    }

    public void setPrepareDataTime(long prepareDataTime) {
        this.prepareDataTime = prepareDataTime;
    }

    public long getTruncateTime() {
        return truncateTime;
    }

    public void setTruncateTime(long truncateTime) {
        this.truncateTime = truncateTime;
    }

    public long getInsertOrdTime() {
        return insertOrdTime;
    }

    public void setInsertOrdTime(long insertOrdTime) {
        this.insertOrdTime = insertOrdTime;
    }

    public long getInsertCstTime() {
        return insertCstTime;
    }

    public void setInsertCstTime(long insertCstTime) {
        this.insertCstTime = insertCstTime;
    }

    public long getInsertPrdTime() {
        return insertPrdTime;
    }

    public void setInsertPrdTime(long insertPrdTime) {
        this.insertPrdTime = insertPrdTime;
    }

    public long getInsertWhsTime() {
        return insertWhsTime;
    }

    public void setInsertWhsTime(long insertWhsTime) {
        this.insertWhsTime = insertWhsTime;
    }

    public long getSelectOrdConnTime() {
        return selectOrdConnTime;
    }

    public void setSelectOrdConnTime(long selectOrdConnTime) {
        this.selectOrdConnTime = selectOrdConnTime;
    }

    public long getSelectOrdExecTime() {
        return selectOrdExecTime;
    }

    public void setSelectOrdExecTime(long selectOrdExecTime) {
        this.selectOrdExecTime = selectOrdExecTime;
    }

    public long getSelectOrdLoopTime() {
        return selectOrdLoopTime;
    }

    public void setSelectOrdLoopTime(long selectOrdLoopTime) {
        this.selectOrdLoopTime = selectOrdLoopTime;
    }

    public long getSelectOrdAllTime() {
        return selectOrdAllTime;
    }

    public void setSelectOrdAllTime(long selectOrdAllTime) {
        this.selectOrdAllTime = selectOrdAllTime;
    }

    public long getOrdSize() {
        return ordSize;
    }

    public void setOrdSize(long ordSize) {
        this.ordSize = ordSize;
    }

    public long getSelectCstConnTime() {
        return selectCstConnTime;
    }

    public void setSelectCstConnTime(long selectCstConnTime) {
        this.selectCstConnTime = selectCstConnTime;
    }

    public long getSelectCstExecTime() {
        return selectCstExecTime;
    }

    public void setSelectCstExecTime(long selectCstExecTime) {
        this.selectCstExecTime = selectCstExecTime;
    }

    public long getSelectCstLoopTime() {
        return selectCstLoopTime;
    }

    public void setSelectCstLoopTime(long selectCstLoopTime) {
        this.selectCstLoopTime = selectCstLoopTime;
    }

    public long getSelectCstAllTime() {
        return selectCstAllTime;
    }

    public void setSelectCstAllTime(long selectCstAllTime) {
        this.selectCstAllTime = selectCstAllTime;
    }

    public long getCstSize() {
        return cstSize;
    }

    public void setCstSize(long cstSize) {
        this.cstSize = cstSize;
    }

    public long getSelectPrdConnTime() {
        return selectPrdConnTime;
    }

    public void setSelectPrdConnTime(long selectPrdConnTime) {
        this.selectPrdConnTime = selectPrdConnTime;
    }

    public long getSelectPrdExecTime() {
        return selectPrdExecTime;
    }

    public void setSelectPrdExecTime(long selectPrdExecTime) {
        this.selectPrdExecTime = selectPrdExecTime;
    }

    public long getSelectPrdLoopTime() {
        return selectPrdLoopTime;
    }

    public void setSelectPrdLoopTime(long selectPrdLoopTime) {
        this.selectPrdLoopTime = selectPrdLoopTime;
    }

    public long getSelectPrdAllTime() {
        return selectPrdAllTime;
    }

    public void setSelectPrdAllTime(long selectPrdAllTime) {
        this.selectPrdAllTime = selectPrdAllTime;
    }

    public long getPrdSize() {
        return prdSize;
    }

    public void setPrdSize(long prdSize) {
        this.prdSize = prdSize;
    }

    public long getSelectWhsConnTime() {
        return selectWhsConnTime;
    }

    public void setSelectWhsConnTime(long selectWhsConnTime) {
        this.selectWhsConnTime = selectWhsConnTime;
    }

    public long getSelectWhsExecTime() {
        return selectWhsExecTime;
    }

    public void setSelectWhsExecTime(long selectWhsExecTime) {
        this.selectWhsExecTime = selectWhsExecTime;
    }

    public long getSelectWhsLoopTime() {
        return selectWhsLoopTime;
    }

    public void setSelectWhsLoopTime(long selectWhsLoopTime) {
        this.selectWhsLoopTime = selectWhsLoopTime;
    }

    public long getSelectWhsAllTime() {
        return selectWhsAllTime;
    }

    public void setSelectWhsAllTime(long selectWhsAllTime) {
        this.selectWhsAllTime = selectWhsAllTime;
    }

    public long getWhsSize() {
        return whsSize;
    }

    public void setWhsSize(long whsSize) {
        this.whsSize = whsSize;
    }

    public long getSelectOcConnTime() {
        return selectOcConnTime;
    }

    public void setSelectOcConnTime(long selectOcConnTime) {
        this.selectOcConnTime = selectOcConnTime;
    }

    public long getSelectOcExecTime() {
        return selectOcExecTime;
    }

    public void setSelectOcExecTime(long selectOcExecTime) {
        this.selectOcExecTime = selectOcExecTime;
    }

    public long getSelectOcLoopTime() {
        return selectOcLoopTime;
    }

    public void setSelectOcLoopTime(long selectOcLoopTime) {
        this.selectOcLoopTime = selectOcLoopTime;
    }

    public long getSelectOcAllTime() {
        return selectOcAllTime;
    }

    public void setSelectOcAllTime(long selectOcAllTime) {
        this.selectOcAllTime = selectOcAllTime;
    }

    public long getOcSize() {
        return ocSize;
    }

    public void setOcSize(long ocSize) {
        this.ocSize = ocSize;
    }

    public long getSelectOpConnTime() {
        return selectOpConnTime;
    }

    public void setSelectOpConnTime(long selectOpConnTime) {
        this.selectOpConnTime = selectOpConnTime;
    }

    public long getSelectOpExecTime() {
        return selectOpExecTime;
    }

    public void setSelectOpExecTime(long selectOpExecTime) {
        this.selectOpExecTime = selectOpExecTime;
    }

    public long getSelectOpLoopTime() {
        return selectOpLoopTime;
    }

    public void setSelectOpLoopTime(long selectOpLoopTime) {
        this.selectOpLoopTime = selectOpLoopTime;
    }

    public long getSelectOpAllTime() {
        return selectOpAllTime;
    }

    public void setSelectOpAllTime(long selectOpAllTime) {
        this.selectOpAllTime = selectOpAllTime;
    }

    public long getOpSize() {
        return opSize;
    }

    public void setOpSize(long opSize) {
        this.opSize = opSize;
    }

    public long getSelectOwConnTime() {
        return selectOwConnTime;
    }

    public void setSelectOwConnTime(long selectOwConnTime) {
        this.selectOwConnTime = selectOwConnTime;
    }

    public long getSelectOwExecTime() {
        return selectOwExecTime;
    }

    public void setSelectOwExecTime(long selectOwExecTime) {
        this.selectOwExecTime = selectOwExecTime;
    }

    public long getSelectOwLoopTime() {
        return selectOwLoopTime;
    }

    public void setSelectOwLoopTime(long selectOwLoopTime) {
        this.selectOwLoopTime = selectOwLoopTime;
    }

    public long getSelectOwAllTime() {
        return selectOwAllTime;
    }

    public void setSelectOwAllTime(long selectOwAllTime) {
        this.selectOwAllTime = selectOwAllTime;
    }

    public long getOwSize() {
        return owSize;
    }

    public void setOwSize(long owSize) {
        this.owSize = owSize;
    }

    public long getSelectOobConnTime() {
        return selectOobConnTime;
    }

    public void setSelectOobConnTime(long selectOobConnTime) {
        this.selectOobConnTime = selectOobConnTime;
    }

    public long getSelectOobExecTime() {
        return selectOobExecTime;
    }

    public void setSelectOobExecTime(long selectOobExecTime) {
        this.selectOobExecTime = selectOobExecTime;
    }

    public long getSelectOobLoopTime() {
        return selectOobLoopTime;
    }

    public void setSelectOobLoopTime(long selectOobLoopTime) {
        this.selectOobLoopTime = selectOobLoopTime;
    }

    public long getSelectOobAllTime() {
        return selectOobAllTime;
    }

    public void setSelectOobAllTime(long selectOobAllTime) {
        this.selectOobAllTime = selectOobAllTime;
    }

    public long getOobSize() {
        return oobSize;
    }

    public void setOobSize(long oobSize) {
        this.oobSize = oobSize;
    }
}

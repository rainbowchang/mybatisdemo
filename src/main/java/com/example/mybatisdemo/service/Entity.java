package com.example.mybatisdemo.service;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.annotation.Table;

import java.io.Serializable;
import java.util.Date;


@Table(
        columns = {@Column(
                name = "status",
                attrName = "status",
                label = "状态",
                isUpdate = false,
                comment = "（推荐状态：0：正常；1：删除；2：停用；3：冻结；4：审核、待审核；5：审核驳回；9：草稿）"
        ), @Column(
                name = "create_by",
                attrName = "createBy",
                label = "创建者",
                isUpdate = false
        ), @Column(
                name = "create_date",
                attrName = "createDate",
                label = "创建时间",
                isUpdate = false,
                isQuery = false
        ), @Column(
                name = "update_by",
                attrName = "updateBy",
                label = "更新者",
                isUpdate = true
        ), @Column(
                name = "update_date",
                attrName = "updateDate",
                label = "更新时间",
                isUpdate = true,
                isQuery = false
        ), @Column(
                name = "remarks",
                attrName = "remarks",
                label = "备注信息"
//                queryType = QueryType.LIKE
        ), @Column(
                name = "corp_code",
                attrName = "corpCode",
                label = "租户代码",
                isUpdate = false
        ), @Column(
                name = "corp_name",
                attrName = "corpName",
                label = "租户名称",
                isUpdate = false,
                isQuery = false
        )}
)
public class Entity<T extends Entity<T>> implements Serializable {

    protected String id;

    protected String corpName;
    protected String corpCode;
    protected String status;
    protected String remarks;
    protected Date updateDate;
    protected Date createDate;
    protected String createBy;
    protected String updateBy;
    protected boolean isNewRecord;

    private SqlMap<T> sqlMap;

    public Entity(){
        sqlMap = new SqlMap<T>(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public SqlMap<T> getSqlMap() {
        return sqlMap;
    }

    @Override
    public String toString() {
        return ", corpName='" + corpName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'';
    }


}

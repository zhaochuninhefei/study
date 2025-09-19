//******************************************************************************
// 版权所有(c)，科大国创，保留所有权利。
//******************************************************************************

package com.czhao.test.jdk11.db.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TbEmployeeExample {
    /**
     * 排序.
     */
    protected String orderByClause;

    /**
     * 是否去重.
     */
    protected boolean distinct;

    /**
     * 条件列表.
     */
    protected List<Criteria> oredCriteria;

    public TbEmployeeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameIsNull() {
            addCriterion("employee_name is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameIsNotNull() {
            addCriterion("employee_name is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameEqualTo(String value) {
            addCriterion("employee_name =", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotEqualTo(String value) {
            addCriterion("employee_name <>", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameGreaterThan(String value) {
            addCriterion("employee_name >", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameGreaterThanOrEqualTo(String value) {
            addCriterion("employee_name >=", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameLessThan(String value) {
            addCriterion("employee_name <", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameLessThanOrEqualTo(String value) {
            addCriterion("employee_name <=", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameLike(String value) {
            addCriterion("employee_name like", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotLike(String value) {
            addCriterion("employee_name not like", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameIn(List<String> values) {
            addCriterion("employee_name in", values, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotIn(List<String> values) {
            addCriterion("employee_name not in", values, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameBetween(String value1, String value2) {
            addCriterion("employee_name between", value1, value2, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotBetween(String value1, String value2) {
            addCriterion("employee_name not between", value1, value2, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexIsNull() {
            addCriterion("employee_sex is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexIsNotNull() {
            addCriterion("employee_sex is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexEqualTo(Byte value) {
            addCriterion("employee_sex =", value, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexNotEqualTo(Byte value) {
            addCriterion("employee_sex <>", value, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexGreaterThan(Byte value) {
            addCriterion("employee_sex >", value, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexGreaterThanOrEqualTo(Byte value) {
            addCriterion("employee_sex >=", value, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexLessThan(Byte value) {
            addCriterion("employee_sex <", value, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexLessThanOrEqualTo(Byte value) {
            addCriterion("employee_sex <=", value, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexIn(List<Byte> values) {
            addCriterion("employee_sex in", values, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexNotIn(List<Byte> values) {
            addCriterion("employee_sex not in", values, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexBetween(Byte value1, Byte value2) {
            addCriterion("employee_sex between", value1, value2, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeSexNotBetween(Byte value1, Byte value2) {
            addCriterion("employee_sex not between", value1, value2, "employeeSex");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdIsNull() {
            addCriterion("employee_entry_ymd is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdIsNotNull() {
            addCriterion("employee_entry_ymd is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdEqualTo(Date value) {
            addCriterionForJDBCDate("employee_entry_ymd =", value, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdNotEqualTo(Date value) {
            addCriterionForJDBCDate("employee_entry_ymd <>", value, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdGreaterThan(Date value) {
            addCriterionForJDBCDate("employee_entry_ymd >", value, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("employee_entry_ymd >=", value, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdLessThan(Date value) {
            addCriterionForJDBCDate("employee_entry_ymd <", value, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("employee_entry_ymd <=", value, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdIn(List<Date> values) {
            addCriterionForJDBCDate("employee_entry_ymd in", values, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdNotIn(List<Date> values) {
            addCriterionForJDBCDate("employee_entry_ymd not in", values, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("employee_entry_ymd between", value1, value2, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeEntryYmdNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("employee_entry_ymd not between", value1, value2, "employeeEntryYmd");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelIsNull() {
            addCriterion("employee_level is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelIsNotNull() {
            addCriterion("employee_level is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelEqualTo(Byte value) {
            addCriterion("employee_level =", value, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelNotEqualTo(Byte value) {
            addCriterion("employee_level <>", value, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelGreaterThan(Byte value) {
            addCriterion("employee_level >", value, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelGreaterThanOrEqualTo(Byte value) {
            addCriterion("employee_level >=", value, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelLessThan(Byte value) {
            addCriterion("employee_level <", value, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelLessThanOrEqualTo(Byte value) {
            addCriterion("employee_level <=", value, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelIn(List<Byte> values) {
            addCriterion("employee_level in", values, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelNotIn(List<Byte> values) {
            addCriterion("employee_level not in", values, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelBetween(Byte value1, Byte value2) {
            addCriterion("employee_level between", value1, value2, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeLevelNotBetween(Byte value1, Byte value2) {
            addCriterion("employee_level not between", value1, value2, "employeeLevel");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdIsNull() {
            addCriterion("employee_org_id is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdIsNotNull() {
            addCriterion("employee_org_id is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdEqualTo(Integer value) {
            addCriterion("employee_org_id =", value, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdNotEqualTo(Integer value) {
            addCriterion("employee_org_id <>", value, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdGreaterThan(Integer value) {
            addCriterion("employee_org_id >", value, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("employee_org_id >=", value, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdLessThan(Integer value) {
            addCriterion("employee_org_id <", value, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdLessThanOrEqualTo(Integer value) {
            addCriterion("employee_org_id <=", value, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdIn(List<Integer> values) {
            addCriterion("employee_org_id in", values, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdNotIn(List<Integer> values) {
            addCriterion("employee_org_id not in", values, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdBetween(Integer value1, Integer value2) {
            addCriterion("employee_org_id between", value1, value2, "employeeOrgId");
            return (Criteria) this;
        }

        public Criteria andEmployeeOrgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("employee_org_id not between", value1, value2, "employeeOrgId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
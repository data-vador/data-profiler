package com.data.profiler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
public class ProfileResult {
    @JsonProperty("columnName")
    private String columnName;

    @JsonProperty("dataType")
    private String dataType;

    @JsonProperty("completeness")
    private Double completeness;

    @JsonProperty("distinctCount")
    private Long distinctCount;

    @JsonProperty("mean")
    private Double mean;

    @JsonProperty("stdDev")
    private Double stdDev;

    @JsonProperty("min")
    private String min;

    @JsonProperty("max")
    private String max;

    @JsonProperty("nullCount")
    private Long nullCount;

    @JsonProperty("totalCount")
    private Long totalCount;

    // Constructeurs
    public ProfileResult() {}

    public ProfileResult(String columnName, String dataType, Double completeness,
                         Long distinctCount, Double mean, Double stdDev,
                         String min, String max, Long nullCount, Long totalCount) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.completeness = completeness;
        this.distinctCount = distinctCount;
        this.mean = mean;
        this.stdDev = stdDev;
        this.min = min;
        this.max = max;
        this.nullCount = nullCount;
        this.totalCount = totalCount;
    }

    // Getters et Setters
    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public Double getCompleteness() { return completeness; }
    public void setCompleteness(Double completeness) { this.completeness = completeness; }

    public Long getDistinctCount() { return distinctCount; }
    public void setDistinctCount(Long distinctCount) { this.distinctCount = distinctCount; }

    public Double getMean() { return mean; }
    public void setMean(Double mean) { this.mean = mean; }

    public Double getStdDev() { return stdDev; }
    public void setStdDev(Double stdDev) { this.stdDev = stdDev; }

    public String getMin() { return min; }
    public void setMin(String min) { this.min = min; }

    public String getMax() { return max; }
    public void setMax(String max) { this.max = max; }

    public Long getNullCount() { return nullCount; }
    public void setNullCount(Long nullCount) { this.nullCount = nullCount; }

    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
}

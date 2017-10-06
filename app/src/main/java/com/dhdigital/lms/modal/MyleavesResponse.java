package com.dhdigital.lms.modal;

import java.util.List;

/**
 * Created by admin on 04/10/17.
 */

public class MyleavesResponse {

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<LeaveModal> getContent() {
        return content;
    }

    public void setContent(List<LeaveModal> content) {
        this.content = content;
    }

    private Boolean last;
    private Integer totalPages;
    private Integer numberOfElements;
    private Integer totalElements;
    private Boolean first;
    private Integer number;
    private List<LeaveModal> content;
}

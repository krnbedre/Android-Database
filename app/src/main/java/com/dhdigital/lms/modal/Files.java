package com.dhdigital.lms.modal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Bedre on 22/1/16.
 * DarkHorse BOA
 */
public class Files {

    private int id;
    private String fileName;
    private String pathURI;
    private Boolean fileType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPathURI() {
        return pathURI;
    }

    public void setPathURI(String pathURI) {
        this.pathURI = pathURI;
    }

    public Boolean getFileType() {
        return fileType;
    }

    public void setFileType(Boolean fileType) {
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

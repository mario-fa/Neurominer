package com.devsphere.examples.mapping.upload;

import com.devsphere.mapping.FileBean;

/**
 * Upload bean
 */
public class UploadBean implements java.io.Serializable {
    private String optionalText;
    private String textGroup[];
    private FileBean optionalFile;
    private FileBean fileList[];

    /**
     * No-arg constructor
     */
    public UploadBean() {
    }

    /**
     * Gets the optionalText property
     */
    public String getOptionalText() {
        return this.optionalText;
    }

    /**
     * Sets the optionalText property
     */
    public void setOptionalText(String value) {
        this.optionalText = value;
    }

    /**
     * Gets the textGroup property
     */
    public String[] getTextGroup() {
        return this.textGroup;
    }

    /**
     * Sets the textGroup property
     */
    public void setTextGroup(String values[]) {
        this.textGroup = values;
    }

    /**
     * Gets an element of the textGroup property
     */
    public String getTextGroup(int index) {
        return this.textGroup[index];
    }

    /**
     * Sets an element of the textGroup property
     */
    public void setTextGroup(int index, String value) {
        this.textGroup[index] = value;
    }

    /**
     * Gets the optionalFile property
     */
    public FileBean getOptionalFile() {
        return this.optionalFile;
    }

    /**
     * Sets the optionalFile property
     */
    public void setOptionalFile(FileBean value) {
        this.optionalFile = value;
    }

    /**
     * Gets the fileList property
     */
    public FileBean[] getFileList() {
        return this.fileList;
    }

    /**
     * Sets the fileList property
     */
    public void setFileList(FileBean values[]) {
        this.fileList = values;
    }

    /**
     * Gets an element of the fileList property
     */
    public FileBean getFileList(int index) {
        return this.fileList[index];
    }

    /**
     * Sets an element of the fileList property
     */
    public void setFileList(int index, FileBean value) {
        this.fileList[index] = value;
    }

}
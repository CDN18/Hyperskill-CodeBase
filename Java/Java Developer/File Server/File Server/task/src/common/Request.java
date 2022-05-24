package common;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    public RequestType operation;
    public ParamType by;
    public String fileName;
    public byte[] fileContent;
    public int fileID;

    public Request(RequestType operation, ParamType by) {
        this.operation = operation;
        this.by = by;
    }

    public Request(RequestType operation, ParamType by, String fileName, byte[] fileContent, int fileID) {
        this.operation = operation;
        this.by = by;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileID = fileID;
    }
}


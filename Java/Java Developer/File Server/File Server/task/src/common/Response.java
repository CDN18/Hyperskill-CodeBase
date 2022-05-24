package common;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    public int code;
    public int fileID;
    public String fileName;
    public byte[] rawData;

    public Response(int code) {
        this.code = code;
    }

    public Response() {
        this.code = 400;
    }
}

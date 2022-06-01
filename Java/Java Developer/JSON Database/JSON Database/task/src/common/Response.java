package common;

import com.google.gson.JsonElement;

public class Response {
    public String response;
    public JsonElement value;
    public String reason;

    public Response(String response, JsonElement value) {
        this.response = response;
        this.value = value;
    }

    public Response(String response, String reason) {
        this.response = response;
        this.reason = reason;
    }

    public Response(String response) {
        this.response = response;
    }

    public Response() {

    }
}

package common;

public enum DefaultResponse {
    OK(new Response("OK")),
    NO_SUCH_KEY(new Response("ERROR", "No such key"));

    DefaultResponse(Response response) {
        this.response = response;
    }

    Response response;
}

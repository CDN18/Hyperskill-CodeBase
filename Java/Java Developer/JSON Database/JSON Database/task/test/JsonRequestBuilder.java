import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class JsonBuilder {

    private final Gson gson = new Gson();
    private final JsonObject jsonObject = new JsonObject();

    private JsonBuilder() {

    }

    public static JsonBuilder newBuilder() {
        return new JsonBuilder();
    }

    public JsonBuilder addValue(String key, String value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonBuilder addValue(String key, JsonElement value) {
        jsonObject.add(key, value);
        return this;
    }

    public JsonBuilder addJsonObject(String key, JsonObject object) {
        jsonObject.add(key, object);
        return this;
    }

    public String getAsString() {
        return jsonObject.toString();
    }

    public JsonObject getAsJsonObject() {
        return jsonObject;
    }
}
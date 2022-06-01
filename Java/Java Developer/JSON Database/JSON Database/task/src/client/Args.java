package client;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonElement;

public class Args {

    @Parameter(names = "-t", description = "command type")
    String type;

    @Parameter(names = "-k", description = "key")
    String key;

    @Parameter(names = "-v", description = "value")
    String value;

    @Parameter(names = "-in", description = "input file path")
    String input;

}

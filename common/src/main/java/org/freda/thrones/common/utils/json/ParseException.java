package org.freda.thrones.common.utils.json;

/**
 * Exception when parse json
 */
public class ParseException extends RuntimeException {
    private static final long serialVersionUID = 2560442967697088006L;
    private int               position         = 0;
    private String            jsonString       = "";

    /**
     * Constructs a new json exception with the specified detail message.
     *
     * @param json     the json text which cause JSONParseException
     * @param position the position of illegal escape char at json text;
     * @param message  the detail message. The detail message is saved for
     *                 later retrieval by the {@link #getMessage()} method.
     */
    public ParseException(String json, int position, String message) {
        super(message);
        this.jsonString = json;
        this.position = position;
    }

    /**
     * Get message about error when parsing illegal json
     *
     * @return error message
     */
    @Override
    public String getMessage() {
        final int maxTipLength = 10;
        int end = position + 1;
        int start = end - maxTipLength;
        if (start < 0) {
            start = 0;
        }
        if (end > jsonString.length()) {
            end = jsonString.length();
        }
        return String.format("%s (%d):%s", jsonString.substring(start, end), position, super.getMessage());
    }

    public String getJson() {
        return this.jsonString;
    }

    public int getPosition() {
        return this.position;
    }

}

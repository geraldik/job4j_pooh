package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        validateContent(content);
        String[] stringArr = content.split("\n");
        String[] firstLineArr = stringArr[0].split(" ");
        var httpRequestType = firstLineArr[0];
        var poohMode = firstLineArr[1].split("/")[1];
        var sourceName = firstLineArr[1].split("/")[2];
        var param = "";
        if ("GET".equals(httpRequestType)) {
            param = firstLineArr[1].split("/").length > 3
                    ? firstLineArr[1].split("/")[3]
                    : param;
        }
        if ("POST".equals((httpRequestType))) {
            param = stringArr.length > 7
                    ? stringArr[7].split("\r")[0]
                    : param;
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    private static void validateContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException("Empty http request");
        }
        String[] stringArr = content.split(" ");
        if (!"GET".equals(stringArr[0])
                && !"POST".equals(stringArr[0])) {
            throw new IllegalArgumentException("Wrong first word of http request. "
                    + "It must be \"GET\" or \"POST\"");
        }
        if (stringArr.length < 3
                || !stringArr[1].startsWith("/")
                || stringArr[1].split("/").length < 3
                || (!"queue".equals(stringArr[1].split("/")[1])
                && !"topic".equals(stringArr[1].split("/")[1]))) {
            throw new IllegalArgumentException("Wrong block with type/path");
        }
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
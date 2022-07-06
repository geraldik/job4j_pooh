package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();

    public static final String SUCCESSFUL_RESPONSE = "200";

    public static final String NO_CONTENT = "204";

    public static final String NOT_FOUND = "404";

    public static final String  NOT_IMPLEMENTED = "501";

    @Override
    public Resp process(Req req) {
        String text = "";
        String status = NOT_IMPLEMENTED;
        if ("POST".equals(req.getHttpRequestType())) {
            status = put(req) ? SUCCESSFUL_RESPONSE : NOT_FOUND;
        } else if ("GET".equals(req.getHttpRequestType())) {
            text = get(req);
            if (text == null) {
                status = NO_CONTENT;
                text = "";
            } else {
                status = SUCCESSFUL_RESPONSE;
            }
        }

        return new Resp(text, status);
    }

    private boolean put(Req req) {
        addIfEmpty(req);
        var name = req.getSourceName();
        var param = req.getParam();
        return queue.get(name).add(param);
    }

    private void addIfEmpty(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
    }

    private String get(Req req) {
        return queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
    }

}
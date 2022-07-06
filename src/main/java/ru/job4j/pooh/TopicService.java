package ru.job4j.pooh;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics =
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
        boolean rsl = false;
        var queueMap = topics.get(req.getSourceName());
        if (queueMap != null) {
            queueMap.forEachValue(1, v -> v.add(req.getParam()));
            rsl = true;
        }
        return true;
    }

    private String get(Req req) {
        var messageQueue =
                topics.computeIfAbsent(req.getSourceName(), topicName -> new ConcurrentHashMap<>())
                .putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
        return Optional.ofNullable(messageQueue)
                .map(ConcurrentLinkedQueue::poll)
                .orElse(null);
    }

}

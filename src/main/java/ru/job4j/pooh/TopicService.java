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

    @Override
    public Resp process(Req req) {
        String text = null;
        String status = null;
        if ("POST".equals(req.getHttpRequestType())) {
            put(req);
            status = put(req) ? SUCCESSFUL_RESPONSE : NOT_FOUND;
        } else if ("GET".equals(req.getHttpRequestType())) {
            text = get(req);
            status = text == null ? NO_CONTENT : SUCCESSFUL_RESPONSE;
        }
        return new Resp(text, status);
    }

    private boolean put(Req req) {
        var queueMap = topics.get(req.getSourceName());
        return Optional.ofNullable(queueMap)
                        .flatMap(map ->
                            map.values()
                                    .stream()
                                    .map(queue -> queue.add(req.getParam()))
                                    .findAny()
                        ).orElse(false);
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

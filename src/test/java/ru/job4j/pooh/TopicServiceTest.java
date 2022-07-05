package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

public class TopicServiceTest {

    @Test
    public void whenGetAndPostThenGetMessage() {
        TopicService topicService = new TopicService();
        String paramForPostMethod = "temperature=18";
        topicService.process(
                new Req("GET", "topic", "weather", "1")
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPostMethod)
        );
        Resp result = topicService.process(
                new Req("GET", "queue", "weather", "1")
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenPostThenGetNull() {
        TopicService topicService = new TopicService();
        String paramForPostMethod = "temperature=18";
        topicService.process(
                new Req("POST", "topic", "weather", paramForPostMethod)
        );
        Resp result = topicService.process(
                new Req("GET", "queue", "weather", "1")
        );
        assertNull(result.text());
    }
}

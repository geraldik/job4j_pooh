## Pooh JMS
***

Аналог асинхронной очереди.
Приложение запускает Socket и ждет клиентов.
Клиенты могут быть двух типов: отправители (publisher), получатели (subscriber).
В качестве клиента используется cURL. `https://curl.se/download.html`
В качестве протокола используется HTTP.

***
**Инструменты**
- Java 17
- Maven
- JUnit
- Concurrency

**Сборка и запуск<br>**
- для сборки `mvn install`
- для запуска `java -jar target/job4j_pooh-1.0-jar-with-dependencies.jar`

**Пример работы**

Для работы в режиме очереди нужно в отдельном терминале направить запрос:<br>
`curl -X POST -d "test message" http://localhost:9000/queue/queue-name` <br>
Для получения сообщения из очереди:<br>
`curl -X GET http://localhost:9000/queue/queue-name` <br>
В ответ придет: `test message`
***
**Контакты**

email : geraldik1986@gmail.com
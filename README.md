# Notebook Telegram Bot
## Приложение, которок позволяет сохранять сообщения отправленные через Telegram в файл

### Описание
Spring Boot приложение (Telegram bot), на данный момент максимально простое. Каждое сообщение написанное в боте проходит проверку на корректный ChatId, 
чтобы быть уверенным, что прислано из нужного аккаунта, после чего несложным методом сохраняется в notebook.txt файл рядом с собой на сервере. 
Если файла нет, он будет создан, если есть запись будет добавлена в конец без изменения уже существующего содержания. 
Каждая запись помечается датой и временем момента в который приложение на сервере получает сообщение.
Приложение имеет логирование на всех этапах и сохраняет это все рядом с собой в файл general.log.
Также при запуске и остановке об этом приходит сообщение, что позволяет определять статус приложения (не работает, если процесс был убит).

### Установка
**Технологии:** Spring Boot, Java 17, Maven

**Зависимости, кроме базовых:** 
```
<dependency>
  <groupId>org.apache.logging.log4j</groupId>
  <artifactId>log4j-jcl</artifactId>
</dependency>
<dependency>
  <groupId>org.telegram</groupId>
  <artifactId>telegrambots</artifactId>
  <version>6.8.0</version>
    <exclusions>
      <exclusion>
        <artifactId>commons-logging</artifactId>
        <groupId>commons-logging</groupId>
      </exclusion>
    </exclusions>
</dependency>
```

Для запуска требуется указание трех параметров, наименования и токена бота полученных у BotFather, а также Id аккаунта который будет работать с ботом. 
Вот где указываются данные параметры 
```
    // id нашего личного аккаунта Telegram
    private final Long myId = **********L;
    
    // имя бота, этот и ниже методы служебные, без них работать не будет
    @Override
    public String getBotUsername() {
        return "name_of_your_bot";
    }

    // токен бота
    @Override
    public String getBotToken() {
        return "token_of_your_bot";
    }
```

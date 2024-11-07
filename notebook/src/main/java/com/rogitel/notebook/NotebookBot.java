package com.rogitel.notebook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

// данная аннотация убирает предупреждение о неиспользовании deprecated методов
@SuppressWarnings("deprecation")
// добавляем класс в Spring
@Component
public class NotebookBot extends TelegramLongPollingBot {

    // логгер объект, через который мы будем писать логи
    private static final Logger log = LoggerFactory.getLogger(NotebookBot.class);

    // id нашего личного аккаунта Telegram
    private final Long myId = 1436375463L;

    // путь к файлу, в который будут сохранятся сообщения (если файлв нет, то будет
    // создан автоматически)
    String path = new String("notebook.txt");
    File file = new File(path);
    Date date;

    // метод для обработки обновлений сообщений в чате
    @Override
    public void onUpdateReceived(Update update) {

        // проверяем появилось ли сообщение в чате, и сравниваем id адресата с myId
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("We received a new message");
            if (update.getMessage().getChat().getId().equals(myId)) {
                log.info("Correct ChatId == myId");

                // собираем результаты обработки сообщения методами и отправляем
                answerSender(update.getMessage().getChatId(), workWithFile(update.getMessage().getText()));

            } else {
                // обрабатываем случай, если написали с другого аккаунта не равного myId
                log.info("Incorrect ChatId != myId");
                answerSender(update.getMessage().getChatId(), "Это приватный бот...");
            }
        }

    }

    // возвращаем имя бота, этот и ниже методы служебные, без них работать не будет
    @Override
    public String getBotUsername() {
        return "NotebookReallyTestBot";
    }

    // возвращаем токен бота
    @Override
    public String getBotToken() {
        return "7943334136:AAEoUJN4VWCKpyeuyWCYBEt9aGwJezeoNlM";
    }

    // метод работы с файлом
    public String workWithFile(String message) {
        if (file.exists() && !file.isDirectory()) {
            writer(message);
            log.info("File exist " + path);
        } else {
            writer(message);
            log.info("Create new file " + path);
        }

        return "Сообщение сохранено в файл!";
    }

    // метод записи в файл
    public void writer(String message) {

        FileWriter writer;
        try {
            // собираем шаблон для сохранения, добавляем дату и время, выполняем перенос
            // строки для разделения сообщений
            date = new Date();
            writer = new FileWriter(path, true);
            writer.write(date.toString());
            writer.write("\n");
            writer.write(message);
            writer.write("\n\n");
            writer.close();
            log.info("The message has been saved to a file");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    //метод для отправки сообщений
    public void answerSender(Long chatId, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(answer);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // сообщение о запуске программы
    @PostConstruct
    public void startingMessage() {
        LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String answer = "🟩 Программа запустидась " + currentDateTime.format(formatter).toString();
        answerSender(myId, answer);

    }

    // сообщение о завершении программы
    @PreDestroy
    public void finalMessage() throws ClassNotFoundException, SQLException {
        LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String answer = "🟥 Программа остановилась " + currentDateTime.format(formatter).toString();
        answerSender(myId, answer);

    }

}

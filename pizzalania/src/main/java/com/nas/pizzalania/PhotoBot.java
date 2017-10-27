package com.nas.pizzalania;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class PhotoBot extends TelegramLongPollingBot {
    Bill bill = new Bill();

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            System.out.println(message_text);
            if (message_text.equals("/start")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("لطفا خدمت مورد نظر را انتخاب کنید:");
                // Create ReplyKeyboardMarkup object
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                // Create the keyboard (list of keyboard rows)
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Create a keyboard row
                KeyboardRow row1 = new KeyboardRow();
                // Set each button, you can also use KeyboardButton objects if you need something else than text
                row1.add("سفارش پیتزا");
                row1.add("تکرار آخرین خرید");
                // Add the first row to the keyboard
                keyboard.add(row1);
                // Create another keyboard row
                row1 = new KeyboardRow();
                // Set each button for the second line
                row1.add("پشتیبانی");
                // Add the second row to the keyboard
                keyboard.add(row1);
                // Set the keyboard to the markup
                keyboardMarkup.setKeyboard(keyboard);
                // Add it to the message
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("سفارش پیتزا")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("چه نوع پیتزایی دوست داری؟");
                // Create ReplyKeyboardMarkup object
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                // Create the keyboard (list of keyboard rows)
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Create a keyboard row
                KeyboardRow row1 = new KeyboardRow();
                // Set each button, you can also use KeyboardButton objects if you need something else than text
                row1.add("پیتزا مخصوص لانیا(1نفره)");
                // Add the second row to the keyboard
                keyboard.add(row1);
                
                row1 = new KeyboardRow();
                row1.add("پیتزا مخصوص لانیا(1نفره)");
                // Set the keyboard to the markup
                keyboardMarkup.setKeyboard(keyboard);
                // Add it to the message
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("ایرانسل")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("لطفا نوع شارژ را انتخاب کنید:");
                bill.setOperator("Irancell");
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();

                KeyboardRow row1 = new KeyboardRow();
                row1.add("کارت شارژ");
                row1.add("شارژ برخط");
                keyboard.add(row1);

                replyKeyboardMarkup.setKeyboard(keyboard);
                message.setReplyMarkup(replyKeyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("کارت شارژ")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("لطفا مبلغ شارژ را انتخاب کنید:");
                bill.setChrgType("کارت شارژ");


                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();

                KeyboardRow row1 = new KeyboardRow();
                row1.add("1000 تومان");
                row1.add("2000 تومان");
                row1.add("5000 تومان");
                keyboard.add(row1);

                row1 = new KeyboardRow();
                row1.add("10000 تومان");
                row1.add("20000 تومان");
                keyboard.add(row1);

                replyKeyboardMarkup.setKeyboard(keyboard);
                message.setReplyMarkup(replyKeyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("شارژ مستقیم")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("لطفا مبلغ شارژ را انتخاب کنید:");
                bill.setChrgType("شارژ مستقیم");


                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();

                KeyboardRow row1 = new KeyboardRow();
                row1.add("1000 تومان");
                row1.add("2000 تومان");
                row1.add("5000 تومان");
                keyboard.add(row1);

                row1 = new KeyboardRow();
                row1.add("10000 تومان");
                row1.add("20000 تومان");
                keyboard.add(row1);

                replyKeyboardMarkup.setKeyboard(keyboard);
                message.setReplyMarkup(replyKeyboardMarkup);

            } else if (message_text.equals("1000 تومان") || message_text.equals("2000 تومان") || message_text.equals("5000 تومان") || message_text.equals("10000 تومان") || message_text.equals("20000 تومان")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("لطفا شماره موبایل را وارد کنید:");
                bill.setPrice(message_text);
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);

                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.startsWith("09")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id);
                bill.setPhoneNum(message_text);
                StringBuilder stringBuilder = new StringBuilder("فاکتور خرید:");
                stringBuilder.append("\n\n").append("اپراتور: ").append(bill.getOperator());
                stringBuilder.append("\n").append("نوع شارژ: ").append(bill.getChrgType());
                stringBuilder.append("\n").append("مبلغ: ").append(bill.getPrice());
                stringBuilder.append("\n").append("شماره تلفن:").append(bill.getPhoneNum());
                stringBuilder.append("\n\n آیا اطلاعات صحیح است؟");
                msg.setText(stringBuilder.toString());

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboardRows = new ArrayList();

                KeyboardRow row1 = new KeyboardRow();
                row1.add("تایید و خرید");
                row1.add("خیر و بازگشت");
                keyboardRows.add(row1);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                msg.setReplyMarkup(replyKeyboardMarkup);
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("/hide")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Keyboard hidden");
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("تایید و خرید")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("token"+"به منظور پرداخت وجه روی لینک بزنید تا به صفحه بانک هدایت شوید");
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Unknown command");
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Message contains photo
            // Set variables
            long chat_id = update.getMessage().getChatId();

            List<PhotoSize> photos = update.getMessage().getPhoto();
            String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            int f_width = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            int f_height = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
            SendPhoto msg = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto(f_id)
                    .setCaption(caption);
            try {
                sendPhoto(msg); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "pizzalaniabot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "475445507:AAHWqbA_sJcjy0-xVSmfhU0YKBJRLjuS0VE";
    }
}
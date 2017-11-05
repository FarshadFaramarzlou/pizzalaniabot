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

public class PizzaOrder extends TelegramLongPollingBot {

    private Customers customers = Customers.getCusInstance();

    @Override
    public void onUpdateReceived(Update update) {
        int step = 0;
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            int i = customers.isCusExist(chat_id);

            if (message_text.equals("/start")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("به ربات پیتزا تنوری لانیا خوش آمدید");
                if (i != -1) {

                } else {
                    //initual a new customer
                    Customer cus = new Customer();
                    cus.setChat_id(chat_id);
                    cus.setStep(1);
                    customers.cusList.add(cus);
                    System.out.println(chat_id);
                }

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();

                KeyboardRow row1 = new KeyboardRow();
                row1.add("سفارش پیتزا");
                keyboard.add(row1);

                row1 = new KeyboardRow();
                row1.add("تکرار آخرین خرید");
                keyboard.add(row1);

                row1 = new KeyboardRow();
                row1.add("درباره من");
                keyboard.add(row1);

                row1 = new KeyboardRow();
                row1.add("پشتیبانی");
                keyboard.add(row1);

                keyboardMarkup.setKeyboard(keyboard);
                message.setReplyMarkup(keyboardMarkup);

                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("سفارش پیتزا") && customers.cusList.get(i).getStep() == 1) {
                if (i != -1) {
                    customers.cusList.get(i).addStep();
                    chooseFood(chat_id);
                    System.out.println(chat_id + "  " + "مشتری سفارش دارد");
                } else {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("فرآیند خرید را مجدد اجرا کنید!");
                }
            } else if (customers.cusList.get(i).getStep() == 2) {
                 customers.cusList.get(i).addStep();

                if (i != -1) {
                    if (customers.cusList.get(i).billList.isEmpty()) {
                        Bill bill = new Bill();
                        Order order = new Order();
                        Food food = new Food();
                        food.setFoodName(message_text);
                        order.food = food;
                        bill.orderList.add(order);
                        customers.cusList.get(i).billList.add(bill);
                    } else {
                        Order order = new Order();
                        Food food = new Food();
                        food.setFoodName(message_text);
                        order.food = food;
                        int billNum = customers.cusList.get(i).billList.size()-1;
                        customers.cusList.get(i).billList.get(billNum).orderList.add(order);
                    }
                    SendMessage message = new SendMessage() // Create a message object object
                                .setChatId(chat_id)
                                .setText("چند تا پیتزا میخوای؟");

                        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                        message.setReplyMarkup(keyboardMarkup);
                        try {
                            execute(message); // Sending our message object to user
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                } else {

                }

            } else if (customers.cusList.get(i).getStep() == 3) {
                if (i != -1) {

                    customers.cusList.get(i).addStep();
                    int billNum = customers.cusList.get(i).billList.size() - 1;
                    int orderNum = customers.cusList.get(i).billList.get(billNum).orderList.size() - 1;
                    customers.cusList.get(i).billList.get(billNum).orderList.get(orderNum).num
                            = Integer.valueOf(message_text);

                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id);
                    SendMessage message2 = new SendMessage() // Create a message object object
                            .setChatId(chat_id);
                    int j = 1;
                    StringBuilder stringBuilder = new StringBuilder("فاکتور خرید:");
                    stringBuilder.append("\n");

                    Customer c = customers.cusList.get(i);
                    for (Order o : c.billList.get(billNum).orderList) {
                        stringBuilder.append("\n")
                                .append(j + "- ")
                                .append(o.food.foodName)
                                .append(" - قیمت:" + o.food.price)
                                .append(" - تعداد:" + o.num);
                        j++;
                    }
                    message.setText(stringBuilder.toString());
                    message2.setText("میخوای به فاکتورت چیزی اضافه کنی؟");

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> keyboard = new ArrayList<>();

                    KeyboardRow row1 = new KeyboardRow();
                    row1.add("اره");
                    row1.add("نه");
                    keyboard.add(row1);

                    replyKeyboardMarkup.setKeyboard(keyboard);
                    message.setReplyMarkup(replyKeyboardMarkup);
                    try {
                        execute(message);
                        execute(message2); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (message_text.equals("اره") && customers.cusList.get(i).getStep() == 4) {
                customers.cusList.get(i).setStep(2);
                chooseFood(chat_id);
            } else if (message_text.equals("نه") && customers.cusList.get(i).getStep() == 4) {
                customers.cusList.get(i).addStep();
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("لطفا شماره موبایل را وارد کنید:");
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);

                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.startsWith("09")&& customers.cusList.get(i).getStep() == 5) {
                customers.cusList.get(i).addStep();
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("لطفا آدرس دریافت سفارشات را وارد کنید؟");

                customers.cusList.get(i).setPhone(message_text);
                
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (customers.cusList.get(i).getStep() == 6) {
                customers.cusList.get(i).addStep();
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("آیا مشخصات صحیح است؟");

                customers.cusList.get(i).setAddress(message_text);
                
                StringBuilder stringBuilder = new StringBuilder("قبل از آماده سازی سفارش با شما تماس گرفته می شود");
                    stringBuilder.append("\n\n").append("شماره همراه:")
                            .append(customers.cusList.get(i).getPhone());
                
                
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("/hide")) {
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
            } else if (message_text.equals("تایید و خرید")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("token" + "به منظور پرداخت وجه روی لینک بزنید تا به صفحه بانک هدایت شوید");
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

    public void chooseFood(long chatId) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText("چه نوع پیتزایی دوست داری؟");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("پیتزا مخصوص لانیا(1نفره)");
        keyboard.add(row1);

        row1 = new KeyboardRow();
        row1.add("پیتزا مخصوص لانیا(2نفره)");
        keyboard.add(row1);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

package com.nas.pizzalania;

import static java.lang.Math.toIntExact;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class PizzaOrder extends TelegramLongPollingBot {

    Customers customers = Customers.getCusInstance();

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.equals("/start")) {
                if (customers.isCustomer(chat_id)) {
                    sendMessage(chat_id, "به پیتزا تنوری لانیا خوش آمدید");
                    log("farshad", "faramarzlou", "00", "Start state 1", "Customer Size: " + customers.getCusList().size());
                    goToMain(chat_id);
                } else if (customers.isCusExist(chat_id)) {
                    sendMessage(chat_id, "به پیتزا تنوری لانیا خوش آمدید");
                    log("farshad", "faramarzlou", "00", "Start state 2", "Customer Size: " + customers.getCusList().size());
                    goToMain(chat_id);
                } else {
                    Customer cus = new Customer(chat_id);
                    if (customers.newCustomer(cus)) {
                        customers.addCustumerToList(cus);
                        sendMessage(chat_id, "به پیتزا تنوری لانیا خوش آمدید");
                        log("farshad", "faramarzlou", "00", "Start state 3", "Customer Size: " + customers.getCusList().size());
                        goToMain(chat_id);
                    } else {
                        sendMessage(chat_id, "خطایی در ثبت نام رخ داده است!");
                        log("farshad", "faramarzlou", "00", "Start state 4", "Customer Size: " + customers.getCusList().size());
                    }

                }

            } else if (message_text.equals(Constants.ORDER)) {
                if (!customers.isCustomer(chat_id)) {
                    sendMessage(chat_id, "لطفا دوباره وارد شوید");
                } else {
                    List<KeyboardRow> keyboard = new ArrayList<>();
                    KeyboardRow row1 = new KeyboardRow();
                    row1.add(Constants.PIZZA);
                    keyboard.add(row1);

                    row1 = new KeyboardRow();
                    row1.add(Constants.DRINK);
                    row1.add(Constants.SALAD);
                    keyboard.add(row1);

                    row1 = new KeyboardRow();
                    row1.add(Constants.FINISHORDER);
                    row1.add(Constants.BACK);
                    keyboard.add(row1);

                    sendMessageKB(chat_id, "لطفا یکی از گزینه ها را انتخاب کنید:", keyboard);
                    log("farshad", "faramarzlou", "000", message_text, "Print test");
                }

            } else if (message_text.equals(Constants.PIZZA)) {
                boolean flag = false;
                for (Customer c : customers.getCusList()) {
                    if (c.getChat_id() == chat_id) {
                        showFood(chat_id);
                        flag = true;
                    }
                }
                if (!flag) {
                    sendMessage(chat_id, "لطفا دوباره وارد شوید!");
                }

            } else if (message_text.equals("--")) {

            } else if (message_text.equals(Constants.SHOPPINGBASKET)) {
                showBasket(chat_id);
            } else if (message_text.equals("اره")) {
                showFood(chat_id);
            } else if (message_text.equals("نه")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("لطفا شماره موبایل را وارد کنید:");
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);

            } else if (message_text.startsWith("09")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("لطفا آدرس دریافت سفارشات را وارد کنید؟");

            } else if (customers.getCustumerFromList(chat_id).getStep() == 6) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("آیا مشخصات صحیح است؟");


                /* StringBuilder stringBuilder = new StringBuilder("قبل از آماده سازی سفارش با شما تماس گرفته می شود");
                stringBuilder.append("\n\n").append("شماره همراه:")
                        .append(customers.cusList.get(i).getPhone());
                 */
            } else if (message_text.equals("/hide")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Keyboard hidden");
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);

            } else if (message_text.equals(Constants.BACK)) {
                goToMain(chat_id);
            } else {
                sendMessage(chat_id, "Unknown command");
            }
        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            if (call_data.startsWith("food")) {
                selectFood(chat_id, call_data);
                String answer = "پیتزا ثبت شد";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                showNumber(chat_id);

                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (call_data.startsWith("num")) {
                selectNumber(chat_id, call_data);
                String answer = "تعداد: " + call_data.replaceFirst("num", "");
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                /*List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                rowInline1.add(new InlineKeyboardButton().setText("بله").setCallbackData("chooseyes"));
                rowInline1.add(new InlineKeyboardButton().setText("خیر").setCallbackData("chooseno"));
                rowsInline.add(rowInline1);
                sendMessageIKB(chat_id, "انتخاب دیگری دارید؟", rowsInline);*/
            } else if (call_data.startsWith("choose")) {

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

    public void showBasket(long chat_id) {
        for (Customer c : customers.getCusList()) {
            if (c.getChat_id() == chat_id) {
                if (c.getBasket().getOrderItems().isEmpty()) {
                    sendMessage(chat_id, "سبد خالی است!");
                } else {
                    int j = 1;
                    StringBuilder stringBuilder = new StringBuilder("سبد خرید:");
                    stringBuilder.append("\n");

                    for (OrderItem o : c.getBasket().getOrderItems()) {
                        stringBuilder.append("\n").append(j)
                                .append("- ")
                                .append(o.getEatable().getName())
                                .append(" - قیمت:")
                                .append(o.getEatable().getPrice())
                                .append(" - تعداد:")
                                .append(o.getNum());
                        j++;
                    }
                    sendMessage(chat_id, stringBuilder.toString());
                }
            }
        }

    }

    public void selectFood(long chatId, String foodId) {
        for (Customer c : customers.getCusList()) {
            if (c.getChat_id() == chatId) {
                int id = Integer.valueOf(foodId.replaceFirst("food", ""));
                c.getBasket().selectEatable(id);
            }
        }
    }

    public void selectNumber(long chatId, String num) {
        for (Customer c : customers.getCusList()) {
            if (c.getChat_id() == chatId) {
                int n = Integer.valueOf(num.replaceFirst("num", ""));
                c.getBasket().selectNum(n);
            }
        }
    }

    public void showNumber(long chatId) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        //row 1
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("1").setCallbackData("num1"));
        rowInline1.add(new InlineKeyboardButton().setText("2").setCallbackData("num2"));
        rowInline1.add(new InlineKeyboardButton().setText("3").setCallbackData("num3"));
        rowInline1.add(new InlineKeyboardButton().setText("4").setCallbackData("num4"));
        rowsInline.add(rowInline1);
        //row 2
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("5").setCallbackData("num5"));
        rowInline2.add(new InlineKeyboardButton().setText("6").setCallbackData("num6"));
        rowInline2.add(new InlineKeyboardButton().setText("7").setCallbackData("num7"));
        rowInline2.add(new InlineKeyboardButton().setText("8").setCallbackData("num8"));
        rowsInline.add(rowInline2);
        //row 3
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(new InlineKeyboardButton().setText("9").setCallbackData("num9"));
        rowInline3.add(new InlineKeyboardButton().setText("10").setCallbackData("num10"));
        rowInline3.add(new InlineKeyboardButton().setText("11").setCallbackData("num11"));
        rowInline3.add(new InlineKeyboardButton().setText("12").setCallbackData("num12"));
        rowsInline.add(rowInline3);
        // Add it to the message
        sendMessageIKB(chatId, "لطفا تعداد پیتزا را انتخاب کنید:", rowsInline);
    }

    /*
    public void showOrderItem(long chatId) {
        StringBuilder stringBuilder = new StringBuilder("تعداد ");
        for (Customer c : customers.getCusList()) {
            if (c.getChat_id() == chatId) {
                if (c.getBasket().getOrderItems().isEmpty()) {

                } else {
                    int billListSize = c.getBillList().size() - 1;
                    int orderSize = c.getBillList().get(billListSize).getOrderItems().size() - 1;
                    stringBuilder.append(c.getBillList().get(billListSize).getOrderItems().get(orderSize).num + " تا ");
                    stringBuilder.append(c.getBillList().get(billListSize).getOrderItems().get(orderSize).eatable.Name + " انتخاب شد.\nبرای ثبت نهایی دکمه زیر را فشار دهید ");

                }
            }
        }
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        //row 1
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("1").setCallbackData("num1"));
        rowInline1.add(new InlineKeyboardButton().setText("2").setCallbackData("num2"));
        rowInline1.add(new InlineKeyboardButton().setText("3").setCallbackData("num3"));
        rowInline1.add(new InlineKeyboardButton().setText("4").setCallbackData("num4"));
        rowsInline.add(rowInline1);

        // Add it to the message
        sendMessageIKB(chatId, "???? ????? ????? ?? ?????? ????:", rowsInline);
    }
     */
    public void showFood(long chatId) {
        ArrayList<Eatable> foodList;
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Eatable.class
        ).buildSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();
            foodList = (ArrayList<Eatable>) session.createQuery("from Eatable where type =:type ").setParameter("type", "1").list();
            session.getTransaction().commit();

        } finally {
            factory.close();
        }

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Eatable e : foodList) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(e.getName() + "   قیمت:" + e.getPrice()).setCallbackData("food" + e.getId()));
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
        }
        // Add it to the message
        sendMessageIKB(chatId, "چه نوع غذایی دوس داری؟", rowsInline);
    }

    public void sendMessageIKB(long chat_id, String textMessage, List<List<InlineKeyboardButton>> inlineKeyboard) {
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(textMessage);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard(inlineKeyboard);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageKB(long chat_id, String textMessage, List<KeyboardRow> keyboardButton) {
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(textMessage);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup()
                .setKeyboard(keyboardButton);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Long chat_id, String textMessage) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(textMessage);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void goToMain(long chat_id) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(Constants.SHOPPINGBASKET);
        keyboard.add(row1);

        row1 = new KeyboardRow();
        row1.add(Constants.LANIAMENU);
        row1.add(Constants.ORDER);
        keyboard.add(row1);

        row1 = new KeyboardRow();
        row1.add(Constants.FOLLOWORDER);
        row1.add(Constants.REBATE);
        keyboard.add(row1);

        row1 = new KeyboardRow();
        row1.add(Constants.SUPPORT);
        keyboard.add(row1);

        sendMessageKB(chat_id, "منوی اصلی", keyboard);

    }

    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }
}

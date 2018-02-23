package com.nas.pizzalania;

import static java.lang.Math.toIntExact;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        if (update.hasMessage() || update.hasCallbackQuery()) {
            // Set variables
            String call_data = "";
            String message_text = "";
            long chat_id = -1;
            if (update.hasCallbackQuery()) {
                chat_id = update.getCallbackQuery().getMessage().getChatId();

                call_data = update.getCallbackQuery().getData();
            } else {
                message_text = update.getMessage().getText();
                chat_id = update.getMessage().getChatId();
            }
            if (message_text.equals("/start")) {
                if (customers.isCustomer(chat_id)) {
                    sendMessage(chat_id, "به پیتزا تنوری لانیا خوش آمدید");
                    log("farsha", "faramarzlou", "00", "Start state 1", "Customer Size: " + customers.getCusList().size());
                    goToMain(chat_id);
                } else if (customers.isCusExist(chat_id)) {
                    sendMessage(chat_id, "به پیتزا تنوری لانیا خوش آمدید");
                    log("farshad", "faramarzlou", "00", "Start state 2", "Customer Size: " + customers.getCusList().size());
                    goToMain(chat_id);
                } else {
                    Customer cus = new Customer(chat_id);
                    if (customers.newCustomer(cus)) {
                        sendMessage(chat_id, "به پیتزا تنوری لانیا خوش آمدید");
                        log("farshad", "faramarzlou", "00", "Start state 3", "Customer Size: " + customers.getCusList().size());
                        goToMain(chat_id);
                    } else {
                        sendMessage(chat_id, "خطایی در ثبت نام رخ داده است!");
                        log("farshad", "faramarzlou", "00", "Start state 4", "Customer Size: " + customers.getCusList().size());
                    }
                    //sendMessage(chat_id, "خطا: شما وارد ربات نشدید لطفا روی استارت بزنید\n\n" + "/start");
                }

            } else {
                boolean errorFlag = true;
                for (Customer c : customers.getCusList()) {
                    errorFlag = false;
                    if (c.getChat_id() == chat_id) {
                        if (c.getChatState() == 0 && !update.hasCallbackQuery()) {

                            System.out.println("normall");
                            if (message_text.equals(Constants.ORDER) || message_text.equals(Constants.ADDTOBASKET)) {
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
                                c.getBasket().showFood(1);
                                sendMessageIKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getRowsInline());
                            } else if (message_text.equals(Constants.DRINK)) {
                                c.getBasket().showFood(2);
                                sendMessageIKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getRowsInline());

                            } else if (message_text.equals(Constants.SALAD)) {
                                c.getBasket().showFood(3);
                                sendMessageIKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getRowsInline());

                            } else if (message_text.equals(Constants.LANIAMENU)) {
                                c.getBasket().showFood(1);
                                sendMessageIKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getRowsInline());
                                c.getBasket().showFood(2);
                                sendMessageIKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getRowsInline());
                                c.getBasket().showFood(3);
                                sendMessageIKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getRowsInline());

                            } else if (message_text.equals("--")) {

                            } else if (message_text.equals(Constants.SHOPPINGBASKET)) {
                                c.getBasket().showBasket(chat_id);
                                sendMessageKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getKeyboard());

                            } else if (message_text.equals(Constants.SUPPORT)) {
                                sendMessage(chat_id, "تفن پشتیبانی: 09192795531\n");

                            } else if (message_text.equals(Constants.ACCOUNT)) {
                                c.showAccountInfo();
                                c.showAccountKeyboard();
                                sendMessageKB(chat_id, MessageControler.getStringBuilder().toString(), MessageControler.getKeyboard());
                                sendMessageKB(chat_id, "لطفا موارد" + Constants.WARN + "دار راوارد کنید:", MessageControler.getKeyboard());
                            } else if (message_text.equals(Constants.FINISHBASKET)) {
                                c.showAccountInfo();
                                sendMessage(chat_id, MessageControler.getStringBuilder().toString());
                                c.showVerifyBasket();
                                sendMessageKB(chat_id, MessageControler.getStringBuilder().toString(), MessageControler.getKeyboard());

                            } else if (message_text.equals(Constants.FINISH)) {
                                c.saveBasket();
                                c.finish();
                                sendMessageKB(chat_id, MessageControler.getStringBuilder().toString(), MessageControler.getKeyboard());
                                StringBuilder str = new StringBuilder("نام مشتری: " + c.getfName() + " " + c.getlName());
                                str.append("\nآدرس مشتری: " + c.getAddress());
                                str.append("\n شماره تماس: " + c.getPhone());
                                c.getBasket().showBasket(chat_id);
                                String s = c.getBasket().getStringBuilder().toString();
                                str.append("\n" + s);
                                long id = 116979632;
                                sendMessage(id, str.toString());
                                c.setBasket(new Basket(chat_id));

                                goToMain(chat_id);
                            } else if (message_text.equals(Constants.FINISHORDER)) {
                                c.getBasket().showBasket(chat_id);
                                sendMessageKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getKeyboard());
                            } else if (message_text.equals(Constants.FNAME) || message_text.equals(Constants.W_FNAME)) {
                                sendMessage(chat_id, "لطفا نام خود را وارد کنید:\nترجیها نام دریافت کننده سفارشات");
                                c.setChatState(ChatState.S_NAME);
                            } else if (message_text.equals(Constants.LNAME) || message_text.equals(Constants.W_LNAME)) {
                                sendMessage(chat_id, "لطفا نام خانوادگی خود را وارد کنید:");
                                c.setChatState(ChatState.S_LASTNAME);
                            } else if (message_text.equals(Constants.PHONE) || message_text.equals(Constants.W_PHONE)) {
                                sendMessage(chat_id, "لطفا شماره موبایل خود را وارد کنید:");
                                c.setChatState(ChatState.S_PHONE);
                                SendMessage message = new SendMessage()
                                        .setChatId(chat_id);
                                message.setReplyToMessageId(update.getMessage().getMessageId());

                                try {
                                    execute(message); // Sending our message object to user
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            } else if (message_text.equals(Constants.ADDRESS) || message_text.equals(Constants.W_ADDRESS)) {
                                sendMessage(chat_id, "لطفا آدرس دقیق خود را وارد کنید:");
                                c.setChatState(ChatState.S_ADDRESS);
                            } else if (message_text.equals(Constants.RPHONE) || message_text.equals(Constants.W_RPHONE)) {
                                sendMessage(chat_id, "لطفا شماره تلفن معرف خود را وارد کنید:");
                                c.setChatState(ChatState.S_RPHONE);
                            } else if (message_text.equals(Constants.EMPTYBASKET)) {
                                c.getBasket().emptyBasket();
                                c.getBasket().showBasket(chat_id);
                                sendMessageKB(chat_id, c.getBasket().getStringBuilder().toString(), c.getBasket().getKeyboard());

                            } else if (message_text.equals("/hide")) {
                                SendMessage msg = new SendMessage()
                                        .setChatId(chat_id)
                                        .setText("Keyboard hidden");
                                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                                msg.setReplyMarkup(keyboardMarkup);

                            } else if (message_text.equals(Constants.BACK)) {
                                goToMain(chat_id);
                            } else {
                                if (!message_text.equals("/start")) {
                                    sendMessage(chat_id, "Unknown command");
                                }
                            }
                        } else if (c.getChatState() != 0 && !update.hasCallbackQuery()) {
                            System.out.println("chat state");
                            if (message_text.equals(Constants.BACK)) {
                                c.setChatState(ChatState.S_NON);
                                goToMain(chat_id);
                            } else if (c.getChatState() == ChatState.S_NAME) {
                                c.setfName(message_text);
                                c.updateCustomer(c);
                                c.setChatState(ChatState.S_NON);
                                c.showAccountKeyboard();
                                sendMessageKB(chat_id, "نام: " + c.getfName(), MessageControler.getKeyboard());

                            } else if (c.getChatState() == ChatState.S_LASTNAME) {
                                c.setlName(message_text);
                                c.updateCustomer(c);
                                c.setChatState(ChatState.S_NON);
                                c.showAccountKeyboard();
                                sendMessageKB(chat_id, "نام خانوادگی: " + c.getlName(), MessageControler.getKeyboard());
                            } else if (c.getChatState() == ChatState.S_PHONE) {
                                c.setPhone(message_text);
                                c.updateCustomer(c);
                                c.setChatState(ChatState.S_NON);
                                c.showAccountKeyboard();
                                sendMessageKB(chat_id, "تلفن همراه: " + c.getPhone(), MessageControler.getKeyboard());
                                c.showAccountInfo();
                                c.showAccountKeyboard();
                            } else if (c.getChatState() == ChatState.S_RPHONE) {
                                c.setrPhone(message_text);
                                c.updateCustomer(c);
                                c.setChatState(ChatState.S_NON);
                                c.showAccountKeyboard();
                                sendMessageKB(chat_id, "شماره تماس معرف: " + c.getrPhone(), MessageControler.getKeyboard());
                            } else if (c.getChatState() == ChatState.S_ADDRESS) {
                                c.setAddress(message_text);
                                c.updateCustomer(c);
                                c.setChatState(ChatState.S_NON);
                                c.showAccountKeyboard();
                                sendMessageKB(chat_id, "آدرس: " + c.getAddress(), MessageControler.getKeyboard());
                            }
                        } else {
                            if (update.hasCallbackQuery()) {
                                long message_id = update.getCallbackQuery().getMessage().getMessageId();
                                if (call_data.startsWith("food")) {
                                    c.getBasket().selectFood(call_data, "food");
                                    editMessage(chat_id, message_id, "پیتزا ثبت شد");
                                    showNumber(chat_id);

                                } else if (call_data.startsWith("drink")) {
                                    c.getBasket().selectFood(call_data, "drink");
                                    editMessage(chat_id, message_id, "نوشیدنی ثبت شد");
                                    showNumber(chat_id);

                                } else if (call_data.startsWith("salad")) {
                                    c.getBasket().selectFood(call_data, "salad");
                                    editMessage(chat_id, message_id, "سالاد ثبت شد");
                                    showNumber(chat_id);

                                } else if (call_data.startsWith("num")) {
                                    c.getBasket().selectNumber(call_data);
                                    String answer = "تعداد: " + call_data.replaceFirst("num", "");
                                    editMessage(chat_id, message_id, answer);

                                } else if (call_data.startsWith("choose")) {

                                }

                            }
                        }

                    }
                }
                if(errorFlag)
                sendMessage(chat_id, "خطا: شما وارد ربات نشدید لطفا روی استارت بزنید\n\n" + "/start");

            }
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        //return "@NaazGolBot";
        return "@pizzalaniaBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "475445507:AAHWqbA_sJcjy0-xVSmfhU0YKBJRLjuS0VE";
        //return "200356273:AAHjO7evJacJrDUWrOrN_HSuHq1DOQWnOvM";
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
        row1.add(Constants.ACCOUNT);
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

    public void setChatState(long chat_id, int chatState) {
        for (Customer c : customers.getCusList()) {
            if (chat_id == c.getChat_id()) {
                c.setChatState(chatState);
            }
        }
    }

    private void editMessage(long chat_id, long message_id, String answer) {
        EditMessageText new_message = new EditMessageText()
                .setChatId(chat_id)
                .setMessageId(toIntExact(message_id))
                .setText(answer);
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

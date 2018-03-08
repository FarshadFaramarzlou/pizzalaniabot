package com.nas.pizzalania;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;


public class MessageControler {
    private static List<List<InlineKeyboardButton>> rowsInline = null;
    private static ArrayList<KeyboardRow> keyboard = null;
    private static StringBuilder stringBuilder = null;

    public static List<List<InlineKeyboardButton>> getRowsInline() {
        return rowsInline;
    }

    public static void setRowsInline(List<List<InlineKeyboardButton>> rowsInline) {
        MessageControler.rowsInline = rowsInline;
    }

    public static ArrayList<KeyboardRow> getKeyboard() {
        return keyboard;
    }

    public static void setKeyboard(ArrayList<KeyboardRow> keyboard) {
        MessageControler.keyboard = keyboard;
    }

    public static StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public static void setStringBuilder(StringBuilder stringBuilder) {
        MessageControler.stringBuilder = stringBuilder;
    }

    


}

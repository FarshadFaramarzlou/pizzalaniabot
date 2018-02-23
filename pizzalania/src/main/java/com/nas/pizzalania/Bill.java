package com.nas.pizzalania;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

@Entity
@Table(name = "bill")
public class Bill {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "bill_order_item", joinColumns = {
        @JoinColumn(name = "id_bill")}, inverseJoinColumns = {
        @JoinColumn(name = "id_order_item")})
    private List<OrderItem> orderItems;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private Date date;

    //tahvilType p:peyk -- h:hozoori
    @Column(name = "tahvil_type")
    private String tahvilType = "p";

    //payType n:interneti -- p:tahvilPeyk -- h:hozoori
    @Column(name = "pay_type")
    private String payType = "p";

    //private DoDate doDate;
    @Column(name = "money_sum")
    private double moneySum;

    @Column(name = "rebate_sum")
    private double rebateSum;

    @Transient
    private List<List<InlineKeyboardButton>> rowsInline = null;
    @Transient
    private ArrayList<KeyboardRow> keyboard = null;
    @Transient
    private StringBuilder stringBuilder = null;

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Bill() {
        this.orderItems = new ArrayList<>();
    }

    public void sumItemMoney() {
        this.moneySum = 0;
    }

    public void sumItemRebate() {

        this.rebateSum = 0;

    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void emptyBasket() {
        this.orderItems = new ArrayList<>();
    }

    public String getTahvilType() {
        return tahvilType;
    }

    public void setTahvilType(String tahvilType) {
        this.tahvilType = tahvilType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }

    public double getRebateSum() {
        return rebateSum;
    }

    public void setRebateSum(double rebateSum) {
        this.rebateSum = rebateSum;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<List<InlineKeyboardButton>> getRowsInline() {
        return rowsInline;
    }

    public void setRowsInline(List<List<InlineKeyboardButton>> rowsInline) {
        this.rowsInline = rowsInline;
    }

    public ArrayList<KeyboardRow> getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(ArrayList<KeyboardRow> keyboard) {
        this.keyboard = keyboard;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public boolean newOrder(Customer cus) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(OrderItem.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(cus);
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
        return true;
    }

    public void selectFood(String eatableId, String type) {
        String id = eatableId.replaceFirst(type, "");
        OrderItem oi = new OrderItem();
        oi.selectEatable(id);
        if (orderItems.isEmpty()) {
            this.orderItems.add(oi);
        } else {
            int flag = 0;
            int index = -1;
            for (OrderItem o : getOrderItems()) {
                index++;
                if (o.getEatable().getId()==Integer.valueOf(id)) {
                    flag = 1;
                    break;
                }
            }

            if (flag == 0) {
                this.orderItems.add(oi);
            } else if (flag == 1) {
                orderItems.remove(index);
                this.orderItems.add(oi);
            }
        }
    }

    public void selectNumber(String num) {
        int n = Integer.valueOf(num.replaceFirst("num", ""));
        int oiSize = orderItems.size() - 1;
        this.orderItems.get(oiSize).setNum(n);
    }

    public void showBasket(long chat_id) {

        int i = -1;
        i = getOrderItems().size() - 1;
        for (; i >= 0; i--) {
            if (getOrderItems().get(i).getNum() == 0) {
                orderItems.remove(i);
            }
        }
        if (getOrderItems().isEmpty()) {
            keyboard = new ArrayList<KeyboardRow>();
            KeyboardRow row1 = new KeyboardRow();
            row1.add(Constants.SHOWLASTORDER);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.ADDTOBASKET);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.BACK);
            keyboard.add(row1);

            stringBuilder = new StringBuilder("ÓÈÏ ÔãÇ ÎÇá? ÇÓÊ.");
            stringBuilder.append("\n\n");
            stringBuilder.append("ÈÇ ÝÔÑÏä Ï˜ãå\" " + Constants.SHOWLASTORDER + "\"" + "ã? ÊæÇä?Ï ÂÎÑ?ä ÎÑ?Ï ÎæÏ ÑÇ Ê˜ÑÇÑ ˜ä?Ï.");

        } else {

            keyboard = new ArrayList<KeyboardRow>();
            KeyboardRow row1 = new KeyboardRow();
            row1.add(Constants.FINISHBASKET);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.ADDTOBASKET);
            row1.add(Constants.EMPTYBASKET);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.BACK);
            keyboard.add(row1);
            int j = 1;
            stringBuilder = new StringBuilder("ÓÈÏ ÎÑ?Ï:");
            stringBuilder.append("\n");

            for (OrderItem o : getOrderItems()) {
                stringBuilder.append("\n").append(j)
                        .append("- ")
                        .append(o.getEatable().getName())
                        .append(" - Þ?ãÊ:")
                        .append(o.getEatable().getPrice())
                        .append(" - ÊÚÏÇÏ:")
                        .append(o.getNum());
                moneySum = +o.getNum() * o.getEatable().getPrice();
                j++;
            }

            stringBuilder.append("\n\n");
            stringBuilder.append(String.valueOf("ãÈáÛ ˜á ÎÑ?Ï: " + moneySum));

        }

    }

    public void showFood(int type) {
        String price = "Þ?ãÊ:  ";
        String eatType = "";
        switch (type) {
            case 1:
                eatType = "food";
                stringBuilder = new StringBuilder("ãäæ? ?ÊÒÇ:\n");
                break;
            case 2:
                eatType = "drink";
                stringBuilder = new StringBuilder("ãäæ? äæÔ?Ïä?:\n");
                break;
            case 3:
                eatType = "salad";
                stringBuilder = new StringBuilder("ãäæ? ÓÇáÇÏ:\n");
                break;
            default:
                break;
        }
        ArrayList<Eatable> foodList = Eatable.getEatableFromDbByType(type);

        rowsInline = new ArrayList<>();
        for (Eatable e : foodList) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(e.getName() + price + e.getPrice()).setCallbackData(eatType + e.getId()));
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
        }
    }

}

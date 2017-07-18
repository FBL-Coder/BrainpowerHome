package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Say GoBay on 2017/7/5.
 * 安防报警信息
 */
public class Safety_Data implements Serializable{

    public List<Data_Data> datas;

    public List<Data_Data> getDatas() {
        if (datas == null)
            datas = new ArrayList<>();
        return datas;
    }

    public void setDatas(List<Data_Data> datas) {
        this.datas = datas;
    }

   public class Data_Data implements Serializable{

        public int id;
        public int year;
        public int month;
        public int day;
        public int h;
        public int m;
        public int s;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getM() {
            return m;
        }

        public void setM(int m) {
            this.m = m;
        }

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }
    }
}

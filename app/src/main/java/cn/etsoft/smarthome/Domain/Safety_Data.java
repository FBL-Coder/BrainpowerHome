package cn.etsoft.smarthome.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**+
 * 安防报警记录
 */

public class Safety_Data implements Serializable{

    public List<Safety_Data.Safety_Time> safetyTimes;

    public List<Safety_Data.Safety_Time> getSafetyTime() {
        if (safetyTimes == null)
            safetyTimes = new ArrayList<>();
        return safetyTimes;
    }

    public void setSafetyTime(List<Safety_Data.Safety_Time> datas) {
        this.safetyTimes = datas;
    }

    public class Safety_Time implements Serializable{

        public SetSafetyResult.SecInfoRowsBean  SafetyBean;
        public int year;
        public int month;
        public int day;
        public int h;
        public int m;
        public int s;


        public SetSafetyResult.SecInfoRowsBean getSafetyBean() {
            if (SafetyBean == null)
                SafetyBean = new SetSafetyResult.SecInfoRowsBean();
            return SafetyBean;
        }

        public void setSafetyBean(SetSafetyResult.SecInfoRowsBean SafetyBean) {
            this.SafetyBean = SafetyBean;
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

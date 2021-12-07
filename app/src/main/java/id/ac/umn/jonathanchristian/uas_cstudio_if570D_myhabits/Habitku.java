package id.ac.umn.jonathanchristian.uas_cstudio_if570D_myhabits;

public class Habitku {
    private String key;
    private String nama_habit;
    private String desc;
    private String date;
    private String time;


    public Habitku() {}
    public Habitku(String nama_habit,String desc, String date, String time) {
        this.nama_habit = nama_habit;
        this.desc = desc;
        this.date = date;
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama_habit() {
        return nama_habit;
    }

    public void setNama_habit(String nama_habit) {
        this.nama_habit = nama_habit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

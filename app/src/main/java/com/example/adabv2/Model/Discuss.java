package com.example.adabv2.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Discuss {

    @PrimaryKey(autoGenerate = true)
    private int class_id;
    @ColumnInfo(name = "class_lecturer_id")
    private int class_lecturer_id;
    @ColumnInfo(name = "class_name")
    private String class_name;
    @ColumnInfo(name = "class_code")
    private String class_code;
//    @ColumnInfo(name = "class_type")
//    private String class_type;

//    public String getClass_type() {
//        return class_type;
//    }
//
//    public void setClass_type(String class_type) {
//        this.class_type = class_type;
//    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getClass_lecturer_id() {
        return class_lecturer_id;
    }

    public void setClass_lecturer_id(int class_lecturer_id) {
        this.class_lecturer_id = class_lecturer_id;
    }
}

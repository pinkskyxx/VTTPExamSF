package sdf.mytaskTwo;

import java.io.File;

public class tasktwo {
    public static void main(String[] args) {
        System.out.println("tasktwo");
        String pName = args[0]; //"TEA_APRYL";
        File teawow = new File(pName);
        if (teawow.mkdir() == true) {
            System.out.println("Directory has been created successfully");
        } else {
            System.out.println("Directory cannot be created");
        }
    }
}

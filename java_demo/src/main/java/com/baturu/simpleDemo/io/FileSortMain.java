package com.baturu.simpleDemo.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSortMain {

    private static final String root = "/Users/xuran/";

    private static long fileCount = 0;
    private static long dicCount = 1;
    private List<File> files = new ArrayList<>();


    public FileSortMain() {

    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long startNano = System.nanoTime();
        File files = new File(root);
        FileSortMain fsm = new FileSortMain();
        fsm.dealFiles(files);
        long end = System.currentTimeMillis();
        long endNano = System.nanoTime();


        System.out.println("mill:"+(end-start));
        System.out.println("nano:"+(endNano-startNano));
        System.out.println("fileCount:"+fileCount+"  dicCount:"+dicCount);


        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    }

    private static void countFile(File file,boolean flag){
        if(flag){
            dicCount++;
        }else{
            fileCount++;
        }
    }

    private void dealFiles(File files){
        if(files == null){
            return ;
        }else{
            if(files.isDirectory()){
                dealWithDic(files);
            }else{
                dealWithFile(files);
            }
        }
    }

    private void dealWithDic(File file){
        files.add(file);
        countFile(file, true);
        for(File files:file.listFiles()){
            if(files.isDirectory()){
                dealWithDic(files);
            }else{
                dealWithFile(files);
            }
        }
    }

    private void dealWithFile(File file){
        countFile(file, false);
        files.add(file);
    }
}
//mill:169186
//nano:169194459958
//fileCount:123928  dicCount:24091

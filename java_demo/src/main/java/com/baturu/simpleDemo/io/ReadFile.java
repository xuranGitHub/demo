package com.baturu.simpleDemo.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xuran on 16/2/21.
 */
public class ReadFile {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<File> allFiles = readFile(new File("/Users/xuran/"));
        System.out.println("mill:" + (System.currentTimeMillis() - start));
        long fileCount = allFiles.stream()
                .filter(file -> file.isFile())
                .collect(Collectors.counting());
        long dirCount = allFiles.stream()
                .filter(file -> file.isDirectory())
                .collect(Collectors.counting());
        System.out.println("fileCount:" + fileCount + ", dirCount:" + dirCount);

    }

    public static List<File> readFile(File baseFile) {
        List<File> result = new ArrayList<>();
        result.add(baseFile);
        List<File> sonFiles = Arrays.asList(baseFile.listFiles());
        result.addAll(sonFiles.stream()
                .filter(file -> file.isFile())
                .collect(Collectors.toList()));
        result.addAll(sonFiles.stream()
                .filter(file -> file.isDirectory())
                .flatMap(dir -> readFile(dir).stream())
                .collect(Collectors.toList()));
        return result;
    }
}

package com.vm;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException{
        if (args.length != 1) throw new IllegalArgumentException();
        Vm vm = new Vm(args[0]);
        vm.start();
    }
}

class Vm {
    private int[] r = new int[4];
    private int[] ram = new int[0x1000];
    private byte[] bytecode = new byte[0x1010];
    private boolean exit;

    public Vm(String bytecode_url) throws IOException {
        File f_bytecode = new File(bytecode_url);
        if(!f_bytecode.exists()) throw new NoSuchFileException(f_bytecode.getName());

        FileInputStream fileInputStream = new FileInputStream(f_bytecode);
        fileInputStream.read(bytecode, 0, 0x1010);
        fileInputStream.close();
    }

    public void start(){
        int ip = 0;
        Scanner scanner = new Scanner(System.in);

        while (!exit && ip < 0x1000){
            ip ++;
            switch (bytecode[ip - 1]) {
                case 0x7f: exit = true; break;
                case 0x00: r[bytecode[ip]] += r[bytecode[ip+1]]; ip+=2; break;
                case 0x01: r[bytecode[ip]] -= r[bytecode[ip+1]]; ip+=2; break;
                case 0x10: r[bytecode[ip]] = scanner.nextByte(); ip++; break;
                case 0x20: System.out.println(r[bytecode[ip]]); ip++; break;
                case 0x30: ram[r[0]] = r[bytecode[ip]]; ip++; break;
                case 0x31: r[bytecode[ip]] = ram[r[0]]; ip++; break;
                case 0x32: r[bytecode[ip]] = bytecode[ip+1]; ip+=2; break;
            }
        }

        scanner.close();
    }


}

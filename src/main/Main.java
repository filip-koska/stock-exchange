package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import symulacja.Symulacja;


public class Main {
    public static void main(String[] args) {
        Symulacja sym = new Symulacja(args[0], args[1]);
        sym.symuluj();
    }
}
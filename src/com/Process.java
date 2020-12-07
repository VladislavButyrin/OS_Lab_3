package com;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Process {
    private int ProcessID;
    private PagesTable pageTable;

    public Process(int ProcessID) {
        this.ProcessID = ProcessID;
        System.out.println("New process : " + this.ProcessID + ";");
        pageTable = new PagesTable();
        pageTable.setProcessID(ProcessID);
    }

    public int getID() {
        return ProcessID;
    }

    public Page getNextPage() {
        return this.pageTable.getPage();
    }

    public Page getPage(int ind) {
        return pageTable.getPage(ind);
    }

    public void addPage(Page page) {
        pageTable.addPage(page);
    }

    public int getSize() {
        return pageTable.getSize();
    }
}
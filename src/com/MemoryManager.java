package com;


import java.util.ArrayList;
import java.util.LinkedList;

public class MemoryManager {
    private ArrayList<Process> processes = new ArrayList<Process>();
    private int memoryCapacity = 512;
    private int pageCapacity = 64;
    private PagesTable RAM = new PagesTable(memoryCapacity/pageCapacity);
    private LinkedList<Page> pagesQueue = new LinkedList<Page>();
    private int countPageCapacity = 0;

    public MemoryManager() {
        for (int i = 0; i < 5; i++) {
            processes.add(new Process(i));
        }
    }

    public void run() {
        while (!processes.isEmpty()) {
            for (Process process : processes) {
                if (RAM.checkContain(countPageCapacity)) {
                    swap(process);
                    countPageCapacity = 0;
                    continue;
                }
                System.out.println("Process : " + process.getID());
                System.out.println("\tLoad page");
                Page nowPage = process.getNextPage();

                if (nowPage != null) {
                    if (RAM.getSize() < RAM.getPagesCount()) {
                        System.out.println("\tPage : " + nowPage.getPageID() + " adding to RAM");
                        pagesQueue.add(nowPage);
                        RAM.addPage(nowPage);
                    }
                    else {
                        Page safePage;
                        while (true) {
                            safePage = pagesQueue.poll();
                            safePage.setApp();
                            if (safePage.getApp()) {
                                pagesQueue.add(safePage);
                            }
                            else {
                                pagesQueue.add(nowPage);
                                System.out.println("\tPage : " + safePage.getPageID() + " from process : " + safePage.getProcessID() + " remove from RAM");
                                RAM.removePage(safePage);
                                System.out.println("\tPage : " + nowPage.getPageID() + " adding to RAM");
                                countPageCapacity++;
                                RAM.addPage(nowPage);
                                for (Process safeProcess : processes) {
                                    if (safePage.getProcessID() == safeProcess.getID()) {
                                        safeProcess.addPage(safePage);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                else {
                    System.out.println("\tNULL new pages in process : " + process.getID());
                    RAM.removePage(pagesQueue.poll());
                    if (!RAM.havePageInProcess(process.getID())) {
                        System.out.println("\tProcess : " + process.getID() + " removing");
                        processes.remove(process);
                        break;
                    }

                }
            }
        }
    }

    public void swap(Process process) {
        System.out.println("Swapping start");
        for (int i = 0;i<process.getSize();i++) {
            Page safePage = pagesQueue.poll();
            if (safePage != null) {
                System.out.println("\tSave page " + safePage.getPageID() + " of process : " + safePage.getProcessID());
                RAM.removePage(safePage);
                for (Process safeProcess : processes) {
                    if (safePage.getProcessID() == safeProcess.getID()) {
                        safeProcess.addPage(safePage);
                        break;
                    }
                }
            }
            Page nowPage = process.getNextPage();
            System.out.println("\tAdding a page " + nowPage.getPageID() + " of  process : " + nowPage.getProcessID());
            pagesQueue.add(nowPage);
            RAM.addPage(nowPage);
        }
    }
}

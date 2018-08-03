package com.zzti.lsy.ninetingapp.utils;

import android.os.Process;
import android.support.v7.app.AppCompatActivity;


import com.zzti.lsy.ninetingapp.network.OkHttpManager;

import java.util.ArrayList;
import java.util.Stack;

public class ActivityStack {
    private ActivityStack() {
        stack = new Stack<>();
    }

    private static ActivityStack            ins;
    private Stack<AppCompatActivity> stack;

    public static ActivityStack get() {
        if (ins == null) {
            synchronized (ActivityStack.class) {
                if (ins == null) {
                    ins = new ActivityStack();
                }
            }
        }
        return ins;
    }

    public boolean add(AppCompatActivity activity) {
        return stack.add(activity);
    }

    public boolean remove(AppCompatActivity activity) {
        return stack.remove(activity);
    }

    public int finishActivityUntilEquals(Class<? extends AppCompatActivity> cls) {
        int finishCount = 0;
        if (cls != null && stack != null) {
            final int stackSize = stack.size();
            for (int currentIndex = stackSize - 1; currentIndex >= 0; currentIndex--) {
                AppCompatActivity currentItem = stack.get(currentIndex);
                if (currentItem.getClass()
                        .equals(cls))
                {
                    break;
                }
                boolean removed = stack.remove(currentItem);
                if (removed) {
                    finishCount++;
                }
            }
        }
        return finishCount;
    }

    public int finishActivity(Class<? extends AppCompatActivity> cls) {
        int finishCount = 0;
        if (cls != null && stack != null && stack.size() > 0) {
            final int                    stackSize              = stack.size();
            ArrayList<AppCompatActivity> waitFinishActivityList = new ArrayList<>();
            for (int currentIndex = stackSize - 1; currentIndex >= 0; currentIndex--) {
                AppCompatActivity currentItem = stack.get(currentIndex);
                if (currentItem.getClass()
                        .equals(cls))
                {
                    waitFinishActivityList.add(currentItem);
                }
            }
            finishCount = waitFinishActivityList.size();
            if (finishCount > 0) {
                for (int currentIndex = finishCount - 1; currentIndex >= 0; currentIndex--) {
                    AppCompatActivity activity = waitFinishActivityList.remove(currentIndex);
                    stack.remove(activity);
                    activity.finish();
                }
            }
        }
        return finishCount;
    }

    public void exit() {
        OkHttpManager.cancel(null);
        while (stack.size() > 0) {
            AppCompatActivity removeActivity = stack.remove(0);
            removeActivity.finish();
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}

package com.ysy.minimal;

import java.util.Date;

/**
 * User: ysy
 * Date: 2015/10/22
 * Time: 14:26
 */
public class ToDoItem {
    private String mToDoText;
    private boolean mHasReminder;
    private Date mToDoDate;

    public ToDoItem(String todoBody, boolean hasReminder, Date toDoDate) {
        mToDoText = todoBody;
        mHasReminder = hasReminder;
        mToDoDate = toDoDate;
    }

    public ToDoItem() {
        this("Clean my room", true, new Date());
    }

    public String getToDoText() {
        return mToDoText;
    }

    public void setToDoText(String mToDoText) {
        this.mToDoText = mToDoText;
    }

    public boolean HasReminder() {
        return mHasReminder;
    }

    public void setHasReminder(boolean mHasReminder) {
        this.mHasReminder = mHasReminder;
    }

    public Date getToDoDate() {
        return mToDoDate;
    }

    public void setToDoDate(Date mToDoDate) {
        this.mToDoDate = mToDoDate;
    }
}

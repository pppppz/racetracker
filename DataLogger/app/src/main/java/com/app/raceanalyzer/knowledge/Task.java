package com.app.raceanalyzer.knowledge;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Task")
public class Task extends ParseObject {
    public Task() {

    }

    public boolean isCompleted() {
        return getBoolean("completed");
    }

    public void setCompleted(boolean complete) {
        put("completed", complete);
    }

    public String getDescription() {
        return getString("description");
    }

    public String getTitle() {
        return getString("title");
    }

    public String getObjectID() {
        return getString("objectId");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public void setUser(ParseUser currentUser) {
        put("user", currentUser);
    }

    public void setDeadline(Date date) { put("deadlineAt", date); }

    public Date getdateDeadline() {
        Date date = getDate("deadlineAt");
        return date;

    }

}

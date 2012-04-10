package com.daumcorp.ua.report.bean;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String title;
	private List<Issue_> done = new ArrayList<Issue_>();
	private List<Issue_> todo = new ArrayList<Issue_>();
	private List<Issue_> someday = new ArrayList<Issue_>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Issue_> getDone() {
		return done;
	}

	public void setDone(List<Issue_> done) {
		this.done = done;
	}

	public List<Issue_> getTodo() {
		return todo;
	}

	public void setTodo(List<Issue_> todo) {
		this.todo = todo;
	}

	public List<Issue_> getSomeday() {
		return someday;
	}

	public void setSomeday(List<Issue_> someday) {
		this.someday = someday;
	}

	public void addIssue(Issue_ issue) {
		if ("In Progress".equals(issue.getStatus())) {
			todo.add(issue);
		} else if ("Open".equals(issue.getStatus())) {
			someday.add(issue);
		} else {
			done.add(issue);
		}
	}

	@Override
	public String toString() {
		return "Project [title=" + title + ", done=" + done + ", todo=" + todo
				+ "]";
	}

}

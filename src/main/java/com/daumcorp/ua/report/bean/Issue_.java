package com.daumcorp.ua.report.bean;

public class Issue_ {
	private String status;
	private String title;
	private String link;
	private String assignee;
	private String created;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "Issue [status=" + status + ", title=" + title + ", link="
				+ link + ", assignee=" + assignee + ", created=" + created
				+ "]";
	}

}

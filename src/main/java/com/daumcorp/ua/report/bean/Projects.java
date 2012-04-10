package com.daumcorp.ua.report.bean;

import java.util.ArrayList;
import java.util.List;

public class Projects {
	private List<Project> projects = new ArrayList<Project>();

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Project getProject(String title) {
		for (Project project : projects) {
			if (title.equals(project.getTitle())) {
				return project;
			}
		}

		Project project = new Project();
		project.setTitle(title);
		projects.add(project);
		return project;
	}

	@Override
	public String toString() {
		return "Projects [projects=" + projects + "]";
	}

}

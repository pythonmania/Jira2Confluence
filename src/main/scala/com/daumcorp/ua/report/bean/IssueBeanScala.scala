package com.daumcorp.ua.report.bean

import org.clapper.scalasti.StringTemplate
import com.daumcorp.ua.report.util._

class Issue(val project: String, val projectId: String, val status: String,
  val title: String, val link: String, val assignee: String, val created: String, val updated: String,
  val plannedStart: String, val plannedEnd: String, val component: String) {

  val pageContentTemplate = new StringTemplate("""{section}
{column:width=12%}
$component$
{column}
{column:width=54%}
[$title$|$link$]
{column}
{column}
$date1$
{column}
{column}
$date2$
{column}
{section}""")

  def toPageContent(date1: String, date2: String): String =
    pageContentTemplate.setAttributes(Map("component" -> component,
      "title" -> WikiUtil.escapeWikiText(title),
      "link" -> link, "date1" -> date1, "date2" -> date2)) toString

  def toProjectPageContent() =
    toPageContent(plannedStart, plannedEnd)

  def toPersonalPageContent() =
    toPageContent(created, updated)
}

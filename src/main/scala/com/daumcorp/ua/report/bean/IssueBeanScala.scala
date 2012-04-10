package com.daumcorp.ua.report.bean

class Issue(val project: String, val projectId: String, val status: String,
  val title: String, val link: String, val assignee: String, val created: String, val updated: String,
  val plannedStart: String, val plannedEnd: String, val component: String) {

  import com.daumcorp.ua.report.util._

  implicit def RichFormatter(string: String) = new {
    def richFormat(replacement: Map[String, Any]) =
      (string /: replacement) { (res, entry) => res.replaceAll("#\\{%s\\}".format(entry._1), WikiUtil.escapeWikiText(entry._2.toString)) }
  }

  def toPageContent(date1: String, date2: String) = """{section}
{column:width=12%}
#{component}
{column}
{column:width=54%}
[#{title}|#{link}]
{column}
{column}
#{date1}
{column}
{column}
#{date2}
{column}
{section}""" richFormat Map("component" -> component,
    "title" -> WikiUtil.escapeWikiText(title),
    "link" -> link, "date1" -> date1, "date2" -> date2)

  def toProjectPageContent() =
    toPageContent(plannedStart, plannedEnd)

  def toPersonalPageContent() =
    toPageContent(created, updated)
}

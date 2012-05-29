package com.daumcorp.ua.report.util

import java.text.ParseException

object DateUtil {
  import java.util.Calendar
  import java.text.SimpleDateFormat;

  val sdf = new SimpleDateFormat("yyyy-MM-dd");

  def getToday() = {
    import java.util.Date
    sdf.format(new Date)
  }

  def convertDateFormat(dateStr: String) = {
    try {
      import java.util.Locale
      val sdfIn = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
      val sdfOut = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
      sdfOut.format(sdfIn.parse(dateStr));
    } catch {
      case e: ParseException =>
        ""
    }
  }
}

object WikiUtil {
  def escapeWikiText(src: String) =
    src.replaceAll("\\[", "\\\\\\[").replaceAll("\\]", "\\\\\\]")

  implicit def RichFormatter(string: String) = new {
    def richFormat(replacement: Map[String, Any]) =
      (string /: replacement) { (res, entry) => res.replaceAll("#\\{%s\\}".format(entry._1), WikiUtil.escapeWikiText(entry._2.toString)) }
  }

  def makeSubtitle(subtitle: String, date1Title: String, date2Title: String) =
    """{section}
{column:width=66%}
{span:style=font-size:large;font-weight:bold;color:LimeGreen}#{subtitle}{span}
{column}
{column}
{span:style=font-weight:bold;color:LimeGreen}#{date1Title}{span}
{column}
{column}
{span:style=font-weight:bold;color:LimeGreen}#{date2Title}{span}
{column}
{section}""" richFormat Map("subtitle" -> subtitle, "date1Title" -> date1Title, "date2Title" -> date2Title)

}
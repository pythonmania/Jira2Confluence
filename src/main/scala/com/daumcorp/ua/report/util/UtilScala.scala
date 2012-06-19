package com.daumcorp.ua.report.util

import java.text.ParseException
import org.clapper.scalasti.StringTemplate

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
  def escapeWikiText(src: String): String = {
    src.replaceAll("\\[", "\\\\\\[").replaceAll("\\]", "\\\\\\]")
  }

  val subtitleTemplate = new StringTemplate("""{section}
{column:width=66%}
{span:style=font-size:large;font-weight:bold;color:LimeGreen}$subtitle${span}
{column}
{column}
{span:style=font-weight:bold;color:LimeGreen}$date1Title${span}
{column}
{column}
{span:style=font-weight:bold;color:LimeGreen}$date2Title${span}
{column}
{section}""")

  def makeSubtitle(subtitle: String, date1Title: String, date2Title: String): String =
    subtitleTemplate.setAttributes(Map("subtitle" -> subtitle, "date1Title" -> date1Title, "date2Title" -> date2Title)) toString
}
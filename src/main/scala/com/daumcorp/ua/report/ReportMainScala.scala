package com.daumcorp.ua.report

import scala.collection.JavaConversions._
import org.apache.xmlrpc.client._
import java.util.HashMap
import java.util.Calendar
import bean.Issue
import util._

object ReportMainScala {
  val projectPageQuery = """
		project = %s AND ((plannedStart >= -1w and plannedStart <= "0") or (plannedEnd >= -1w and plannedEnd <= "0"))
		"""
  val personalPageQuery = """
		(created >= -1w OR updated >= -1w OR status in ("In Progress", "Open"))
		 AND assignee in (sunrise, skyscrape, sooo, abyoh, noamom, dbellh, crpark98, ls15, chery, tweetyeunk)
		 AND (labels is EMPTY OR labels not in ("later", "plan"))
    	"""
  var username: String = _
  var password: String = _
  var token: String = _
  var client: XmlRpcClient = _

  def main(args: Array[String]) {
    if (args.length != 3) {
      print("please specify id, pw, pwdFile on commandline argument")
      return
    }

    username = args(0)
    password = decryptCredential(args(1), args(2))

    import scala.xml._

    confluenceLogin()

    // read project template page, extract target projects
    val projectPageContent = getProjectPageContent().lines;

    // create project page
    val projectPageRegex = "h3. (.*) \\((.*)\\).*".r
    val projectPageContents = new StringBuilder
    
    projectPageContent foreach {
      case projectPageRegex (project, projectId) => {
        projectPageContents ++= "h3. " + project + " (" + projectId + ")"
        val projectPageXML = XML.load(makeRequestURL(projectPageQuery.format(projectId)))
        val projectPageIssues = xmlToIssues(projectPageXML)
        val projectPageContent = issuesToProjectPageContent(projectPageIssues)
        projectPageContents ++= projectPageContent + "\n"
      }
      case line => projectPageContents ++= line + "\n"
    }
    println(projectPageContents)
    
    // create personal page
    val personalPageXML = XML.load(makeRequestURL(personalPageQuery))
    val personalPageIssues = xmlToIssues(personalPageXML)
    val personalPageContent: StringBuilder = issuesToPersonalPageContent(personalPageIssues)
    print(personalPageContent)

    publish(projectPageContents mkString, personalPageContent mkString)
  }

  def decryptCredential(pw: String, pwdFile: String) = {
    import scala.io.Source
    val passwordSeed = Source.fromFile(pwdFile).mkString.trim

    import org.jasypt.util.text._
    val textEncryptor: BasicTextEncryptor = new BasicTextEncryptor
    textEncryptor.setPassword(passwordSeed)

    val password = textEncryptor.decrypt(pw)

    password
  }

  def makeRequestURL(query: String): String = {
    import java.net.URLEncoder

    val requestURL = "http://issue.daumcorp.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=" +
      URLEncoder.encode(query, "UTF-8")

    requestURL + "&tempMax=1000" +
      "&os_username=" + username +
      "&os_password=" + password
  }

  def getProjectPageContent() = {
    val projectTemplatePage: HashMap[String, AnyRef] = getPage("프로젝트 템플릿", None)
    projectTemplatePage("content") toString
  }

  def xmlToIssues(inXML: scala.xml.Elem) = {
    val rawIssues = for (item <- inXML \ "channel" \ "item") yield {
      val project = item \ "project" text
      val projectId = item \ "project" \ "@id" text
      val status = item \ "status" text match {
        case "In Progress" => "진행중"
        case "Open" => "차주계획"
        case _ => "완료"
      }
      val title = item \ "title" text
      val link = item \ "link" text
      val assignee = item \ "assignee" text
      val created = DateUtil convertDateFormat (item \ "created" text)
      val updated = DateUtil convertDateFormat (item \ "updated" text)

      var plannedStart: String = ""
      var plannedEnd: String = ""
      item \ "customfields" \ "customfield" foreach { cfnode =>
        if ((cfnode \ "customfieldname").text == "plannedStart")
          plannedStart = DateUtil convertDateFormat (cfnode \ "customfieldvalues" \ "customfieldvalue" text)
        else if ((cfnode \ "customfieldname").text == "plannedEnd")
          plannedEnd = DateUtil convertDateFormat (cfnode \ "customfieldvalues" \ "customfieldvalue" text)
      }

      val component = item \ "component" text

      new Issue(project, projectId, status, title, link, assignee, created, updated, plannedStart, plannedEnd, component)
    }

    rawIssues
  }

  def issuesToProjectPageContent(rawIssues: Seq[Issue]) = {
    val output = new StringBuilder
    val issues = rawIssues.groupBy(_.project)

    issues.foreach(kv => {
      output ++= WikiUtil.makeSubtitle("", "plannedStart", "plannedEnd")

      val issue = kv._2.groupBy(_.component)
      issue.foreach(kv => {
        kv._2.foreach(kv => {
          output ++= "\n%s\n".format(kv.toProjectPageContent)
        })
      })
    })

    output
  }

  def issuesToPersonalPageContent(rawIssues: Seq[Issue]) = {
    val output = new StringBuilder
    val issues = rawIssues.groupBy(_.assignee)

    issues.foreach(kv => {
      output ++= "\nh1. %s\n".format(WikiUtil.escapeWikiText(kv._1))

      val projects = kv._2.groupBy(_.project)
      projects.foreach(kv => {
        var project = WikiUtil.escapeWikiText(kv._1)
        if (project.trim.isEmpty)
          project = "미지정"
        output ++= "\nh2. %s\n".format(project)

        val statuses = kv._2 groupBy (_.status)
        List("완료", "진행중", "차주계획") foreach (status => {
          if (statuses.get(status) != None) {
            output ++= WikiUtil.makeSubtitle(status, "created", "updated")

            val issue = statuses(status).groupBy(_.component)
            issue.foreach(kv => {
              kv._2.foreach(kv => {
                output ++= "\n%s\n".format(kv.toPersonalPageContent)
              })
            })
          }
        })
      })
    })

    output
  }

  def getPage(title: String, parentId: AnyRef): HashMap[String, AnyRef] = {
    try {
      val pageObj = client.execute("confluence1.getPage", seqAsJavaList(List(token, "uapd", title)))

      pageObj.asInstanceOf[HashMap[String, AnyRef]]
    } catch {
      case e: java.lang.Exception =>
        var newPage = new HashMap[String, AnyRef]
        newPage("space") = "uapd"
        newPage("title") = title
        if (parentId != None) {
          newPage("parentId") = parentId
        }
        val newPageObj = client.execute("confluence1.storePage", seqAsJavaList(List(token, newPage)));

        newPageObj.asInstanceOf[HashMap[String, AnyRef]]
    }
  }

  def getPageId(title: String, parentId: AnyRef): String = {
    getPage(title, parentId)("id") toString
  }

  def storePage(page: HashMap[String, AnyRef]): HashMap[String, AnyRef] = {
    val pageObj = client.execute("confluence1.storePage", seqAsJavaList(List(token, page)));

    pageObj.asInstanceOf[HashMap[String, AnyRef]]
  }

  def getQuarterPageTitle(): String = {
    val calendar: Calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val quarter: Integer = math.ceil(month / 3.0).toInt

    "%s년 %d분기".format(year, quarter)
  }

  def getCurrentChildPageTitle(): String = {
    val calendar: Calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

    "%s년 %s주차".format(year, weekOfYear)
  }

  def getCurrentProjectPageTitle(): String = {
    getCurrentChildPageTitle() + " 프로젝트"
  }

  def getCurrentPersonalPageTitle(): String = {
    getCurrentChildPageTitle() + " 개인업무"
  }

  def confluenceLogin() {
    import java.net.URL

    // setup confluence connection
    val config = new XmlRpcClientConfigImpl
    config.setServerURL(new URL("http://play.daumcorp.com/rpc/xmlrpc"))
    client = new XmlRpcClient()
    client.setConfig(config)

    // login
    token = client.execute("confluence1.login", seqAsJavaList(List(username, password))).toString;
    println("token:" + token)
  }

  def publish(projectPageContent: String, personalPageContent: String) {

    // create pages
    val currentPageTitle = DateUtil.getToday()
    val currentProjectPageTitle = getCurrentProjectPageTitle()
    val currentPersonalPageTitle = getCurrentPersonalPageTitle()

    val historyPageId: String = getPageId("과거이력", None)
    val quarterPageId: String = getPageId(getQuarterPageTitle(), historyPageId)
    val currentPageId: String = getPageId(currentPageTitle, quarterPageId)

    val currentProjectPage: HashMap[String, AnyRef] = getPage(getCurrentProjectPageTitle(), currentPageId)
    val currentPersonalPage: HashMap[String, AnyRef] = getPage(getCurrentPersonalPageTitle(), currentPageId)

    // create project page
    currentProjectPage("content") = projectPageContent
    storePage(currentProjectPage)

    // create personal page
    currentPersonalPage("content") = personalPageContent
    storePage(currentPersonalPage)

    // update thisweek page
    val linkPage: HashMap[String, AnyRef] = getPage("이번주", None);
    linkPage("content") = "{redirect:" + currentPageTitle + "|delay=0|visible=false}"
    storePage(linkPage)

    // logout
    client.execute("confluence1.logout", seqAsJavaList(List(token)));
  }
}

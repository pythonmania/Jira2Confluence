package com.daumcorp.ua.report

import org.scalatest.Assertions
import org.scalatest.matchers._
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import org.testng.Assert._
import org.testng.annotations._
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import org.apache.xmlrpc.client.XmlRpcClient
import java.net.URL
import java.util.HashMap

class ReportMainScalaSpec extends ShouldMatchers {

  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  val username = "____"
  val password = "____"
  var token: String = _
  var client: XmlRpcClient = _

  @BeforeClass
  def beforeClass() {
    val config = new XmlRpcClientConfigImpl
    config.setServerURL(new URL("http://play.daumcorp.com/rpc/xmlrpc"))
    client = new XmlRpcClient()
    client.setConfig(config)

    // login
    token = client.execute("confluence1.login", seqAsJavaList(List(username, password))) toString;
    println("token:" + token)
  }

  @BeforeMethod(enabled = false)
  def beforeMethod() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }

  @Test def getCurrentIssue() {
        val query = """
project = UPDUSERREFACTOR AND plannedEnd >=-1w and plannedEnd <= 0d
          """
    import java.net.URLEncoder

    val requestURL = "http://issue.daumcorp.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=" +
      URLEncoder.encode(query, "UTF-8")
      
    
  }
  
  @Test def getPlannedIssue() {
    
  }
  @Test(enabled = false) def crypt() {
    import org.jasypt.util.text._
    import org.jasypt.salt._
    import org.apache.commons.codec.binary.Base64
    import scala.io.Source

    val saltGenerator: SaltGenerator = new RandomSaltGenerator
    val textEncryptor: BasicTextEncryptor = new BasicTextEncryptor
    val password2 = Source.fromFile("C:/work/password.txt").mkString.trim
    println(password2)
    textEncryptor.setPassword(password2)
    val myEncryptedText: String = textEncryptor.encrypt(password)
    val plainText: String = textEncryptor.decrypt(myEncryptedText)

    println(myEncryptedText)
    println(plainText)
  }

  @Test(enabled = false) def storePage() {
    val historyPageId: String = getPageId("과거이력", None)
    val quarterPageId: String = getPageId("2012년 3분기", historyPageId)
    val currentPageId: String = getPageId("2012-08-01 ~ 2012-08-07", quarterPageId)

    val currentProjectPageId: String = getPageId("2012년 10주차 프로젝트", currentPageId)
    val currentPersonalPageId: String = getPageId("2012년 10주차 개인업무", currentPageId)
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

  def getParentPage(): HashMap[String, AnyRef] = {
    val parentPageTitle1 = "2011년 4분기"

    val parentPageObj = client.execute("confluence1.getPage", seqAsJavaList(List(token, "uapd", parentPageTitle1)))
    return parentPageObj.asInstanceOf[HashMap[String, AnyRef]]

    import java.util.Calendar
    import java.util.HashMap

    val historyPageObj = client.execute("confluence1.getPage", seqAsJavaList(List(token, "uapd", "과거이력")))
    val historyPage = historyPageObj.asInstanceOf[HashMap[String, AnyRef]]

    val calendar: Calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val quarter: Integer = math.ceil(month / 3.0).toInt
    val parentPageTitle = "%s년 %d분기".format(year, quarter)

    println(parentPageTitle)

    var parentPage = new HashMap[String, AnyRef]
    parentPage("space") = "uapd"
    parentPage("title") = parentPageTitle
    parentPage("parentId") = historyPage("id")

    try {
      client.execute("confluence1.storePage", seqAsJavaList(List(token, parentPage)));
    } catch {
      case e: org.apache.xmlrpc.XmlRpcException =>
        println(e + " creating new page...")
    }
    new HashMap[String, AnyRef]
  }

  @Test(enabled = false) def verifyEasy() { // Uses ScalaTest assertions
    sb.append("easy!")
    assert(sb.toString === "ScalaTest is easy!")
    assert(lb.isEmpty)
    lb += "sweet"
    intercept[StringIndexOutOfBoundsException] {
      "concise".charAt(-1)
    }
  }

  @Test(enabled = false) def verifyFun() { // Uses ScalaTest matchers
    sb.append("fun!")
    sb.toString should be("ScalaTest is fun!")
    lb should be('empty)
    lb += "sweet"
    evaluating { "concise".charAt(-1) } should produce[StringIndexOutOfBoundsException]
  }

  @Test(description = "11글자", enabled = false)
  def contain_11_characters() = {
    val helloworld = "Hello world"
    assert(helloworld.size == 11)
  }

  @Test(description = "Hello로 시작", enabled = false)
  def starts_with_Hello() = {
    val helloworld = "Hello world"
    assert(helloworld.startsWith("Hello") == true)
  }

  @Test(description = "world로 끝남", enabled = false)
  def ends_with_world() = {
    val helloworld = "Hello world"
    assert(helloworld.endsWith("world") == true)
  }
}
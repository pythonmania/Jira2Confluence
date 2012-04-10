package com.daumcorp.ua.report

import org.testng.annotations.Test

class PageContentMatchTest {
  val content = """{iframe:src=http://issue.daumcorp.com/browse/UPDUSERREFACTOR#selectedTab=de.polscheit.jira.plugins.gantt%3Agantt-panel|width=100%|height=800px}
{iframe}

h3. 회원정보 개편 2012 (UPDUSERREFACTOR)

h3. 광고주 회원정보 개편 2012 (UPDADUSERREFACTOR)
* 광고주 회원 아이디/비번찾기 일정에서 누락되어 일정을 1주 정도 연기
* 강주란님이 작성한 일정을 토대로 광고주 주민번호 삭제에 따른 개발일정 이슈로 등록
** 기획서 리뷰 후 개발시 기존 광고주 프로젝트는 소스 프리징, 크리티컬 이슈가 아닌이상 배포를 하지 않고 이후 개발은 리팩토링 코드에 적용할 예정
** 광고주 개발서버역시 리팩토링 소스로 대체하고 테스트 할 예정입니다.

h3. 회원정보 - 세션 인증 도입 (SPDSESSIONAUTH)
* auth.gif 수정으로 인해 2주 지연 예상
* 3/9 완료 예정인 일정 진행률 50%

h3. 회원정보 - 로그인 로그 시스템 개선 (SPDLOGINLOG)
"""
  @Test def testExtractProjects() {
    // find all h3
    // find enclosed project id
    val regex = ".*\\((.*)\\).*".r
    var result = ""
    for (line <- content.lines) {
      result ++= line + "\n"
      if (line.startsWith("h")) {
        val regex(projectId) = line
        result ++= getProjectContents(projectId) + "\n"
      }
    }

    println(result)
  }

  def getProjectContents(projectId: String) = projectId + "___"
}
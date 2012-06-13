package com.daumcorp.ua.report

import org.testng.annotations.Test

class PageContentMatchTest {
  @Test def testExtractProjects() {
    // find all h3
    // find enclosed project id
    val regex = "h3. (.*)\\((.*)\\).*".r
    content2.lines foreach {
      case regex(project, projectId) => println(project + "\t" + projectId)
      case line => 
    }
    //    for (line <- content.lines) {
    //      if (line.startsWith("h")) {
    //        val regex(projectId) = line
    //        println(projectId)
    //      }
    //    }
  }
val content2 = """h3. 회원정보 개편 2011112 (UPDUSERREFACTOR)
  h5. 회원정보 개편 2012222 (UPDUSERREFACTOR)
  h3. 회원정보 개편 2013332 (UPDUS한글ERREFACTOR)
  h3. 회원정보 개편 2014442 (UPDUSER__REFACTOR)
  """
  
  val content = """h3. 회원정보 개편 2012 (UPDUSERREFACTOR)
{iframe:src=http://110.45.227.147:9000/UPDUSERREFACTOR|width=100%|height=300px}
{iframe}
* 금주 목요일 nossn버전 staging 배포 예정
* 금일 id-nuser-dev에 nossn버전 운영DB를 이용하여 구동
* SSN삭제 테스트 진행중
* FT작업 진행중

{pre}

h3. 광고주 회원정보 개편 2012 (UPDADUSERREFACTOR)
{iframe:src=http://110.45.227.147:9000/UPDADUSERREFACTOR|width=100%|height=300px}
{iframe}
* 광고주 회원 아이디/비번찾기 일정에서 누락되어 일정을 1주 정도 연기
* 강주란님이 작성한 일정을 토대로 광고주 주민번호 삭제에 따른 개발일정 이슈로 등록
** 기획서 리뷰 후 개발시 기존 광고주 프로젝트는 소스 프리징, 크리티컬 이슈가 아닌이상 배포를 하지 않고 이후 개발은 리팩토링 코드에 적용할 예정
** 광고주 개발서버역시 리팩토링 소스로 대체하고 테스트 할 예정입니다.

h3. 회원정보 - 세션 인증 도입 (SPDSESSIONAUTH)
{iframe:src=http://110.45.227.147:9000/SPDSESSIONAUTH|width=100%|height=300px}
{iframe}
* couchbase 2.0 실서비스에 부하 적용 결과
** 신규로 서버 구매 신청
!cpu.png|border=1! !memory.png|border=1!

현재 ~ 7월 2주
* 구현
** client 여러대 사용
** 언어별 인증 확인 API sample 작성
** 회원정보 제공 API
* 인증 세션 저장 시스템 구축
* 기존 membase upgrade

7월 19일 ~ 7/31
* 일부 기능 적용 및 모니터링
** 로그인시 세션 생성
** 로그아웃시 세션 삭제
** auth.gif 에서 invalid한 경우는 cookie expire가 아니고 logging

8월 2일 ~ 
* 회원정보에 인증 WEB API 적용 후 모니터링
** 인증 확인한 결과는 logging, response는 true

?
* auth.gif 정상 작동하도록 수정
* 인증 WEB API 정상 작동하도록 수정
* 서비스에 적용 시작 (대상 서비스 선정)

h3. 회원정보 - 운영 (SPDMNT)
{iframe:src=http://110.45.227.147:9000/SPDMNT|width=100%|height=300px}
{iframe}
"""

}
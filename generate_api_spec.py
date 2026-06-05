"""
LOV3 게시판 프로젝트 REST API 명세서 Excel 생성 스크립트
실행: python3 generate_api_spec.py
"""
import openpyxl
from openpyxl.styles import (
    Font, PatternFill, Alignment, Border, Side, GradientFill
)
from openpyxl.utils import get_column_letter

wb = openpyxl.Workbook()

# ─────────────────────────────────────────────
# 색상 정의
# ─────────────────────────────────────────────
C_HEADER_BG   = "1F3864"   # 진한 네이비 (시트 헤더)
C_HEADER_FONT = "FFFFFF"   # 흰색
C_GET         = "D9EAD3"   # 연녹 (GET)
C_POST        = "FCE5CD"   # 연주황 (POST)
C_PUT         = "FFF2CC"   # 연노랑 (PUT)
C_DELETE      = "F4CCCC"   # 연빨강 (DELETE)
C_METHOD_GET  = "38761D"   # 진녹 (GET 텍스트)
C_METHOD_POST = "B45F06"   # 진주황
C_METHOD_PUT  = "7F6000"   # 진노랑
C_METHOD_DEL  = "990000"   # 진빨강
C_SECTION_BG  = "CFE2F3"   # 연파랑 (소제목)
C_AUTH_Y      = "FF9900"   # 오렌지 (인증 필요)
C_AUTH_N      = "38761D"   # 녹색 (인증 불필요)
C_FIELD_BG    = "F3F3F3"   # 연회색 (필드명)
C_ODD_ROW     = "FFFFFF"
C_EVEN_ROW    = "F9F9F9"

def thin_border():
    thin = Side(style="thin", color="CCCCCC")
    return Border(left=thin, right=thin, top=thin, bottom=thin)

def header_font(bold=True, size=10, color=C_HEADER_FONT):
    return Font(name="맑은 고딕", bold=bold, size=size, color=color)

def body_font(bold=False, size=9, color="000000"):
    return Font(name="맑은 고딕", bold=bold, size=size, color=color)

def fill(hex_color):
    return PatternFill("solid", fgColor=hex_color)

def align(h="left", v="center", wrap=False):
    return Alignment(horizontal=h, vertical=v, wrap_text=wrap)

METHOD_COLOR_MAP = {
    "GET":    (C_GET,    C_METHOD_GET),
    "POST":   (C_POST,   C_METHOD_POST),
    "PUT":    (C_PUT,    C_METHOD_PUT),
    "DELETE": (C_DELETE, C_METHOD_DEL),
}

# ─────────────────────────────────────────────
# 시트 1: API 전체 목록 (Overview)
# ─────────────────────────────────────────────
ws_ov = wb.active
ws_ov.title = "📋 API 전체목록"

# 타이틀
ws_ov.merge_cells("A1:J1")
ws_ov["A1"] = "LOV3 게시판 프로젝트 - REST API 명세서"
ws_ov["A1"].font = Font(name="맑은 고딕", bold=True, size=16, color=C_HEADER_FONT)
ws_ov["A1"].fill = fill(C_HEADER_BG)
ws_ov["A1"].alignment = align("center")
ws_ov.row_dimensions[1].height = 35

ws_ov.merge_cells("A2:J2")
ws_ov["A2"] = "서버 기본 URL: http://localhost:8087  |  인증 방식: JWT Bearer Token  |  Content-Type: application/json"
ws_ov["A2"].font = Font(name="맑은 고딕", size=10, color="444444")
ws_ov["A2"].alignment = align("center")
ws_ov.row_dimensions[2].height = 20

# 컬럼 헤더
ov_headers = ["No.", "도메인", "메서드", "URL", "기능 설명", "인증 필요", "요청 Body 여부", "응답 코드", "담당 컨트롤러", "비고"]
col_widths  = [5,    10,       8,        38,    30,           10,          14,               12,          22,               20]

for ci, (h, w) in enumerate(zip(ov_headers, col_widths), 1):
    cell = ws_ov.cell(row=3, column=ci, value=h)
    cell.font      = header_font()
    cell.fill      = fill("2E4057")
    cell.alignment = align("center")
    cell.border    = thin_border()
    ws_ov.column_dimensions[get_column_letter(ci)].width = w
ws_ov.row_dimensions[3].height = 22

# API 데이터
apis = [
    # (No, 도메인, 메서드, URL, 설명, 인증, Body여부, 응답코드, 컨트롤러, 비고)
    # ── 인증 ──────────────────────────────────────────────────────────────
    (1,  "인증(Auth)",   "POST",   "/login",                                   "로그인 (JWT 발급)",              "X",  "O", "200 OK",         "LoginFilter (Spring Security)", "응답 헤더에 Authorization 토큰 포함"),
    # ── 회원 ──────────────────────────────────────────────────────────────
    (2,  "회원(Member)", "POST",   "/members",                                 "회원가입",                       "X",  "O", "201 Created",    "MemberController",              ""),
    (3,  "회원(Member)", "GET",    "/members/check?id={id}",                   "ID 중복 확인",                   "X",  "X", "200 OK",         "MemberController",              "true=사용가능, false=중복"),
    (4,  "회원(Member)", "GET",    "/members/me",                              "내 정보 조회",                   "O",  "X", "200 OK",         "MemberController",              "JWT에서 회원 정보 추출"),
    (5,  "회원(Member)", "PUT",    "/members/me",                              "내 정보 수정",                   "O",  "O", "200 OK",         "MemberController",              ""),
    (6,  "회원(Member)", "DELETE", "/members/me",                              "회원 탈퇴",                      "O",  "X", "204 No Content", "MemberController",              ""),
    # ── 관리자 ────────────────────────────────────────────────────────────
    (7,  "관리자(Admin)","GET",    "/admin/members",                           "전체 회원 목록 조회",             "O",  "X", "200 OK",         "AdminController",               "ROLE_ADMIN만 접근 가능"),
    (8,  "관리자(Admin)","DELETE", "/admin/members/{memberNo}",                "회원 강제 탈퇴",                  "O",  "X", "204 No Content", "AdminController",               "ROLE_ADMIN만"),
    # ── 게시글 ────────────────────────────────────────────────────────────
    (9,  "게시글(Board)","GET",    "/boards",                                  "게시글 전체 목록 조회",           "X",  "X", "200 OK",         "BoardController",               ""),
    (10, "게시글(Board)","GET",    "/boards/{boardId}",                        "게시글 단건 조회",                "X",  "X", "200 OK",         "BoardController",               "조회수 +1 자동 증가"),
    (11, "게시글(Board)","GET",    "/boards/search?keyword={keyword}",         "게시글 검색 (제목/내용)",         "X",  "X", "200 OK",         "BoardController",               ""),
    (12, "게시글(Board)","POST",   "/boards",                                  "게시글 등록",                    "O",  "O", "201 Created",    "BoardController",               ""),
    (13, "게시글(Board)","PUT",    "/boards/{boardId}",                        "게시글 수정",                    "O",  "O", "200 OK",         "BoardController",               "본인만 수정 가능"),
    (14, "게시글(Board)","DELETE", "/boards/{boardId}",                        "게시글 삭제",                    "O",  "X", "204 No Content", "BoardController",               "본인 또는 관리자"),
    # ── 댓글 ──────────────────────────────────────────────────────────────
    (15, "댓글(Comment)","GET",    "/boards/{boardId}/comments",               "특정 게시글의 댓글 목록",         "X",  "X", "200 OK",         "CommentController",             ""),
    (16, "댓글(Comment)","POST",   "/boards/{boardId}/comments",               "댓글 등록",                      "O",  "O", "201 Created",    "CommentController",             ""),
    (17, "댓글(Comment)","PUT",    "/boards/{boardId}/comments/{commentId}",   "댓글 수정",                      "O",  "O", "200 OK",         "CommentController",             "본인만 수정 가능"),
    (18, "댓글(Comment)","DELETE", "/boards/{boardId}/comments/{commentId}",   "댓글 삭제",                      "O",  "X", "204 No Content", "CommentController",             "본인 또는 관리자"),
]

for ri, row in enumerate(apis, 4):
    method = row[2]
    bg, fg = METHOD_COLOR_MAP.get(method, ("FFFFFF", "000000"))
    row_bg = C_ODD_ROW if ri % 2 == 1 else C_EVEN_ROW

    for ci, val in enumerate(row, 1):
        cell = ws_ov.cell(row=ri, column=ci, value=val)
        cell.border    = thin_border()
        cell.alignment = align("center" if ci in [1,3,6,7,8] else "left")

        if ci == 1:  # No
            cell.fill = fill(row_bg)
            cell.font = body_font(bold=True)
        elif ci == 3:  # 메서드
            cell.fill = fill(bg)
            cell.font = Font(name="맑은 고딕", bold=True, size=9, color=fg)
        elif ci == 6:  # 인증 필요
            cell.font = Font(name="맑은 고딕", bold=True, size=9,
                             color=C_AUTH_Y if val == "O" else C_AUTH_N)
            cell.fill = fill(row_bg)
        else:
            cell.fill = fill(row_bg)
            cell.font = body_font()

    ws_ov.row_dimensions[ri].height = 18

ws_ov.freeze_panes = "A4"

# ─────────────────────────────────────────────
# 상세 시트 생성 함수
# ─────────────────────────────────────────────
def add_detail_sheet(wb, sheet_name, apis_detail):
    ws = wb.create_sheet(title=sheet_name)

    # 타이틀
    ws.merge_cells("A1:G1")
    ws["A1"] = f"LOV3 게시판 - {sheet_name} 상세 명세"
    ws["A1"].font = Font(name="맑은 고딕", bold=True, size=14, color=C_HEADER_FONT)
    ws["A1"].fill = fill(C_HEADER_BG)
    ws["A1"].alignment = align("center")
    ws.row_dimensions[1].height = 30

    col_widths = [6, 12, 40, 12, 55, 55, 30]
    col_headers = ["No.", "메서드", "URL", "인증필요", "요청(Request)", "응답(Response)", "비고/주의사항"]
    for ci, (h, w) in enumerate(zip(col_headers, col_widths), 1):
        cell = ws.cell(row=2, column=ci, value=h)
        cell.font      = header_font()
        cell.fill      = fill("2E4057")
        cell.alignment = align("center")
        cell.border    = thin_border()
        ws.column_dimensions[get_column_letter(ci)].width = w
    ws.row_dimensions[2].height = 22

    ri = 3
    for api in apis_detail:
        no, method, url, auth, req, res, note = api
        bg, fg = METHOD_COLOR_MAP.get(method, ("FFFFFF", "000000"))

        data = [no, method, url, auth, req, res, note]
        for ci, val in enumerate(data, 1):
            cell = ws.cell(row=ri, column=ci, value=val)
            cell.border    = thin_border()
            cell.alignment = align("center" if ci in [1,2,4] else "left", wrap=True)

            if ci == 2:
                cell.fill = fill(bg)
                cell.font = Font(name="맑은 고딕", bold=True, size=9, color=fg)
            elif ci == 4:
                cell.font = Font(name="맑은 고딕", bold=True, size=9,
                                 color=C_AUTH_Y if val == "O (JWT 필요)" else C_AUTH_N)
                cell.fill = fill(C_ODD_ROW if ri%2==1 else C_EVEN_ROW)
            else:
                cell.fill = fill(C_ODD_ROW if ri%2==1 else C_EVEN_ROW)
                cell.font = body_font()

        ws.row_dimensions[ri].height = 160
        ri += 1

    ws.freeze_panes = "A3"
    return ws


# ─────────────────────────────────────────────
# 시트 2: 인증 (Auth)
# ─────────────────────────────────────────────
auth_apis = [
    (
        1, "POST", "/login", "X (인증 불필요)",
        """■ 요청 방식: Form Data 또는 JSON Body

■ Content-Type: application/x-www-form-urlencoded
  또는 application/json

■ 요청 파라미터:
  username : 로그인 ID   (문자열, 필수)
  password : 비밀번호     (문자열, 필수)

■ 예시 (Form Data):
  username=hong123
  password=mypass1234

■ 예시 (JSON Body):
{
  "username": "hong123",
  "password": "mypass1234"
}""",
        """■ 성공 시: 200 OK

■ 응답 헤더:
  Authorization: Bearer eyJhbGciOi...

■ 응답 Body:
{
  "memberNo": 1,
  "id": "hong123",
  "name": "홍길동",
  "role": "ROLE_USER",
  "message": "로그인 성공"
}

■ 실패 시: 401 Unauthorized
{
  "message": "아이디 또는 비밀번호가 올바르지 않습니다.",
  "error": "UNAUTHORIZED"
}""",
        """★ 중요:
응답 헤더의 Authorization 값을
이후 모든 인증 필요 요청의
헤더에 포함해야 함

헤더 형식:
Authorization: Bearer {토큰값}

토큰 유효시간: 1시간
(LoginFilter.java에서 변경 가능)"""
    ),
]
add_detail_sheet(wb, "🔐 인증(Auth)", auth_apis)

# ─────────────────────────────────────────────
# 시트 3: 회원 (Member)
# ─────────────────────────────────────────────
member_apis = [
    (
        1, "POST", "/members", "X (인증 불필요)",
        """■ Content-Type: application/json

■ 요청 Body (JSON):
{
  "id":    "hong123",     ← 로그인 ID (필수, 중복불가)
  "pwd":   "mypass1234",  ← 비밀번호 (필수, 평문 전송)
  "name":  "홍길동",      ← 이름 (필수)
  "email": "hong@test.com" ← 이메일 (선택)
}

■ 필드 설명:
  id    : 영문+숫자, 4~30자
  pwd   : 8자 이상 권장
  name  : 최대 20자
  email : 이메일 형식""",
        """■ 성공 시: 201 Created

■ 응답 Body:
{
  "memberNo": 1,
  "id":       "hong123",
  "name":     "홍길동",
  "email":    "hong@test.com",
  "role":     "ROLE_USER",
  "regDate":  "2026-06-05T12:00:00"
}

■ 실패 시: 400 Bad Request
{
  "message": "이미 사용 중인 ID입니다.",
  "status":  "400"
}""",
        """비밀번호는 서버에서
BCrypt로 암호화하여 저장
(평문으로 저장되지 않음)

가입 후 자동으로
ROLE_USER 권한 부여"""
    ),
    (
        2, "GET", "/members/check?id={id}", "X (인증 불필요)",
        """■ 요청 방식: Query Parameter

■ URL 예시:
  GET /members/check?id=hong123

■ 파라미터:
  id : 확인할 로그인 ID (필수)""",
        """■ 성공 시: 200 OK

■ 응답 Body:
  true   ← 사용 가능 (중복 없음)
  false  ← 중복 (이미 존재)

■ 응답 예시:
  true""",
        """회원가입 폼에서
ID 입력 후 실시간
중복 확인에 사용"""
    ),
    (
        3, "GET", "/members/me", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ 요청 Body: 없음

■ 회원 번호는 JWT 토큰에서
  자동 추출 (별도 파라미터 불필요)""",
        """■ 성공 시: 200 OK

■ 응답 Body:
{
  "memberNo": 1,
  "id":       "hong123",
  "name":     "홍길동",
  "email":    "hong@test.com",
  "role":     "ROLE_USER",
  "regDate":  "2026-06-05T12:00:00"
}

■ 실패 시: 401 Unauthorized
  (토큰 없음 또는 만료)""",
        """비밀번호(pwd)는
응답에 포함되지 않음
(보안상 이유)"""
    ),
    (
        4, "PUT", "/members/me", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ 요청 Body (JSON):
{
  "name":  "홍길동수정",   ← 이름 (선택)
  "email": "new@test.com", ← 이메일 (선택)
  "pwd":   "newpass1234"   ← 새 비밀번호 (선택)
}

■ 변경하지 않을 필드는
  생략하거나 기존 값 유지""",
        """■ 성공 시: 200 OK

■ 응답 Body:
{
  "memberNo": 1,
  "id":       "hong123",
  "name":     "홍길동수정",
  "email":    "new@test.com",
  "role":     "ROLE_USER",
  "regDate":  "2026-06-05T12:00:00"
}""",
        """pwd 필드가 있으면
새 비밀번호로 변경
(BCrypt 재암호화)

ID(로그인 아이디)는
변경 불가"""
    ),
    (
        5, "DELETE", "/members/me", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ 요청 Body: 없음""",
        """■ 성공 시: 204 No Content
  (응답 Body 없음)

■ 실패 시: 401 Unauthorized""",
        """탈퇴 시 작성한 게시글,
댓글도 함께 삭제
(cascade 설정에 따라 다름)"""
    ),
]
add_detail_sheet(wb, "👤 회원(Member)", member_apis)

# ─────────────────────────────────────────────
# 시트 4: 관리자 (Admin)
# ─────────────────────────────────────────────
admin_apis = [
    (
        1, "GET", "/admin/members", "O (JWT + ROLE_ADMIN)",
        """■ 요청 헤더:
  Authorization: Bearer {관리자JWT토큰}

■ 요청 Body: 없음

■ ROLE_ADMIN 권한을 가진
  회원만 접근 가능""",
        """■ 성공 시: 200 OK

■ 응답 Body (배열):
[
  {
    "memberNo": 1,
    "id":       "hong123",
    "name":     "홍길동",
    "email":    "hong@test.com",
    "role":     "ROLE_USER",
    "regDate":  "2026-06-05T12:00:00"
  },
  {
    "memberNo": 2,
    "id":       "kim456",
    ...
  }
]

■ 실패 시: 403 Forbidden
  (ROLE_USER가 접근 시도)""",
        """ROLE_ADMIN 회원은
DB에서 직접 role 컬럼을
'ROLE_ADMIN'으로 변경

SecurityConfig에서
/admin/** 경로는
ADMIN만 허용으로 설정됨"""
    ),
    (
        2, "DELETE", "/admin/members/{memberNo}", "O (JWT + ROLE_ADMIN)",
        """■ 요청 헤더:
  Authorization: Bearer {관리자JWT토큰}

■ Path Variable:
  memberNo : 탈퇴시킬 회원 번호 (숫자)

■ URL 예시:
  DELETE /admin/members/5

■ 요청 Body: 없음""",
        """■ 성공 시: 204 No Content
  (응답 Body 없음)

■ 실패 시: 404 Not Found
{
  "message": "회원을 찾을 수 없습니다.",
  "status":  "404"
}

■ 실패 시: 403 Forbidden
  (ROLE_USER가 접근 시도)""",
        """관리자가 특정 회원을
강제 탈퇴시키는 기능"""
    ),
]
add_detail_sheet(wb, "🛡 관리자(Admin)", admin_apis)

# ─────────────────────────────────────────────
# 시트 5: 게시글 (Board)
# ─────────────────────────────────────────────
board_apis = [
    (
        1, "GET", "/boards", "X (인증 불필요)",
        """■ 요청 헤더: 없음
■ 요청 Body: 없음
■ Query Parameter: 없음""",
        """■ 성공 시: 200 OK

■ 응답 Body (배열):
[
  {
    "boardId":    1,
    "title":      "첫 번째 게시글",
    "content":    "내용입니다.",
    "viewCount":  5,
    "member": {
      "memberNo": 1,
      "id":       "hong123",
      "name":     "홍길동"
    },
    "regDate":    "2026-06-05T12:00:00",
    "updateDate": "2026-06-05T12:30:00",
    "commentCount": 3
  },
  ...
]""",
        """최신순 정렬을 원하면
BoardRepository에
OrderByRegDateDesc 추가"""
    ),
    (
        2, "GET", "/boards/{boardId}", "X (인증 불필요)",
        """■ Path Variable:
  boardId : 게시글 번호 (숫자)

■ URL 예시:
  GET /boards/1

■ 요청 Body: 없음""",
        """■ 성공 시: 200 OK

■ 응답 Body:
{
  "boardId":    1,
  "title":      "첫 번째 게시글",
  "content":    "내용입니다.",
  "viewCount":  6,
  "member": {
    "memberNo": 1,
    "id":       "hong123",
    "name":     "홍길동"
  },
  "regDate":    "2026-06-05T12:00:00",
  "updateDate": "2026-06-05T12:30:00",
  "commentCount": 3
}

■ 실패 시: 404 Not Found
{
  "message": "게시글을 찾을 수 없습니다.",
  "status":  "404"
}""",
        """조회할 때마다
viewCount가 1씩 증가"""
    ),
    (
        3, "GET", "/boards/search?keyword={keyword}", "X (인증 불필요)",
        """■ Query Parameter:
  keyword : 검색어 (문자열, 필수)

■ URL 예시:
  GET /boards/search?keyword=스프링

■ 제목과 내용 모두에서 검색""",
        """■ 성공 시: 200 OK

■ 응답 Body (배열):
  목록 조회와 동일한 구조

■ 검색 결과 없을 때:
  [] (빈 배열) - 200 OK""",
        """검색어가 제목 또는
내용에 포함된 게시글 반환

대소문자 구분 없이 검색
(MySQL LIKE 사용)"""
    ),
    (
        4, "POST", "/boards", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ 요청 Body (JSON):
{
  "title":   "게시글 제목",  ← 필수, 최대 100자
  "content": "게시글 내용"   ← 필수, 최대 2000자
}

■ 작성자(memberNo)는
  JWT 토큰에서 자동 추출
  (Body에 넣을 필요 없음)""",
        """■ 성공 시: 201 Created

■ 응답 Body:
{
  "boardId":    1,
  "title":      "게시글 제목",
  "content":    "게시글 내용",
  "viewCount":  0,
  "member": {
    "memberNo": 1,
    "id":       "hong123",
    "name":     "홍길동"
  },
  "regDate":    "2026-06-05T12:00:00",
  "updateDate": "2026-06-05T12:00:00",
  "commentCount": 0
}""",
        """title, content 둘 다
필수 입력값"""
    ),
    (
        5, "PUT", "/boards/{boardId}", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ Path Variable:
  boardId : 수정할 게시글 번호

■ URL 예시:
  PUT /boards/1

■ 요청 Body (JSON):
{
  "title":   "수정된 제목",
  "content": "수정된 내용"
}""",
        """■ 성공 시: 200 OK

■ 응답 Body:
  게시글 상세와 동일한 구조
  (수정된 내용 반영)

■ 실패 시: 403 Forbidden
{
  "message": "본인의 게시글만 수정할 수 있습니다.",
  "status":  "403"
}

■ 실패 시: 404 Not Found""",
        """본인이 작성한 게시글만
수정 가능

관리자도 수정 불가
(본인만)"""
    ),
    (
        6, "DELETE", "/boards/{boardId}", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ Path Variable:
  boardId : 삭제할 게시글 번호

■ URL 예시:
  DELETE /boards/1

■ 요청 Body: 없음""",
        """■ 성공 시: 204 No Content
  (응답 Body 없음)

■ 실패 시: 403 Forbidden
{
  "message": "삭제 권한이 없습니다.",
  "status":  "403"
}

■ 실패 시: 404 Not Found""",
        """본인 또는 관리자만 삭제

게시글 삭제 시
달린 댓글도 함께 삭제
(cascade 설정)"""
    ),
]
add_detail_sheet(wb, "📝 게시글(Board)", board_apis)

# ─────────────────────────────────────────────
# 시트 6: 댓글 (Comment)
# ─────────────────────────────────────────────
comment_apis = [
    (
        1, "GET", "/boards/{boardId}/comments", "X (인증 불필요)",
        """■ Path Variable:
  boardId : 게시글 번호

■ URL 예시:
  GET /boards/1/comments

■ 요청 Body: 없음""",
        """■ 성공 시: 200 OK

■ 응답 Body (배열, 작성일 오름차순):
[
  {
    "commentId":  1,
    "content":    "좋은 글이네요!",
    "member": {
      "memberNo": 2,
      "id":       "kim456",
      "name":     "김철수"
    },
    "boardId":    1,
    "regDate":    "2026-06-05T13:00:00",
    "updateDate": "2026-06-05T13:00:00"
  },
  ...
]""",
        """댓글이 없을 때:
[] (빈 배열) - 200 OK"""
    ),
    (
        2, "POST", "/boards/{boardId}/comments", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ Path Variable:
  boardId : 댓글 달 게시글 번호

■ URL 예시:
  POST /boards/1/comments

■ 요청 Body (JSON):
{
  "content": "댓글 내용입니다."  ← 필수, 최대 500자
}""",
        """■ 성공 시: 201 Created

■ 응답 Body:
{
  "commentId":  1,
  "content":    "댓글 내용입니다.",
  "member": {
    "memberNo": 1,
    "id":       "hong123",
    "name":     "홍길동"
  },
  "boardId":    1,
  "regDate":    "2026-06-05T13:00:00",
  "updateDate": "2026-06-05T13:00:00"
}

■ 실패 시: 404 Not Found
  (게시글이 없을 때)""",
        """"""
    ),
    (
        3, "PUT", "/boards/{boardId}/comments/{commentId}", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ Path Variable:
  boardId   : 게시글 번호
  commentId : 수정할 댓글 번호

■ URL 예시:
  PUT /boards/1/comments/3

■ 요청 Body (JSON):
{
  "content": "수정된 댓글 내용"
}""",
        """■ 성공 시: 200 OK

■ 응답 Body:
  댓글 등록과 동일한 구조
  (수정된 내용 반영)

■ 실패 시: 403 Forbidden
{
  "message": "본인의 댓글만 수정할 수 있습니다.",
  "status":  "403"
}

■ 실패 시: 404 Not Found""",
        """본인만 수정 가능
관리자도 수정 불가"""
    ),
    (
        4, "DELETE", "/boards/{boardId}/comments/{commentId}", "O (JWT 필요)",
        """■ 요청 헤더:
  Authorization: Bearer {JWT토큰}

■ Path Variable:
  boardId   : 게시글 번호
  commentId : 삭제할 댓글 번호

■ URL 예시:
  DELETE /boards/1/comments/3

■ 요청 Body: 없음""",
        """■ 성공 시: 204 No Content
  (응답 Body 없음)

■ 실패 시: 403 Forbidden
{
  "message": "삭제 권한이 없습니다.",
  "status":  "403"
}

■ 실패 시: 404 Not Found""",
        """본인 또는 관리자만 삭제"""
    ),
]
add_detail_sheet(wb, "💬 댓글(Comment)", comment_apis)

# ─────────────────────────────────────────────
# 시트 7: DB 테이블 설계
# ─────────────────────────────────────────────
ws_db = wb.create_sheet(title="🗄 DB 테이블 설계")

ws_db.merge_cells("A1:G1")
ws_db["A1"] = "LOV3 게시판 - DB 테이블 설계"
ws_db["A1"].font = Font(name="맑은 고딕", bold=True, size=14, color=C_HEADER_FONT)
ws_db["A1"].fill = fill(C_HEADER_BG)
ws_db["A1"].alignment = align("center")
ws_db.row_dimensions[1].height = 30

tables = [
    # (테이블명, 설명, [(컬럼명, 타입, 제약조건, 설명)])
    ("member", "회원 테이블", [
        ("member_no", "BIGINT",      "PK, AUTO_INCREMENT",          "회원 번호 (기본키, 자동 증가)"),
        ("id",        "VARCHAR(30)", "UNIQUE, NOT NULL",             "로그인 ID"),
        ("pwd",       "VARCHAR(255)","NOT NULL",                     "비밀번호 (BCrypt 암호화)"),
        ("name",      "VARCHAR(20)", "NOT NULL",                     "이름"),
        ("email",     "VARCHAR(50)", "UNIQUE",                       "이메일"),
        ("role",      "VARCHAR(20)", "NOT NULL",                     "권한 (ROLE_USER / ROLE_ADMIN)"),
        ("reg_date",  "DATETIME",    "DEFAULT CURRENT_TIMESTAMP",    "가입일 (자동 입력)"),
    ]),
    ("board", "게시글 테이블", [
        ("board_id",    "BIGINT",        "PK, AUTO_INCREMENT",                 "게시글 번호 (기본키)"),
        ("title",       "VARCHAR(100)",  "NOT NULL",                            "제목"),
        ("content",     "TEXT",          "NOT NULL",                            "내용"),
        ("view_count",  "INT",           "DEFAULT 0",                           "조회수"),
        ("member_no",   "BIGINT",        "FK → member.member_no, NOT NULL",    "작성자 (외래키)"),
        ("reg_date",    "DATETIME",      "DEFAULT CURRENT_TIMESTAMP",           "작성일"),
        ("update_date", "DATETIME",      "ON UPDATE CURRENT_TIMESTAMP",         "수정일"),
    ]),
    ("comment", "댓글 테이블", [
        ("comment_id",  "BIGINT",       "PK, AUTO_INCREMENT",                  "댓글 번호 (기본키)"),
        ("content",     "VARCHAR(500)", "NOT NULL",                             "댓글 내용"),
        ("board_id",    "BIGINT",       "FK → board.board_id, NOT NULL",       "게시글 (외래키)"),
        ("member_no",   "BIGINT",       "FK → member.member_no, NOT NULL",     "작성자 (외래키)"),
        ("reg_date",    "DATETIME",     "DEFAULT CURRENT_TIMESTAMP",            "작성일"),
        ("update_date", "DATETIME",     "ON UPDATE CURRENT_TIMESTAMP",          "수정일"),
    ]),
]

ws_db.column_dimensions["A"].width = 16
ws_db.column_dimensions["B"].width = 16
ws_db.column_dimensions["C"].width = 36
ws_db.column_dimensions["D"].width = 22

ri = 3
for tbl_name, tbl_desc, cols in tables:
    # 테이블 이름 헤더
    ws_db.merge_cells(f"A{ri}:D{ri}")
    cell = ws_db.cell(row=ri, column=1, value=f"📌 {tbl_name}  ({tbl_desc})")
    cell.font      = Font(name="맑은 고딕", bold=True, size=11, color="1F3864")
    cell.fill      = fill(C_SECTION_BG)
    cell.alignment = align("left")
    cell.border    = thin_border()
    ws_db.row_dimensions[ri].height = 22
    ri += 1

    # 컬럼 헤더
    col_headers = ["컬럼명", "데이터 타입", "제약 조건", "설명"]
    for ci, h in enumerate(col_headers, 1):
        cell = ws_db.cell(row=ri, column=ci, value=h)
        cell.font      = header_font(size=9)
        cell.fill      = fill("2E4057")
        cell.alignment = align("center")
        cell.border    = thin_border()
    ws_db.row_dimensions[ri].height = 20
    ri += 1

    # 컬럼 데이터
    for j, (col_name, dtype, constraint, desc) in enumerate(cols):
        row_bg = C_ODD_ROW if j % 2 == 0 else C_EVEN_ROW
        data = [col_name, dtype, constraint, desc]
        for ci, val in enumerate(data, 1):
            cell = ws_db.cell(row=ri, column=ci, value=val)
            cell.font = Font(name="맑은 고딕", bold=(ci == 1), size=9)
            cell.fill = fill(row_bg)
            cell.alignment = align("left")
            cell.border = thin_border()
        ws_db.row_dimensions[ri].height = 18
        ri += 1

    ri += 1  # 테이블 간 빈 줄

# ─────────────────────────────────────────────
# 시트 8: HTTP 상태코드 가이드
# ─────────────────────────────────────────────
ws_guide = wb.create_sheet(title="📖 HTTP 상태코드 가이드")
ws_guide.merge_cells("A1:D1")
ws_guide["A1"] = "HTTP 상태 코드 가이드 (코딩 중 참고용)"
ws_guide["A1"].font = Font(name="맑은 고딕", bold=True, size=14, color=C_HEADER_FONT)
ws_guide["A1"].fill = fill(C_HEADER_BG)
ws_guide["A1"].alignment = align("center")
ws_guide.row_dimensions[1].height = 30

ws_guide.column_dimensions["A"].width = 8
ws_guide.column_dimensions["B"].width = 22
ws_guide.column_dimensions["C"].width = 18
ws_guide.column_dimensions["D"].width = 50

guide_headers = ["코드", "이름", "언제 사용?", "코드 작성 방법"]
for ci, h in enumerate(guide_headers, 1):
    cell = ws_guide.cell(row=2, column=ci, value=h)
    cell.font = header_font()
    cell.fill = fill("2E4057")
    cell.alignment = align("center")
    cell.border = thin_border()
ws_guide.row_dimensions[2].height = 22

status_codes = [
    ("200", "OK",               "조회, 수정 성공",              "return ResponseEntity.ok(result);"),
    ("201", "Created",          "등록(생성) 성공",              "return ResponseEntity.status(HttpStatus.CREATED).body(result);"),
    ("204", "No Content",       "삭제 성공 (응답 없음)",        "return ResponseEntity.noContent().build();"),
    ("400", "Bad Request",      "요청 데이터가 잘못됨",         "throw new MemberException(\"메시지\");"),
    ("401", "Unauthorized",     "로그인 필요 (인증 안됨)",      "JWT 토큰 없거나 만료 시 자동 처리"),
    ("403", "Forbidden",        "권한 없음 (다른 사람 글 수정)","throw new BoardException(\"메시지\", HttpStatus.FORBIDDEN);"),
    ("404", "Not Found",        "데이터를 찾을 수 없음",        "throw new BoardException(\"게시글을 찾을 수 없습니다.\", HttpStatus.NOT_FOUND);"),
    ("500", "Internal Error",   "서버 오류 (버그)",             "서버에서 예상치 못한 에러 발생 시"),
]

code_colors = {
    "200": C_GET, "201": C_POST, "204": C_DELETE,
    "400": C_DELETE, "401": C_DELETE, "403": C_DELETE,
    "404": C_PUT, "500": C_DELETE,
}

for ri, (code, name, when, how) in enumerate(status_codes, 3):
    row_bg = C_ODD_ROW if ri % 2 == 1 else C_EVEN_ROW
    data = [code, name, when, how]
    for ci, val in enumerate(data, 1):
        cell = ws_guide.cell(row=ri, column=ci, value=val)
        cell.border = thin_border()
        cell.alignment = align("center" if ci <= 3 else "left", wrap=True)
        if ci == 1:
            cell.fill = fill(code_colors.get(code, row_bg))
            cell.font = Font(name="맑은 고딕", bold=True, size=10)
        else:
            cell.fill = fill(row_bg)
            cell.font = body_font()
    ws_guide.row_dimensions[ri].height = 22

# ─────────────────────────────────────────────
# 파일 저장
# ─────────────────────────────────────────────
output_path = "/Users/hongjunhwa/Desktop/Personal_Project/work/lov3_board_project/LOV3_게시판_REST_API_명세서.xlsx"
wb.save(output_path)
print(f"✅ 엑셀 명세서 생성 완료: {output_path}")

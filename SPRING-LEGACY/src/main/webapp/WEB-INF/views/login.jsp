<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
</head>
<body>

<h2>로그인</h2>

<form action="${pageContext.request.contextPath}/loginProc" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  <div>
    <label>Email</label><br/>
    <input type="text" name="email" required />
  </div>

  <div>
    <label>Password</label><br/>
    <input type="password" name="password" required />
  </div>

  <button type="submit">로그인</button>
</form>

<c:if test="${param.error != null}">
  <p style="color:red;">아이디 또는 비밀번호가 올바르지 않습니다.</p>
</c:if>

<c:if test="${param.logout != null}">
  <p style="color:green;">로그아웃되었습니다.</p>
</c:if>

<hr/>

<p>
  아직 회원이 아니신가요?
  <a href="${pageContext.request.contextPath}/members/new">회원가입</a>
</p>

</body>
</html>

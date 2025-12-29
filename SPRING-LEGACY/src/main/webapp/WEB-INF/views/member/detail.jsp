<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Member Detail</title>
  <style>
    .row { margin: 10px 0; }
    .btn { display:inline-block; padding:6px 10px; border:1px solid #ccc; border-radius:6px; text-decoration:none; }
  </style>
</head>
<body>
<h2>회원 상세</h2>

<c:if test="${empty member}">
  <p>해당 회원이 없습니다.</p>
  <a class="btn" href="${pageContext.request.contextPath}/members">목록</a>
</c:if>

<c:if test="${not empty member}">
  <div class="row">userno: <b><c:out value="${member.userno}"/></b></div>
  <div class="row">name: <b><c:out value="${member.name}"/></b></div>
  <div class="row">age: <b><c:out value="${member.age}"/></b></div>

  <a class="btn" href="${pageContext.request.contextPath}/members/${member.userno}/edit">수정</a>

  <form action="${pageContext.request.contextPath}/members/${member.userno}/delete"
        method="post" style="display:inline;">
    <button class="btn" type="submit" onclick="return confirm('삭제할까요?');">삭제</button>
  </form>

  <a class="btn" href="${pageContext.request.contextPath}/members">목록</a>
</c:if>
</body>
</html>

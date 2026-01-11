<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Edit Member</title>
  <style>
    label { display:block; margin:10px 0 6px; }
    input { padding:8px; width: 300px; }
    .btn { margin-top: 12px; padding: 8px 12px; }
    .link { margin-left: 8px; }
  </style>
</head>
<body>
<h2>회원 수정</h2>

<c:if test="${empty member}">
  <p>수정할 회원이 없습니다.</p>
  <a href="${pageContext.request.contextPath}/members">목록</a>
</c:if>

<c:if test="${not empty member}">
  <form action="${pageContext.request.contextPath}/members/${member.memberId}/edit" method="post">
    <label>memberId</label>
    <input type="text" value="<c:out value='${member.memberId}'/>" readonly />

    <label>name</label>
    <input type="text" name="name" value="<c:out value='${member.name}'/>" required />

    <label>age</label>
    <input type="number" name="age" min="0" value="<c:out value='${member.age}'/>" required />

    <button class="btn" type="submit">저장</button>
    <a class="link" href="${pageContext.request.contextPath}/members/${member.memberId}">취소</a>
  </form>
</c:if>
</body>
</html>

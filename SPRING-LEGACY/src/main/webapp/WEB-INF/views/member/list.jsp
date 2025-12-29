<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Member List</title>
    <style>
        table { border-collapse: collapse; width: 600px; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background: #f5f5f5; text-align: left; }
        .top { margin-bottom: 12px; }
        a { text-decoration: none; }
        .btn { display:inline-block; padding:6px 10px; border:1px solid #ccc; border-radius:6px; }
    </style>
</head>
<body>
<h2>회원 목록</h2>

<div class="top">
    <a class="btn" href="${pageContext.request.contextPath}/members/new">+ 신규 등록</a>
</div>

<table>
    <thead>
    <tr>
        <th>userno</th>
        <th>name</th>
        <th>age</th>
        <th>action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="m" items="${members}">
        <tr>
            <td><c:out value="${m.userno}"/></td>
            <td>
                <a href="${pageContext.request.contextPath}/members/${m.userno}">
                    <c:out value="${m.name}"/>
                </a>
            </td>
            <td><c:out value="${m.age}"/></td>
            <td>
                <a class="btn" href="${pageContext.request.contextPath}/members/${m.userno}/edit">수정</a>

                <form action="${pageContext.request.contextPath}/members/${m.userno}/delete"
                      method="post" style="display:inline;">
                    <button class="btn" type="submit" onclick="return confirm('삭제할까요?');">삭제</button>
                </form>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty members}">
        <tr>
            <td colspan="4">데이터가 없습니다.</td>
        </tr>
    </c:if>
    </tbody>
</table>
</body>
</html>

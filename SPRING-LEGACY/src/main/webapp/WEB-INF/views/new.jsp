<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>New Member</title>
  <style>
    label { display:block; margin:10px 0 6px; }
    input { padding:8px; width: 300px; }
    .btn { margin-top: 12px; padding: 8px 12px; }
    .link { margin-left: 8px; }
  </style>
</head>
<body>
<h2>회원 등록</h2>

<form action="${pageContext.request.contextPath}/members" method="post">


  <label>email</label>
  <input type="text" name="email" required /><br/><br/>


  <label>password</label>
  <input type="password" name="passwordHash" required />

  <label>name</label>
  <input type="text" name="name" required /><br/><br/>

  <label>age</label>
  <input type="number" name="age" min="0" required />

  <button class="btn" type="submit">등록</button>
  <a class="link" href="${pageContext.request.contextPath}/members">목록</a>
</form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>myTitle</title>
</head>
<body>
123
$.ajax({
url:"",
data:{//前端传给后台的参数

},
type:"",
dataType:"json",
success:function (data) {

}
})

//创建时间：当前系统时间
String createTime = DateTimeUtil.getSysTime();
//创建人：当前登录用户
String createBy = ((User)request.getSession().getAttribute("user")).getName();

</body>
</html>
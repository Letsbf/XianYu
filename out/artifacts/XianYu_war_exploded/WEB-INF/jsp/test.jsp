<%--
  Created by IntelliJ IDEA.
  User: tong
  Date: 2018/3/30
  Time: 下午10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>testAjax</title>
</head>
<body>
    <%= request.getAttribute("msg")%>

    <div id="myDiv"><h3>嘤嘤嘤</h3></div>
    <button type="button" onclick="ajaxx()">点击获取ajax数据</button>
    <script type="text/javascript">
        function ajaxx() {
            var xhr =  xmlHttp=new XMLHttpRequest();
            xhr.onreadystatechange=function(){
                if(xhr.readyState==4)
                {
                    if(xhr.status==200)
                    {
                        alert(this.responseText);
                        document.getElementById("mytext").innerHTML=xhr.responseText;
                    }
                }
            }
            xhr.open("POST","test3");
            xhr.send();
        }
    </script>
</body>
</html>

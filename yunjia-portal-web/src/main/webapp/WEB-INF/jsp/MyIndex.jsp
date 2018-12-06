<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>正品低价、品质保障、货到付款、配送及时、放心服务、轻松购物！</title>
<link href="/css/taotao.css" rel="stylesheet"/>
<script src="../js/jquery-1.6.4.js"></script>
<style type="text/css">
	.solr{
		width: 472px;
		height: 36px;
		border: solid 2px #ff5600;
		position: relative;
		left: 30%;
		border-radius : 30px 0 0 30px;
		//background: url('http://192.168.25.128/group1/M00/00/00/wKgZgFvZf7CAdoQEAABBVvy2EBo499.png') no-repeat;
		//background-position: 3%;
	}
	.search_icon{
		position: absolute;
		left: 31%;
		z-index: 10;
	}
	.search_btn{
		border: solid 2px #ff5600;
		border-radius: 0 25px 25px 0;
		width: 74px;
		height: 42px;
		position: absolute;
		left: 60.8%;
		background-color: #ff5600;
	}
	.search_btn:hover{
		cursor: pointer;
	}
	.top{
		margin-top: 25px;
	}
</style>

</head>
<body>
	<jsp:include page="commons/shortcut.jsp" />
	<div></div>
	<div class="top">
		<div>
			<div class="search_icon"><img src="http://192.168.25.128/group1/M00/00/00/wKgZgFvZf7CAdoQEAABBVvy2EBo499.png"></img></div>
			<button id="search_btn" class="search_btn">搜索</button>
			<input class="solr" type="text"></input>
		</div>
	</div>
		<script>
	var s = document.getElementById("solr");
	s.onClick = function(){
		alert(1);
	}
</script>
</body>

</html>
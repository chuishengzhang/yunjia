<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<a id="solr" class="easyui-linkbutton">导入Solr索引库</a>
	<img id="load" style="display:none" src="loading.gif" />
	
	<script>
		$(function(){
			$('#solr').click(function(){
				$('#load').show();
				$.post('http://localhost:8086/solr/import',function(data){
					if(data.status == 200){
						$('#load').hide();
						alert("ggg")
					}
				});
			});
		});
	</script>
</body>
</html>
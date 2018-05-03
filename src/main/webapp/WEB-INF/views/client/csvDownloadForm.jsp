<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="layoutTag" tagdir="/WEB-INF/tags"%>
<layoutTag:layout>


	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Csv Download</title>
</head>
<body>

	<div class="container">
		<form class="form-inline" action="sectionProc" method="post">
			<div class="form-group">
				<label for="fileName">제목</label> <input type="text"
					class="form-control" id="fileName" name="fileName"
					placeholder="제목을 입력하세요.">
			</div>

			<div class="form-group">
				<label for="startNum">시작</label> <input type="text"
					class="form-control" id="startNum" name="startNum"
					placeholder="시작할 행 번호를 입력하세요.">
			</div>

			<div class="form-group">
				<label for="lastNum">끝</label> <input type="text"
					class="form-control" id="lastNum" name="lastNum"
					placeholder="끝낼 행 번호를 입력하세요.">
			</div>

			<button type="submit" class="btn btn-primary btn-sm">다운</button>

		</form>
	</div>
</body>
	</html>
</layoutTag:layout>

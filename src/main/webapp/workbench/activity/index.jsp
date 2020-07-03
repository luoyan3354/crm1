<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//日期控件
		$(".time").datetimepicker({
			minView: "month",
			language: 'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//打开创建表单的模态窗口
		$("#addBtn").click(function () {
			/*操作模态窗口的方式：
			 需要操作的模态窗口的jquery对象，调用model方法，为该方法传递参数 show：打开模态窗口 hide：关闭模态窗口
			*/
			//alert("123");//测试打开模态窗口之前要进行一些操作
			//$("#createActivityModal").modal("show");

			//走后台，目的是为了取得用户信息列表，为owner下拉框铺值，默认选中当前登录名为owner
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data) {
					/*
						data
						    [{"id":?,"name":?,"loginAct":?...},{用户2},{用户3}...]
					 */

					var html = "<option></option>";
					//遍历出来的每一个n，就是每一个user对象
					$.each(data,function (i,n) {
						//value属性是用户表中的id uuid啥玩意的
						html += "<option value='"+n.id+"'>"+n.name+"</option>"

					})

					$("#create-owner").html(html);

					//默认选中当前登录名
					//从sessionScope中取得当前登录用户的id
					//在js中使用el表达式，el表达式一定要套用在字符串中
					var id = "${user.id}";
					$("#create-owner").val(id);

					//展现模态窗口
					$("#createActivityModal").modal("show");

				}
			})


		})

		//提交创建表单的数据到后台及数据库
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				data:{//前端传给后台的参数

					"owner": $.trim($("#create-owner").val()),
					"name": $.trim($("#create-name").val()),
					"startDate": $.trim($("#create-starDate").val()),
					"endDate": $.trim($("#create-endDate").val()),
					"cost": $.trim($("#create-cost").val()),
					"description": $.trim($("#create-description").val())

				},
				type:"post",
				dataType:"json",
				success:function (data) {
					/*
					   data:{"success":true/false}
					 */
					if(data.success){
						//添加成功后，局部刷新列表信息

						//清空之前填写的表单数据（在浏览器上）
						//$("#activityAddForm")[0].reset();//将jquery对象变成js对象

						//关闭新建市场活动的模态窗口
						$("#createActivityModal").modal("hide");
					}else{
						alert("添加市场活动失败");
					}
				}
			})

		})

		//市场活动页面整体加载完毕，只剩数据出为空白时，触发向后台获取数据的方法。
		pageList(1,2);

		//为查询按钮绑定事件，触发pageList()
		$("#searchBtn").click(function () {

			//将搜索框中的信息保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);

		})

		//为全选的复选框绑定事件，触发全选操作（使小选框选中或取消）
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);

		})

		/* 选中小的复选框，来决定全选框是否选中（有多少个小选框==有多少个已选中的小选框）    小选框代表的是所有的小选框
		动态生成的元素，以on方法的形式来触发事件
		语法：
		    $(需要绑定元素的有效的外层元素）.on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数）

		*/
		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {

			//找到复选框中所有挑√的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择需要删除的记录");

			//用户选了。可能是1条，有可能是多条。
			}else{

				//温馨提示是否删除
				if(confirm("确定删除所选中的记录吗")){
					//拼接参数
					var param = "";

					//将$xz中的每一个dom对象遍历出来，取其value值。=>将要删除的id都拼接成串
					for(var i=0;i<$xz.length;i++){

						param += "id="+$($xz[i]).val();

						//如果不是最后一个元素，需要在后面追加一个&符
						if(i<$xz.length-1){
							param += "&";
						}
					}

					//发请求，携参为上面的param
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,//前端传给后台的参数
						type:"post",
						dataType:"json",
						success:function (data) {
							/*
                            data: {"success":true/false}
                            */
							if(data.success){
								pageList(1,2);
							}else{
								alert("删除市场活动失败");
							}

						}
					})
				}



			}

		})
		
	});

	//pageNo:页码（第几页）  pageSize:每页展示的记录数
	function pageList(pageNo,pageSize) {
		//alert("展示市场活动列表");

		//将全选的√去掉
		$("#qx").prop("checked",false);

		//查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{//前端传给后台的参数
				"pageNo": pageNo,
				"pageSize": pageSize,
				"name": $.trim($("#search-name").val()),
				"owner": $.trim($("#search-owner").val()),
				"startDate": $.trim($("#search-startDate").val()),
				"endDate": $.trim($("#search-endDate").val())

			},
			type:"get",
			dataType:"json",
			success:function (data) {
				/*
				data: 1.List的市场活动信息表
				      2.记录的总条数
				    => {"total":100,"dataList":[{市场活动1},{2},{3}...]}
				 */

				//给数据空白处添数据
				var html = "";
				//每一个n就是每一个市场活动对象
				$.each(data.dataList,function (i,n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';

				})
				$("#activityBody").html(html);

				//计算总页数
				var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

				//分页插件
				$("#activityPage").bs_pagination({
					currentPage: pageNo,//当前页：由前端指定
					rowsPerPage: pageSize,
					maxRowsPerPage: 20,
					totalPages: totalPages,//总页数：由接口返回（AJAX）
					totalRows:data.total,//总记录数

					visiblePageLinks: 3,//显示的最多分页链接数

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数在，点击分页组件时触发
					onChangePage: function (event, data) {
						pageList(data.currentPage,data.rowsPerPage);
					}
				});



			}
		})

	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
							<label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 293px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-starDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 293px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
						<div class="form-group">

							<label for="create-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-cost">
							</div>
						</div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<%--data-dismiss="modal"表示关闭模态窗口--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>



	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>
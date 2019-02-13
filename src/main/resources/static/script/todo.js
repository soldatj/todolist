var todo = {
	//모달창 모드 : 입력 - R, 수정 - M
	todoFormModalMode : "R",
	//테이블 초기화
	initTable : function(){
		var table = $('#todoTable').DataTable({
			processing: true,
			serverSide: true,
			ordering: false,
			searching: false,
			lengthChange: false,
			language: {
				emptyTable: "입력된 할일이 없습니다.",
				processing: "로드 중 입니다.",
				loadingRecords: "잠시만 기다려 주세요.",
				paginate: {
					first: "<<",
					last: ">>",
					next: ">",
					previous: "<"
				}
			},
			ajax: function (data, callback, settings) {
				var page = Math.floor(data.start / data.length);

				todoApi.findAll({page: page},
					function (message, response) {
						if (message != null) {
							alert(message);
							return false;
						}
						callback({
							recordsTotal: response.result.totalElements,
							recordsFiltered: response.result.totalElements,
							data: response.result.content
						});
					});
			},
			infoCallback: function (settings, start, end, max, total, pre) {
				var api = this.api();
				var pageInfo = api.page.info();

				return 'Page ' + (pageInfo.page + 1) + ' of ' + pageInfo.pages;
			},
			columns: [
				{data: "id"},
				{data: "contentAndRefTodoIds"},
				{data: "insDtm"},
				{data: "updDtm"},
				{
					"data": "compYn",
					"defaultContent": "<button>완료</button>"
				}
			],
			columnDefs: [ 
				{
					"targets": 0,
					"width": "50px",
					"className": "text-center",
					"render": function ( data, type, row, meta ) {
						return "<a href='#' onclick='todo.doModifyTodoFormModal("+meta.row+")'>"+data+"</a>";
					}
				},
				{
					"targets": 2,
					"className": "text-center",	
					"width": "150px"
				},
				{
					"targets": 3,
					"className": "text-center",
					"width": "150px"
				},
				{
					"targets": 4,
					"className": "text-center",
					"width": "90px",
					"render": function ( data, type, row, meta ) {
						var btnModCompY = "<button class='btn btn-success' onclick='todo.doComplete("+row.id+")'>미완료</button>";
						var btnModCompN = "<button class='btn btn-primary' onclick='todo.doCancel("+row.id+")'>완료</button>";
						
						return data=="N" ? btnModCompY : btnModCompN;
					}
			} ]
		});
	},
	
	//인풋창 초기화
	initInput : function(){
		//init 참조 입력창 AutoComplete
		$( "#ipt_refTodoIds" ).on( "keydown", function( event ) {
			if ( event.keyCode === $.ui.keyCode.TAB &&
					$( this ).autocomplete( "instance" ).menu.active ) {
				event.preventDefault();
				}
			})
			.autocomplete({
				source: function( request, response ) {
					var id = $("#ipt_id").val();
					var contentStr = todo.extractLast($("#ipt_refTodoIds").val());
					
					//2글자 부터 검색
					if(contentStr.length < 2){
						return;
					}
					
					var param = {
						content : contentStr
					};
					
					//수정모드일때는 autocomplete 대상에서 자신을 제외
					if(id){
						param["id"] = id;
					}
				
					todoApi.findByIdNotAndContentLike(param, function(message, data){
						if(data && data.errorCode == "200" && data.result){
							var TodoMap = data.result;
							response(
								$.map(TodoMap, function(item) {
									var label = "["+item.id+"] "+item.content;
									
									return {
										label: label,
										value: item.id
									}
								})
							);
						}
					});
				
				},
				focus: function() {
					return false;
				},
				minLength: 2,
				select: function( event, ui ) {
					var terms = todo.split( this.value );
					terms.pop();
					terms.push( ui.item.value );
					terms.push( "" );
					
					this.value = terms.join( ", " );
					
					return false;
				}
			});
		
		//Modal 오픈시 autocomplate 영역조절
		$("#todoFormModal").on("shown.bs.modal", function() { 
			$("#ipt_refTodoIds").autocomplete("option", "appendTo", "#todoFormModal") 
		});
	},
	
	//이벤트리스너 지정
	initEventListener : function(){
		$('#todoForm').submit(function(e){
			e.preventDefault();
			if (!confirm("저장 하시겠습니까?")) {
				return false;
			}
			
			if(todo.todoFormModalMode == "R"){
				//신규 저장
				var content = $('#ipt_content').val();
				
				var refTodoIdArray = $("#ipt_refTodoIds").val().split(",");
				var refTodoMapList = [];
				if(refTodoIdArray){
					for(var idx=0; refTodoIdArray.length > idx; idx++){
						var refTodoId = jQuery.trim(refTodoIdArray[idx]);
						
						if(refTodoId!=undefined && refTodoId!=null && refTodoId!=""){
							var reftodoMap = {
								todoId : "",
								refTodoId : refTodoId
							};
							
							refTodoMapList.push(reftodoMap);
						}else{
							continue;
						}
						
						if(!$.isNumeric(refTodoId)){
							alert("등록에 실패하였습니다."
								+ "\nmessage : ["+refTodoId+"] 잘못된 형태의 참조 할일 ID가 존재합니다.")
							return false;
						}
					}
				}
				
				var param = {
					content:content,
					refTodoMapList: refTodoMapList
				};

				todoApi.register(param, function(message, response){
					if (message != null) {
						alert("등록에 실패하였습니다."
							+ "\nmessage : " + message);
						return false;
					}
					todo.doFindAll();
					todo.resetTodoFormModalInputs();
					todo.closeTodoFormModal();
				});
			}else{
				//업데이트
				var id = $('#ipt_id').val();
				var content = $('#ipt_content').val();
				var compYn = $('#ipt_compYn').val();
				
				var refTodoIdArray = $("#ipt_refTodoIds").val().split(",");
				var refTodoMapList = [];
				if(refTodoIdArray){
					for(var idx=0; refTodoIdArray.length > idx; idx++){
						var refTodoId = jQuery.trim(refTodoIdArray[idx]);
						
						if(refTodoId!=undefined && refTodoId!=null && refTodoId!=""){
							var reftodoMap = {
								todoId : id,
								refTodoId : refTodoId
							};
							
							refTodoMapList.push(reftodoMap);
						}else{
							continue;
						}
						
						//자기 자신이 포함되있으면 예외
						if(id == refTodoId){
							alert("등록에 실패하였습니다."
								+ "\nmessage : ["+refTodoId+"] 참조에는 해당 할일이 포함될 수 없습니다.")
							return false;
						}
						
						if(!$.isNumeric(refTodoId)){
							alert("등록에 실패하였습니다."
								+ "\nmessage : ["+refTodoId+"] 잘못된 형태의 참조 할일 ID가 존재합니다.")
							return false;
						}
					}
				}
				
				var param = {
					id:id,
					content:content,
					refTodoMapList: refTodoMapList
				};

				todoApi.modifyModal(param, function(message, response){
					if (message != null) {
						alert("등록에 실패하였습니다."
							+ "\nmessage : " + message);
						return false;
					}
					
					todo.doFindAll();
					todo.resetTodoFormModalInputs();
					todo.closeTodoFormModal();
				});
			}
		});
	},
	
	//입력모달출력
	doRegisterTodoFormModal : function(){
		todo.todoFormModalMode = "R";
		$('#lb_modal_reg').show();
		$('#lb_modal_mod').hide();
		
		$('#h4_modal_reg').show();
		$('#h4_modal_mod').hide();
		
		todo.showTodoFormModal();
	},
	
	//수정모달출력
	doModifyTodoFormModal : function(rownum){
		todo.todoFormModalMode = "M";
		$('#lb_modal_mod').show();
		$('#lb_modal_reg').hide();
		
		$('#h4_modal_mod').show();
		$('#h4_modal_reg').hide();
		
		todo.showTodoFormModal();
		
		
		//rownum에 맞는 데이터를 table에서 가져와 모달창에 입력
		var table = $('#todoTable').DataTable();
		var row = table.rows(rownum).data()[0];
		
		$('#ipt_rownum').val(rownum);
		$("#ipt_id").val(row.id);
		$('#ipt_content').val(row.content);
		$("#ipt_refTodoIds").val(row.refTodoIds);
	},
	
	//모달창 입력사항 리셋
	resetTodoFormModalInputs : function(){
		$("#ipt_id").val("");
		$("#ipt_rownum").val("");
		$("#ipt_content").val("");
		$("#ipt_refTodoIds").val("");
	},
	
	//모달창 출력
	showTodoFormModal : function(){
		todo.resetTodoFormModalInputs();
		$('#todoFormModal').modal('show');
	},
	
	//모달창 닫기
	closeTodoFormModal : function(){
		todo.resetTodoFormModalInputs();
		$('#todoFormModal').modal('hide');
	},
	
	//rest api - 완료 실행
	doComplete : function(todoId){
		todoApi.complete(todoId,
			function (message, response) {
				if (message != null) {
					alert(message);
					return false;
				}
				todo.doFindAll();
			});
	},
	
	//rest api - 완료 취소 실행
	doCancel : function(todoId){
		todoApi.cancel(todoId,
			function (message, response) {
				if (message != null) {
					alert(message);
					return false;
				}
				todo.doFindAll();
			});
	},
	
	
	doFindAll : function(){
		$('#todoTable').DataTable().ajax.reload(null, false);
	},
	
	split : function( val ){
		return val.split( /,\s*/ );
	},
	
	extractLast : function( term ){
		return todo.split( term ).pop();
	},
};

//초기화
$(document).ready(function(){
	$.ajaxSetup({ cache: false });
	 
	todo.initTable();
	todo.initInput();
	todo.initEventListener();
});
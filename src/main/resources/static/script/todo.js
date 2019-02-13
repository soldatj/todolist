var todo = {
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
				    "render": function ( data, type, full, meta ) {
						return '<a href="#">'+data+'</a>';
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
					"width": "70px",
					"render": function ( data, type, row, meta ) {
						var btnModCompY = "<button class='btn btn-success' onclick='todo.doComplete("+row.id+")'>미완료</button>";
						var btnModCompN = "<button class='btn btn-primary' onclick='todo.doCancel("+row.id+")'>완료</button>";
						
						return data=="N" ? btnModCompY : btnModCompN;
					}
			} ]
		});
	},
	    
	initInput : function(){
		//init AutoComplete
		$( "#ipt_acTodoList" ).on( "keydown", function( event ) {
	        if ( event.keyCode === $.ui.keyCode.TAB &&
	                $( this ).autocomplete( "instance" ).menu.active ) {
	              event.preventDefault();
	            }
	          })
	        .autocomplete({
			source: function( request, response ) {
				var contentStr = todo.extractLast($("#ipt_acTodoList").val());
				
				//2글자 부터 검색
				if(contentStr.length < 2){
					return;
				}
				
				var param = {
					content : contentStr
				};
				
				todoApi.findByIdNotAndContentLike(param, function(message, data){
					if(data && data.errorCode == "200" && data.result){
						var TodoMap = data.result;
						response(
							$.map(TodoMap, function(item) {
								return {
									label: item.content,
									value: item.id
								}
							})
						);
					}
				});
				
			},
	        focus: function() {
	            // prevent value inserted on focus
	            return false;
	          },
			minLength: 2,
			select: function( event, ui ) {
				console.log( "Selected: " + ui.item.id + " aka " + ui.item.content );
				
				var terms = todo.split( this.value );
	          // remove the current input
	          terms.pop();
	          // add the selected item
	          terms.push( ui.item.value );
	          // add placeholder to get the comma-and-space at the end
	          terms.push( "" );
	          this.value = terms.join( ", " );
	          
	          return false;
			}
		});
		
		//Modal 오픈시 autocomplate 영역조절
		$("#todoFormModal").on("shown.bs.modal", function() { 
			$("#ipt_acTodoList").autocomplete("option", "appendTo", "#todoFormModal") 
		});
	},
	todoFormModalMode : "N",
	initEventListener : function(){
		$('#todoForm').submit(function(e){
			e.preventDefault();
			if (!confirm("저장 하시겠습니까?")) {
				return false;
			}
			
			if(todo.todoFormModalMode == "N"){
				//신규 저장
				var content = $('#ipt_content').val();
				
				var refTodoIdArray = $("#ipt_acTodoList").val().split(",");
				var refTodoMapArray = [];
				if(refTodoIdArray){
					for(var idx=0; refTodoIdArray.length > idx; idx++){
						var refTodoId = jQuery.trim(refTodoIdArray[idx]);
						
						if(refTodoId!=undefined && refTodoId!=null && refTodoId!=""){
							var reftodoMap = {
								todoId : "",
								refTodoId : refTodoId
							};
							
							refTodoMapArray.push(reftodoMap);
						}else{
							continue;
						}
						
						if(!$.isNumeric(refTodoId)){
							alert("["+refTodoId+"] 잘못된 참조 할일 ID가 존재합니다.")
							return false;
						}
					}
				}
				
				var param = {
					todo: {
						content:content
					},
					refTodoList: refTodoMapArray
				};

				todoApi.register(param, function(message, response){
					if (message != null) {
						alert("등록에 실패하였습니다."
							+ "\nCODE : " + response.errorCode
							+ "\nmessage : " + message);
						return false;
					}
					todo.doFindAll();
					
					todo.resetTodoFormModalInputs();
				});
			}else{
				//업데이트
				
			}
		});

//		$('#todoTable tbody').on( 'click', 'button', function () {
//			var data = table.row( $(this).parents('tr') ).data();
//			alert( data[0] +"'s salary is: "+ data[ 2 ] );
//		} );
	},
	
	resetTodoFormModalInputs : function(){
		$("#hdn_id").val("");
		$("#ipt_content").val("");
		$("#ipt_acTodoList").val("");
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
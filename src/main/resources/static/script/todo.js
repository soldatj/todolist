$(document).ready(function(){
	 $.ajaxSetup({ cache: false });
	 
	//init Table
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
			{data: "content"},
			{data: "insDtm"},
			{data: "updDtm"},
			{
				"data": "compYn",
				"defaultContent": "<button>완료</button>"
			}
		],
		"columnDefs": [ 
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
			{"targets": 4,
			"className": "text-center",
			"width": "70px",
			"render": function ( data, type, row, meta ) {
					var btnModCompY = "<button class='btn btn-success'>완료</button>";
					var btnModCompN = "<button class='btn btn-success'>취소</button>";
					
					return data=="N"?btnModCompY:btnModCompN;
				}
			} ]
	});
	
//	$('#todoTable tbody').on( 'click', 'button', function () {
//		var data = table.row( $(this).parents('tr') ).data();
//		alert( data[0] +"'s salary is: "+ data[ 2 ] );
//	} );

	$('#todoForm').submit(function(e){
		e.preventDefault();
		if (!confirm("저장 하시겠습니까?")) {
			return false;
		}
		
		var content = $('#ipt_content').val();

		var param = {
			todo: {
				content:content
			},
			refTodoList: []
		};

		todoApi.register(param, function(message, response){
			if (message != null) {
				alert("등록에 실패하였습니다."
					+ "\nCODE : " + response.code
					+ "\nmessage : " + message);
				return false;
			}
			$('#todoTable').DataTable().ajax.reload(null, false);
		});
	});
	
	//init AutoCo
	$( "#ipt_acTodoList" ).autocomplete({
		source: function( request, response ) {
			var contentStr = $("#ipt_acTodoList").val();
			
			$.ajax({
				url: todoApi.BASE_PREFIX + 'findByIdNotAndContentLike/',
				method: 'get',
				data: {
					content : contentStr
					},
				contentType: "application/json",
				dataType: 'json'
			}).then(function(data){
				if(data && data.errorCode == "200" && data.result){
					var tododata = data.result;
					response(
						$.map(tododata, function(item) {
							return {
								label: item.content,
								value: item.id
							}
						})
					);
				}
			}).catch(function(data){
				console.log(data);
			});
			
			},
			minLength: 2,
			select: function( event, ui ) {
			console.log( "Selected: " + ui.item.id + " aka " + ui.item.content );
		}
	});
	
	//Modal 오픈시 autocomplate 영역조절
	$("#todoFormModal").on("shown.bs.modal", function() { 
		$("#ipt_acTodoList").autocomplete("option", "appendTo", "#todoFormModal") 
	});
});
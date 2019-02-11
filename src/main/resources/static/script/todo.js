'use strict';
$(document).ready(() => {
	$('#myTabs a').click(function (e) {
		  e.preventDefault()
		  $(this).tab('show')
		})


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
				first: "처음",
				last: "마지막 ",
				next: "다음",
				previous: "이전"
			}
		},
		ajax: function (data, callback, settings) {
			let page = Math.floor(data.start / data.length);

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
			let api = this.api();
			let pageInfo = api.page.info();

			return 'Page ' + (pageInfo.page + 1) + ' of ' + pageInfo.pages;
		},
		columns: [
			{data: "id"},
			{data: "content"},
			{data: "insDtm"},
			{data: "updDtm"},
		    {
		      "data": "null", // can be null or undefined
		      "defaultContent": "<button>완료</button>"
		    }
		],
		"columnDefs": [ {
		    "targets": 0,
		    "width": "10%"},
		    {
		    "targets": 2,
		    "width": "15%"},
		    {
		    "targets": 3,
		    "width": "15%"},
		    {"targets": 4,
		    "data": "compYn",
		    "width": "10%",
		    "render": function ( data, type, row, meta ) {
		      return data=="N"?"<button>완료</button>":"<button>취소</button>";
		    }
		  } ]
	});
	
	$('#todoTable tbody').on( 'click', 'button', function () {
        var data = table.row( $(this).parents('tr') ).data();
        alert( data[0] +"'s salary is: "+ data[ 2 ] );
    } );

	$('#todoForm').submit((e) => {
		e.preventDefault();
		if (!confirm("저장 하시겠습니까?")) {
			return false;
		}
		let content = $('#ipt_content').val();


		let param = {
			todo: {
				content:content
			},
			refTodoList: []
		};

		todoApi.create(param, (message, response) => {
			if (message != null) {
				alert("등록에 실패하였습니다."
					+ "\nCODE : " + response.code
					+ "\nmessage : " + message);
				return false;
			}
			$('#todoTable').DataTable().ajax.reload(null, false);
		});
	});
	
	$( "#inputAutocomplete" ).autocomplete({
	      source: function( request, response ) {
	    	  
	        var contentStr = $("#inputAutocomplete").val();
	        
	        $.ajax({
	             url: '/api/todo/findByIdNotAndContentLike/',
	             method: 'get',
	             data: {
	 	            content : contentStr
		          },
	             contentType: "application/json",
	             dataType: 'json'
	         }).then((data) => {
	        	 if(data && data.errorCode == "200" && data.result){
	        		 console.log(data);
	        		 var tododata = data.result;
	        		 
	        		 response(
	                         $.map(tododata, function(item) {
	                        	 console.log("item")
	                        	 console.log(item)
	                        	 
	                             return {
	                                 label: item.content,
	                                 value: item.id
	                             }
	                         })
	                     );
	        	 }
	         }).catch((data) => {
	        	 console.log(data);
	         });
	        
	      },
	      minLength: 2,
	      select: function( event, ui ) {
	        console.log( "Selected: " + ui.item.id + " aka " + ui.item.content );
	      }
	    } );
		
//	const _BASE_PREFIX = '/api/todo/findByIdNotAndContentLike';
//
//    function _findAll(param, callback) {
//        $.ajax({
//            url: _BASE_PREFIX,
//            method: 'get',
//            data: param,
//            contentType: "application/json",
//            dataType: 'json'
//        }).then((response) => {
//            callback(null, response);
//        }).catch((response) => {
//            let responseJSON = response.result;
//            let message = responseJSON.errorMessage;
//            callback(message, responseJSON);
//        });
//    }
		
	// 자동 완성 ON (위 코드 그대로) 
		//$("#inputAutocomplete").autocomplete({ source: "/YOUR/SEARCH/URL", minLength: 2, }); 
	// modal이 열릴 때 다시 영역 한정 (appendTo 옵션) 
		$("#myModal").on("shown.bs.modal", function() { 
			$("#inputAutocomplete").autocomplete("option", "appendTo", "#myModal") 
		});


});
'use strict';
$(document).ready(() => {

	$('#todoTable').DataTable({
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
			{data: "compYn"}
		]
	});

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

});
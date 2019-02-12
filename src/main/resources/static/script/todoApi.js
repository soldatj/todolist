var todoApi = {
	BASE_PREFIX : '/api/todo/',
	
	findAll : function(param, callback) {
		$.ajax({
			url: todoApi.BASE_PREFIX,
			method: 'get',
			data: param,
			contentType: "application/json",
			dataType: 'json'
		}).then(function(response){
			callback(null, response);
		}).catch(function(response){
			var responseJSON = response.result;
			var message = responseJSON.errorMessage;
			callback(message, responseJSON);
		});
	},

	register : function(param, callback) {
		$.ajax({
			url: todoApi.BASE_PREFIX,
			method: 'post',
			data: JSON.stringify(param),
			contentType: "application/json",
			dataType: 'json'
		}).then(function(response){
			callback(null, response);
		}).catch(function(response){
			var responseJSON = response.result;
			var message = responseJSON.errorMessage;
			callback(message, responseJSON);
		});
	},
	
	modify : function(param, callback) {
		$.ajax({
			url: todoApi.BASE_PREFIX,
			method: 'put',
			data: JSON.stringify(param),
			contentType: "application/json",
			dataType: 'json'
		}).then(function(response){
			callback(null, response);
		}).catch(function(response){
			var responseJSON = response.result;
			var message = responseJSON.errorMessage;
			callback(message, responseJSON);
		});
	}
}


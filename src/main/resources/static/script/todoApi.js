'use strict';

let todoApi = (() => {

     const _BASE_PREFIX = '/api/todo/';

     function _findAll(param, callback) {
         $.ajax({
             url: _BASE_PREFIX,
             method: 'get',
             data: param,
             contentType: "application/json",
             dataType: 'json'
         }).then((response) => {
             callback(null, response);
         }).catch((response) => {
             let responseJSON = response.result;
             let message = responseJSON.errorMessage;
             callback(message, responseJSON);
         });
     }

     function _register(param, callback) {
         $.ajax({
             url: _BASE_PREFIX,
             method: 'post',
             data: JSON.stringify(param),
             contentType: "application/json",
             dataType: 'json'
         }).then((response) => {
             callback(null, response);
         }).catch((response) => {
             let responseJSON = response.result;
             let message = responseJSON.errorMessage;
             callback(message, responseJSON);
         });
     }

     return {
         findAll: _findAll,
         create: _register
     }
})();


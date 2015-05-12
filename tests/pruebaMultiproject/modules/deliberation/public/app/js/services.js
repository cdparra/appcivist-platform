'use strict';

/* Services */

var deliberationServices = angular.module('deliberationServices', ['ngResource']);

deliberationServices.factory('CommentsList', ['$resource',
    function($resource){
        return $resource('api/comments', {}, {
            query: {method: 'GET'}
        });
    }]);

deliberationServices.factory('Comment', ['$resource',
    function($resource){
        return $resource('api/comment/:id', {}, {
            query: {method: 'GET', params:{id:'@id'}}
        });
    }]);

deliberationServices.factory('CommentsByUser', ['$resource',
    function($resource){
        return $resource('api/comments/:userId', {}, {
            query: {method: 'GET', params:{userId: '@userId'}}
        });
    }]);

deliberationServices.factory('PostComment', ['$resource',
    function($resource){
        return $resource('api/comment/:id/:comment', {}, {
            query: {method: 'POST', params:{id: '@id', comment: '@comment'}}
        });
    }]);





deliberationServices.factory('Public', ['$resource',
    function($resource){
        return $resource('public/app/partials/deliberation/deliberation.html', {}, {
            query: {method: 'GET'}
        });
    }]);
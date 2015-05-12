'use strict';

/* App Module */

var deliberationApp = angular.module('deliberationApp', [
    'ngRoute',
    'textAngular',
    'deliberationControllers',
    'deliberationServices'
], function($locationProvider){
    $locationProvider.html5Mode({
        enabled: true,
        requireBase : false
    });
});

deliberationApp.config(['$routeProvider', '$provide',
    function($routeProvider, $provide) {
        $routeProvider.
            when('/deliberation', {
                templateUrl: '../../public/app/partials/deliberation/deliberation.html',
                controller: 'CommentsListCtrl'
            }).
            when('/deliberation/comment=:_id', {
                templateUrl: '../../public/app/partials/deliberation/comment.html',
                controller: 'CommentCtrl'
            }).
            when('/deliberation/user=:_id', {
                templateUrl: '../../public/app/partials/deliberation/userComments.html',
                controller: 'CommentsListByUserCtrl'
            });

        $provide.decorator('taOptions', ['taRegisterTool', '$delegate', function(taRegisterTool, taOptions){ // $delegate is the taOptions we are decorating
            taOptions.toolbar = [
                ['h1', 'h2', 'h3', 'p', 'quote'],
                ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol',],
                ['html', 'insertImage','insertLink', 'insertVideo']
            ];
            return taOptions;
        }]);
    }
]);

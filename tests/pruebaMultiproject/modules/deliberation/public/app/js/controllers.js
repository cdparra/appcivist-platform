'use strict';

/* Controllers */

var deliberationControllers = angular.module('deliberationControllers', []);

deliberationControllers.controller('Ctrl', function($scope,$location){
    $scope.getUrl = function() {
        console.log("Location= " + $location.path());
        return $location.path();
    };
});

deliberationControllers.controller('CommentsListCtrl', function($scope, CommentsList, PostComment) {
    $scope.comments = CommentsList.query();

    $scope.update = function(comment){
        PostComment.query({id: 1, comment: comment});
    }
});

deliberationControllers.controller('CommentCtrl', ['$scope', '$routeParams', 'Comment', function($scope, $routeParams, Comment){
    $scope.comment = Comment.query({id:$routeParams._id});
}]);

deliberationControllers.controller('CommentsListByUserCtrl', ['$scope','$routeParams', 'CommentsByUser', function($scope, $routeParams, CommentsByUser) {
    $scope.comments = CommentsByUser.query({userId:$routeParams._id});
}]);

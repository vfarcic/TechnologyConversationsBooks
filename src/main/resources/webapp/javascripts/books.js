angular.module('booksModule', [])
    .controller('booksCtrl', function ($scope, $http) {
        $scope.listBooks = function() {
            $http.get('/api/v1/items').then(function(response) {
                $scope.books = response.data;
            });
        };
        $scope.newBook = function() {
            $scope.book = {};
            $scope.originalBook = angular.copy($scope.book);
        };
        $scope.saveBook = function() {
            $http.put('/api/v1/items', $scope.book).then(function() {
                $scope.listBooks();
                $scope.newBook();
            });
        };
        $scope.openBook = function(bookId) {
            $http.get('/api/v1/items/' + bookId).then(function(response) {
                $scope.book = response.data;
                $scope.originalBook = angular.copy($scope.book);
            });
        };
        $scope.revertBook = function() {
            $scope.book = angular.copy($scope.originalBook);
        };
        $scope.deleteBook = function() {
            $http.delete('/api/v1/items/' + $scope.book.id).then(function() {
                $scope.listBooks();
                $scope.newBook();
            });
        };
        $scope.cssClass = function(ngModelController) {
            return {
                'has-error': ngModelController.$invalid,
                'has-success': ngModelController.$valid
            };
        };
        $scope.cssClassButton = function(ngModelController) {
            return {
                'btn-success': ngModelController.$valid,
                'btn-danger': ngModelController.$invalid
            };
        };
        $scope.isValid = function(ngModelController) {
            return ngModelController.$valid;
        };
        $scope.canRevertBook = function() {
            return !angular.equals($scope.book, $scope.originalBook);
        };
        $scope.canDeleteBook = function() {
            return (typeof $scope.book !== 'undefined' && typeof $scope.book.id !== 'undefined');
        };
        $scope.pricePattern = function() {
            return (/^[\d]+\.\d\d$/);
        };
        $scope.listBooks();
        $scope.newBook();
    });
'use strict';
(function () {
    var module = angular.module('Explorer', []);
    module.filter('range', function () {
        return function (center, limit) {
            var min = center - 3;
            if (min < 1) {
                min = 1;
            }
            var max = min + 6;
            if (max > limit) {
                max = limit;
            }
            var range = [];
            while (min <= max) {
                range.push(min ++);
            }
            return range;
        };
    });
    module.controller('SearchPageController', ['$scope', '$http', function ($scope, $http) {
        $scope.view = {
            query: '',
            results: [],
            loading: false
        };
        $scope.search = function () {
            if (!$scope.view.query) {
                return;
            }
            $scope.view.loading = true;
            $scope.view.results = [];
            $http.post('/api/rest/v1/search', $scope.view.query).then(function (response) {
                $scope.view.results = response.data;
                $scope.view.loading = false;
            });
        };
    }]);
})();
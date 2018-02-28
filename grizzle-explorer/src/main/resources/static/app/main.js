'use strict';
(function () {
    var module = angular.module('Explorer', ['ngRoute']);
    module.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'app/pages/home/template.ng',
                controller: 'HomePageController'
            })
            .when('/tables/:table/:page?', {
                templateUrl: 'app/pages/grid/template.ng',
                controller: 'GridController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }]);
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
    })
})();
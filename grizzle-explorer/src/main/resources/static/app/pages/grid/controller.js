(function () {
    angular.module('Explorer').controller('GridController', ['$scope', '$http', '$routeParams', '$location', function ($scope, $http, $routeParams, $location) {
        $scope.table = {
            name: $routeParams.table,
            page: parseInt($routeParams.page) || 1,
            headers: [],
            content: [],
            pageSize: 0,
            total: 0,
            totalPages: 0,
            hasNext: false,
            hasPrevious: false,
            query: ''
        };
        $scope.view = {
            jumpPage: 0,
            rowIndex: 0,
            cellIndex: 0,
            search: ''
        };
        var search = [];
        var $search = $location.search();
        for (var key in $search) {
            if ($search.hasOwnProperty(key)) {
                search.push(key + '=' + encodeURIComponent($search[key]));
            }
        }
        $scope.view.search = search.length ? '?' + search.join('&') : '';
        $http.get('/api/rest/v1/database/tables/' + $routeParams.table + '/headers').then(function (response) {
            $scope.table.headers = response.data;
        });
        var getData = function (page) {
            var id = $location.search()['id'];
            if (id) {
                $http.get('/api/rest/v1/database/tables/' + $routeParams.table + '/' + id).then(function (response) {
                    $scope.table.content = response.data.values;
                    $scope.table.total = response.data.total;
                    $scope.table.totalPages = response.data.totalPages;
                    $scope.table.pageSize = response.data.pageSize;
                    $scope.table.hasNext = response.data.hasNextPage;
                    $scope.table.hasPrevious = response.data.hasPreviousPage;
                    $scope.table.query = response.data.query;
                });
            } else {
                $http.get('/api/rest/v1/database/tables/' + $routeParams.table + '/pages/' + page).then(function (response) {
                    $scope.table.content = response.data.values;
                    $scope.table.total = response.data.total;
                    $scope.table.totalPages = response.data.totalPages;
                    $scope.table.pageSize = response.data.pageSize;
                    $scope.table.hasNext = response.data.hasNextPage;
                    $scope.table.hasPrevious = response.data.hasPreviousPage;
                    $scope.table.query = response.data.query;
                });
            }
        };
        $scope.$watch('table.page', getData);
    }]);
})();
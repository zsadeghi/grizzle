(function () {
    angular.module('Explorer').controller('HomePageController', ['$scope', '$http', function ($scope, $http) {
        $scope.db = {
            tables: [],
            table: ''
        };
        $http.get('/api/rest/v1/database/tables').then(function (response) {
            $scope.db.tables = response.data;
            $scope.db.table = response.data[0];
        });
    }]);
})();
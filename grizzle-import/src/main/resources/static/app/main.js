'use strict';
var module = angular.module('Importer', []);
module.controller('StatusController', ['$scope', '$http', '$interval', '$timeout', function ($scope, $http, $interval, $timeout) {
    var getStatus = function () {
        $('#loading-dialog').modal('show');
        $scope.error = '';
        $http.get('/api/rest/v1/converter/status')
            .then(function (result) {
                $scope.importer.status = result.data;
                $http.get('/api/rest/v1/converter/status/total').then(function (result) {
                    $scope.importer.total = result.data;
                    $http.get('/api/rest/v1/converter/status/converted').then(function (result) {
                        $scope.importer.converted = result.data;
                        $http.get('/api/rest/v1/converter/last').then(function (result) {
                            $scope.importer.last = result.data;
                            $('#loading-dialog').modal('hide');
                        });
                    });
                });
            }, function (reason) {
                $scope.error = reason.data;
                $('#loading-dialog').modal('hide');
            });
    };
    var startImport = function (rerun) {
        $('#loading-dialog').modal('show');
        $scope.error = '';
        $http.post('/api/rest/v1/converter/start', {
            rerun: rerun
        }).then(function (result) {
            $scope.importer.status = result.data;
            $('#loading-dialog').modal('hide');
            getStatus();
        }, function (reason) {
            $scope.error = reason.data;
            $('#loading-dialog').modal('hide');
        });
    };

    var stopImport = function () {
        $('#loading-dialog').modal('show');
        $scope.error = '';
        $http.post('/api/rest/v1/converter/stop').then(function (result) {
            $scope.importer.status = result.data;
            getStatus();
            $timeout(function () {
                $('#loading-dialog').modal('hide');
            }, 5000);
        }, function (reason) {
            $scope.error = reason.data;
            $('#loading-dialog').modal('hide');
        });
    };
    $scope.showDiagnostics = false;
    $scope.importer = {
        status: "UNKNOWN",
        converted: 0,
        total: 0,
        last: null,
        start: startImport,
        stop: stopImport,
        showDiagnostics: false
    };
    var watcher = null;
    $scope.$watch('importer.status', function (status) {
        if (status !== 'DONE' && status !== 'IDLE' && status !== 'ERROR') {
            watcher = $interval(function () {
                $http.get('/api/rest/v1/converter/status')
                    .then(function (result) {
                        $scope.importer.status = result.data;
                        $http.get('/api/rest/v1/converter/status/converted')
                            .then(function (result) {
                                $scope.importer.converted = result.data;
                                if ($scope.importer.showDiagnostics) {
                                    return $http.get('/api/rest/v1/converter/last').then(function (result) {
                                        $scope.importer.last = result.data;
                                    });
                                } else {
                                    $scope.importer.last = null;
                                }
                            }, function (reason) {
                                $scope.error = reason.data;
                                $interval.cancel(watcher);
                            });
                    }, function (reason) {
                        $scope.error = reason.data;
                        $interval.cancel(watcher);
                    });
            }, 100);
        } else if (watcher) {
            $interval.cancel(watcher);
        }
    });
    getStatus();
}]);
module.filter('percent', function () {
    return function (value) {
        return '%' + (Math.floor(value * 10000) / 100);
    };
});
module.filter('status', function () {
    return function (value) {
        return !value ? '' : value.replace(/_/g, ' ');
    };
});
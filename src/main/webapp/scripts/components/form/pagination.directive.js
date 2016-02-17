/* globals $ */
'use strict';

angular.module('mongohipsterApp')
    .directive('mongohipsterAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });

'use strict';

angular.module('mongohipsterApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



'use strict';

mongojhipApp.factory('Publication', function ($resource) {
        return $resource('app/rest/publications/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });

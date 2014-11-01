'use strict';

mongojhipApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/publication', {
                    templateUrl: 'views/publications.html',
                    controller: 'PublicationController',
                    resolve:{
                        resolvedPublication: ['Publication', function (Publication) {
                            return Publication.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });

'use strict';

neo4jhipsterApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/author', {
                    templateUrl: 'views/authors.html',
                    controller: 'AuthorController',
                    resolve:{
                        resolvedAuthor: ['Author', function (Author) {
                            return Author.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });

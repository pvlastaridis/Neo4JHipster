'use strict';

mongojhipApp.controller('PublicationController', function ($scope, resolvedPublication, Publication) {

        $scope.publications = resolvedPublication;

        $scope.create = function () {
            Publication.save($scope.publication,
                function () {
                    $scope.publications = Publication.query();
                    $('#savePublicationModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.publication = Publication.get({id: id});
            $('#savePublicationModal').modal('show');
        };

        $scope.delete = function (id) {
            Publication.delete({id: id},
                function () {
                    $scope.publications = Publication.query();
                });
        };

        $scope.clear = function () {
            $scope.publication = {title: null, id: null};
        };
    });

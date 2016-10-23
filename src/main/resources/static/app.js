$(function () {

    var dependencyList = $('#dependency-results-list-group');


    $('#check-pom-btn').on('click', checkDependencies);
    $('#pom-url-txt').keyup( function (e) {
        if(e.keyCode ==13){
            checkDependencies(e);
        }
    });

    function checkDependencies () {
        var pomUrl = $('#pom-url-txt').val();
        $.get('http://localhost:8080/upToDate/?mavenUrl=' + pomUrl, function (response) {
            var badgeUrl = window.location.href + "badge/upToDate.svg?mavenUrl=" + pomUrl;
            $('#badge-image').attr('src', badgeUrl);
            $('#badge-link').text(badgeUrl);
            dependencyList.empty();
            $.each(response.dependencyResults, function (i, dependency) {
                dependencyList.append("<li class='list-group-item'> " + getDifferenceLabel(dependency.dependencyDifference) + dependency.group + dependency.artifact + ": " + dependency.projectVersion + "</li>")
            });
        })
    }

    function getDifferenceLabel(difference) {

        var color = "label-default";
        if (difference === "UP_TO_DATE") {
            color = "label-success";
        } else if (difference === "ERROR" || difference === "BEHIND") {
            color = "label-danger";
        } else if (difference === "MINOR_VERSION_BEHIND") {
            color = "label-warning";
        }

        return "<span class='pull-right label " + color + "'>" + difference + "</span>"
    }

});
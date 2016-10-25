$(function () {

    var dependencyList = $('#dependency-results-list-group');

    $('#pom-url-txt').val(getURLParameter("mavenUrl"));
    if($('#pom-url-txt').val() != ""){
        checkDependencies();
    }

    $('#check-pom-btn').on('click', checkDependencies);
    $('#pom-url-txt').keyup(function (e) {
        if (e.keyCode == 13) {
            checkDependencies(e);
        }
    });

    function checkDependencies() {
        var pomUrl = $('#pom-url-txt').val();
        $.get(window.location.origin +'/upToDate/?mavenUrl=' + pomUrl, function (response) {
            var badgeUrl = window.location.origin + "/badge/upToDate.svg?mavenUrl=" + pomUrl;
            $('#badge-image').attr('src', badgeUrl);
            $('#badge-link').text(badgeUrl);
            dependencyList.empty();
            $.each(response.dependencyResults, function (i, dependency) {
                dependencyList.append("<li class='list-group-item'> " + getDifferenceLabel(dependency.dependencyDifference, dependency.latestVersion) + dependency.group + dependency.artifact + ": " + dependency.projectVersion + "</li>")
            });
        })
    }

    function getDifferenceLabel(difference, version) {

        var color = "label-default";
        if (difference === "UP_TO_DATE") {
            color = "label-success";
        } else if (difference === "ERROR" || difference === "BEHIND") {
            color = "label-danger";
        } else if (difference === "MINOR_VERSION_BEHIND") {
            color = "label-warning";
        }

        return "<span class='pull-right label " + color + "' title='" + version + "'>" + difference + "</span>"
    }

    function getURLParameter(sParam) {
        var sPageURL = window.location.search.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
            var sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] == sParam) {
                return sParameterName[1];
            }
        }
    }

});
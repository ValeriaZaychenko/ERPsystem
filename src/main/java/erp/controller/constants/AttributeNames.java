package erp.controller.constants;


public interface AttributeNames {

    interface UserViewUsers {
        String users = "users";
    }

    interface ProgressView {
        String progress = "progress";
        String holiday = "holiday";
        String weekends = "weekends";
        String allDays = "allDays";
        String monthDate = "monthDate";
        String workingDays = "workingDays";
        String holidaysYear = "holidaysYear";
    }

    interface UserViewPossibleUserRoles {
        String possibleUserRoles = "possibleUserRoles";
    }

    interface UserViewReports {
        String userReports = "userReports";
        String userProgress = "userProgress";
        String sumOfDurations = "sumOfDurations";
    }

    interface ErrorView {
        String message = "errorMessage";
        String attribute = "errorAttribute";
    }

    interface ErrorPlainView {
        String errorMessage = "errorMessage";
    }
}

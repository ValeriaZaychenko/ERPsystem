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
        String monthName = "monthName";
    }

    interface UserViewPossibleUserRoles {
        String possibleUserRoles = "possibleUserRoles";
    }

    interface UserViewReports {
        String userReports = "userReports";
    }

    interface ErrorView {
        String message = "errorMessage";
        String attribute = "errorAttribute";
    }

    interface ErrorPlainView {
        String errorMessage = "errorMessage";
    }
}

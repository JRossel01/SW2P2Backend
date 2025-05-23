package com.med.backend.persistence.util;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMINISTRATOR(Arrays.asList(
            RolePermission.READ_MY_PROFILE,
            RolePermission.FIND_ALL_USERS,

            RolePermission.FIND_ALL_DOCTORS,
            RolePermission.CREATE_ONE_DOCTOR,
            RolePermission.UPDATE_ONE_DOCTOR,
            RolePermission.DELETE_ONE_DOCTOR,

            RolePermission.CREATE_ONE_RECEPTIONIST,
            RolePermission.UPDATE_ONE_RECEPTIONIST,
            RolePermission.DELETE_ONE_RECEPTIONIST,
            RolePermission.FIND_ALL_RECEPTIONISTS,

            RolePermission.CREATE_ONE_PATIENT,
            RolePermission.UPDATE_ONE_PATIENT,
            RolePermission.DELETE_ONE_PATIENT,
            RolePermission.FIND_ALL_PATIENTS,

            RolePermission.CREATE_ONE_SCHEDULE,
            RolePermission.UPDATE_ONE_SCHEDULE,
            RolePermission.DELETE_ONE_SCHEDULE,
            RolePermission.FIND_ALL_SCHEDULES,
            RolePermission.FIND_SCHEDULES_BY_DOCTOR,

            RolePermission.CREATE_ONE_APPOINTMENT,
            RolePermission.UPDATE_ONE_APPOINTMENT,
            RolePermission.DELETE_ONE_APPOINTMENT,
            RolePermission.FIND_ALL_APPOINTMENTS,
            RolePermission.FIND_APPOINTMENTS_BY_PATIENT,
            RolePermission.FIND_APPOINTMENTS_BY_DOCTOR,

            RolePermission.CREATE_ONE_MEDICAL_RECORD,
            RolePermission.UPDATE_ONE_MEDICAL_RECORD,
            RolePermission.DELETE_ONE_MEDICAL_RECORD,
            RolePermission.FIND_ALL_MEDICAL_RECORDS,
            RolePermission.FIND_MEDICAL_RECORDS_BY_PATIENT,
            RolePermission.FIND_MEDICAL_RECORD_BY_ID,

            RolePermission.CREATE_ONE_MEDICAL_NOTE,
            RolePermission.UPDATE_ONE_MEDICAL_NOTE,
            RolePermission.DELETE_ONE_MEDICAL_NOTE,
            RolePermission.FIND_ALL_MEDICAL_NOTES,
            RolePermission.FIND_MEDICAL_NOTES_BY_MEDICAL_RECORD,
            RolePermission.FIND_MEDICAL_NOTE_BY_ID,

            RolePermission.CREATE_ONE_CONSULT,
            RolePermission.UPDATE_ONE_CONSULT,
            RolePermission.DELETE_ONE_CONSULT,
            RolePermission.FIND_ALL_CONSULTS,
            RolePermission.FIND_CONSULTS_BY_PATIENT,
            RolePermission.FIND_CONSULTS_BY_DOCTOR

    )),

    DOCTOR(Arrays.asList(
            RolePermission.FIND_ALL_PATIENTS,

            RolePermission.FIND_SCHEDULES_BY_DOCTOR,

            RolePermission.FIND_APPOINTMENTS_BY_DOCTOR,

            RolePermission.FIND_MEDICAL_RECORDS_BY_PATIENT,
            RolePermission.CREATE_ONE_MEDICAL_RECORD,
            // Medical Notes
            RolePermission.CREATE_ONE_MEDICAL_NOTE,
            RolePermission.FIND_MEDICAL_NOTES_BY_MEDICAL_RECORD,

            // Consults
            RolePermission.CREATE_ONE_CONSULT,
            RolePermission.UPDATE_ONE_CONSULT,
            RolePermission.FIND_CONSULTS_BY_DOCTOR,
            RolePermission.FIND_CONSULTS_BY_PATIENT,

            RolePermission.READ_MY_PROFILE
    )),
    RECEPTIONIST(Arrays.asList(
            RolePermission.READ_MY_PROFILE,

            // Doctors
            RolePermission.FIND_ALL_DOCTORS,
            // Patients
            RolePermission.CREATE_ONE_PATIENT,
            RolePermission.UPDATE_ONE_PATIENT,
            RolePermission.DELETE_ONE_PATIENT,
            RolePermission.FIND_ALL_PATIENTS,

            // Schedules
            RolePermission.FIND_SCHEDULES_BY_DOCTOR,

            // Appointments
            RolePermission.CREATE_ONE_APPOINTMENT,
            RolePermission.UPDATE_ONE_APPOINTMENT,
            RolePermission.DELETE_ONE_APPOINTMENT,
            RolePermission.FIND_ALL_APPOINTMENTS,
            RolePermission.FIND_APPOINTMENTS_BY_PATIENT,
            RolePermission.FIND_APPOINTMENTS_BY_DOCTOR,

            // Medical Records
            RolePermission.CREATE_ONE_MEDICAL_RECORD,
            RolePermission.UPDATE_ONE_MEDICAL_RECORD,
            RolePermission.FIND_MEDICAL_RECORDS_BY_PATIENT,
            RolePermission.FIND_ALL_MEDICAL_RECORDS,

            // Medical Notes
            RolePermission.FIND_MEDICAL_NOTES_BY_MEDICAL_RECORD


    )),

    PATIENT(Arrays.asList(

            RolePermission.READ_MY_PROFILE,
            // Appointments
            RolePermission.FIND_APPOINTMENTS_BY_PATIENT,
            // Medical Records
            RolePermission.FIND_MEDICAL_RECORDS_BY_PATIENT
    ));

    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }

    private List<RolePermission> permissions;

    Role(List<RolePermission> permissions) {
        this.permissions = permissions;
    }


}

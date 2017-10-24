package com.dhdigital.lms.modal;

/**
 * Created by Kiran Bedre on 10/11/15.
 * DarkHorse BOA
 */
public class Employee {
    private Long id;
    private int version;
    private String firstName;
    private String lastName;
    private String employeeNo;
    private MasterData costCentre;
    private String gender;
    private String mobileNumber;
    private String emailAddress;
    private String departmentCode;
    private CityMasterData location;
    private String remarks;
    private MasterData unit;
    private Employee manager;
    private Employee offshoreManager;
    private Request userPreference;
    private MasterData team;
    private Files fileUpload;
    private String[] employeeTravelPreferences;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public MasterData getCostCentre() {
        return costCentre;
    }

    public void setCostCentre(MasterData costCenter) {
        this.costCentre = costCenter;
    }

    @Override
    public String toString() {
        if (employeeNo == null) {
            return (firstName);
        } else {
            return (firstName + " " + lastName + " (" + employeeNo + ")");
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public MasterData getUnit() {
        return unit;
    }

    public void setUnit(MasterData unit) {
        this.unit = unit;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Request getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(Request userPreference) {
        this.userPreference = userPreference;
    }

    public String[] getEmployeeTravelPreferences() {
        return employeeTravelPreferences;
    }

    public void setEmployeeTravelPreferences(String[] employeeTravelPreferences) {
        this.employeeTravelPreferences = employeeTravelPreferences;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Employee getOffshoreManager() {
        return offshoreManager;
    }

    public void setOffshoreManager(Employee offshoreManager) {
        this.offshoreManager = offshoreManager;
    }


    public String getCompleteName() {

        return firstName + " " + lastName;
    }

    public CityMasterData getLocation() {
        return location;
    }

    public void setLocation(CityMasterData location) {
        this.location = location;
    }

    public Files getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(Files fileUpload) {
        this.fileUpload = fileUpload;
    }


    public MasterData getTeam() {
        return team;
    }

    public void setTeam(MasterData team) {
        this.team = team;
    }
}

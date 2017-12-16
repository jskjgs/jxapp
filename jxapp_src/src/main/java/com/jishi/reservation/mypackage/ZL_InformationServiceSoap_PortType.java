/**
 * ZL_InformationServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public interface ZL_InformationServiceSoap_PortType extends java.rmi.Remote {
    public String getOracleSysDate() throws java.rmi.RemoteException;
    public PublicResponsePublicResult _public(String reData) throws java.rmi.RemoteException;
    public OutPatientResponseOutPatientResult outPatient(String reData) throws java.rmi.RemoteException;
    public HospitalizationResponseHospitalizationResult hospitalization(String reData) throws java.rmi.RemoteException;
    public MedicalTechnologyResponseMedicalTechnologyResult medicalTechnology(String reData) throws java.rmi.RemoteException;
    public PhysicalExaminationResponsePhysicalExaminationResult physicalExamination(String reData) throws java.rmi.RemoteException;
    public UserManagerResponseUserManagerResult userManager(String reData) throws java.rmi.RemoteException;
    public InformationResponseInformationResult information(String reData) throws java.rmi.RemoteException;
    public CustomResponseCustomResult custom(String reData) throws java.rmi.RemoteException;
    public InsideBasicResponseInsideBasicResult insideBasic(String reData) throws java.rmi.RemoteException;
    public InsideResponseInsideResult inside(String reData) throws java.rmi.RemoteException;
}

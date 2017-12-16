/**
 * ZL_InformationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class ZL_InformationServiceLocator extends org.apache.axis.client.Service implements ZL_InformationService {

    public ZL_InformationServiceLocator() {
    }


    public ZL_InformationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZL_InformationServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ZL_InformationServiceSoap
    private String ZL_InformationServiceSoap_address = "http://hpx10ddns.xicp.io/ExternalServices/ZL_InformationService.asmx";

    public String getZL_InformationServiceSoapAddress() {
        return ZL_InformationServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private String ZL_InformationServiceSoapWSDDServiceName = "ZL_InformationServiceSoap";

    public String getZL_InformationServiceSoapWSDDServiceName() {
        return ZL_InformationServiceSoapWSDDServiceName;
    }

    public void setZL_InformationServiceSoapWSDDServiceName(String name) {
        ZL_InformationServiceSoapWSDDServiceName = name;
    }

    public ZL_InformationServiceSoap_PortType getZL_InformationServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ZL_InformationServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getZL_InformationServiceSoap(endpoint);
    }

    public ZL_InformationServiceSoap_PortType getZL_InformationServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ZL_InformationServiceSoap_BindingStub _stub = new ZL_InformationServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getZL_InformationServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setZL_InformationServiceSoapEndpointAddress(String address) {
        ZL_InformationServiceSoap_address = address;
    }


    // Use to get a proxy class for ZL_InformationServiceSoap12
    private String ZL_InformationServiceSoap12_address = "http://hpx10ddns.xicp.io/ExternalServices/ZL_InformationService.asmx";
    public String getZL_InformationServiceSoap12Address() {
        return ZL_InformationServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private String ZL_InformationServiceSoap12WSDDServiceName = "ZL_InformationServiceSoap12";

    public String getZL_InformationServiceSoap12WSDDServiceName() {
        return ZL_InformationServiceSoap12WSDDServiceName;
    }

    public void setZL_InformationServiceSoap12WSDDServiceName(String name) {
        ZL_InformationServiceSoap12WSDDServiceName = name;
    }

    public ZL_InformationServiceSoap_PortType getZL_InformationServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ZL_InformationServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getZL_InformationServiceSoap12(endpoint);
    }

    public ZL_InformationServiceSoap_PortType getZL_InformationServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ZL_InformationServiceSoap12Stub _stub = new ZL_InformationServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getZL_InformationServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setZL_InformationServiceSoap12EndpointAddress(String address) {
        ZL_InformationServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ZL_InformationServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ZL_InformationServiceSoap_BindingStub _stub = new ZL_InformationServiceSoap_BindingStub(new java.net.URL(ZL_InformationServiceSoap_address), this);
                _stub.setPortName(getZL_InformationServiceSoapWSDDServiceName());
                return _stub;
            }
            if (ZL_InformationServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ZL_InformationServiceSoap12Stub _stub = new ZL_InformationServiceSoap12Stub(new java.net.URL(ZL_InformationServiceSoap12_address), this);
                _stub.setPortName(getZL_InformationServiceSoap12WSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("ZL_InformationServiceSoap".equals(inputPortName)) {
            return getZL_InformationServiceSoap();
        }
        else if ("ZL_InformationServiceSoap12".equals(inputPortName)) {
            return getZL_InformationServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "ZL_InformationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "ZL_InformationServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "ZL_InformationServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("ZL_InformationServiceSoap".equals(portName)) {
            setZL_InformationServiceSoapEndpointAddress(address);
        }
        else
if ("ZL_InformationServiceSoap12".equals(portName)) {
            setZL_InformationServiceSoap12EndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

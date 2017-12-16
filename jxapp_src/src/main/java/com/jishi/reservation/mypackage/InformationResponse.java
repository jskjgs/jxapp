/**
 * InformationResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class InformationResponse  implements java.io.Serializable {
    private InformationResponseInformationResult informationResult;

    public InformationResponse() {
    }

    public InformationResponse(
           InformationResponseInformationResult informationResult) {
           this.informationResult = informationResult;
    }


    /**
     * Gets the informationResult value for this InformationResponse.
     * 
     * @return informationResult
     */
    public InformationResponseInformationResult getInformationResult() {
        return informationResult;
    }


    /**
     * Sets the informationResult value for this InformationResponse.
     * 
     * @param informationResult
     */
    public void setInformationResult(InformationResponseInformationResult informationResult) {
        this.informationResult = informationResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof InformationResponse)) return false;
        InformationResponse other = (InformationResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.informationResult==null && other.getInformationResult()==null) ||
             (this.informationResult!=null &&
              this.informationResult.equals(other.getInformationResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getInformationResult() != null) {
            _hashCode += getInformationResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InformationResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">InformationResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("informationResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "InformationResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>InformationResponse>InformationResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

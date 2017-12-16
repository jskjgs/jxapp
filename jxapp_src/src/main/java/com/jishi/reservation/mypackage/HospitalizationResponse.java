/**
 * HospitalizationResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class HospitalizationResponse  implements java.io.Serializable {
    private HospitalizationResponseHospitalizationResult hospitalizationResult;

    public HospitalizationResponse() {
    }

    public HospitalizationResponse(
           HospitalizationResponseHospitalizationResult hospitalizationResult) {
           this.hospitalizationResult = hospitalizationResult;
    }


    /**
     * Gets the hospitalizationResult value for this HospitalizationResponse.
     * 
     * @return hospitalizationResult
     */
    public HospitalizationResponseHospitalizationResult getHospitalizationResult() {
        return hospitalizationResult;
    }


    /**
     * Sets the hospitalizationResult value for this HospitalizationResponse.
     * 
     * @param hospitalizationResult
     */
    public void setHospitalizationResult(HospitalizationResponseHospitalizationResult hospitalizationResult) {
        this.hospitalizationResult = hospitalizationResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof HospitalizationResponse)) return false;
        HospitalizationResponse other = (HospitalizationResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.hospitalizationResult==null && other.getHospitalizationResult()==null) ||
             (this.hospitalizationResult!=null &&
              this.hospitalizationResult.equals(other.getHospitalizationResult())));
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
        if (getHospitalizationResult() != null) {
            _hashCode += getHospitalizationResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HospitalizationResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">HospitalizationResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hospitalizationResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "HospitalizationResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>HospitalizationResponse>HospitalizationResult"));
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

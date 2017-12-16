/**
 * OutPatientResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class OutPatientResponse  implements java.io.Serializable {
    private OutPatientResponseOutPatientResult outPatientResult;

    public OutPatientResponse() {
    }

    public OutPatientResponse(
           OutPatientResponseOutPatientResult outPatientResult) {
           this.outPatientResult = outPatientResult;
    }


    /**
     * Gets the outPatientResult value for this OutPatientResponse.
     * 
     * @return outPatientResult
     */
    public OutPatientResponseOutPatientResult getOutPatientResult() {
        return outPatientResult;
    }


    /**
     * Sets the outPatientResult value for this OutPatientResponse.
     * 
     * @param outPatientResult
     */
    public void setOutPatientResult(OutPatientResponseOutPatientResult outPatientResult) {
        this.outPatientResult = outPatientResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof OutPatientResponse)) return false;
        OutPatientResponse other = (OutPatientResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.outPatientResult==null && other.getOutPatientResult()==null) ||
             (this.outPatientResult!=null &&
              this.outPatientResult.equals(other.getOutPatientResult())));
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
        if (getOutPatientResult() != null) {
            _hashCode += getOutPatientResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OutPatientResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">OutPatientResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outPatientResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "OutPatientResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>OutPatientResponse>OutPatientResult"));
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

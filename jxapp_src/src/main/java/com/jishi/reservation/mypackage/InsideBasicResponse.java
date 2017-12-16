/**
 * InsideBasicResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class InsideBasicResponse  implements java.io.Serializable {
    private InsideBasicResponseInsideBasicResult insideBasicResult;

    public InsideBasicResponse() {
    }

    public InsideBasicResponse(
           InsideBasicResponseInsideBasicResult insideBasicResult) {
           this.insideBasicResult = insideBasicResult;
    }


    /**
     * Gets the insideBasicResult value for this InsideBasicResponse.
     * 
     * @return insideBasicResult
     */
    public InsideBasicResponseInsideBasicResult getInsideBasicResult() {
        return insideBasicResult;
    }


    /**
     * Sets the insideBasicResult value for this InsideBasicResponse.
     * 
     * @param insideBasicResult
     */
    public void setInsideBasicResult(InsideBasicResponseInsideBasicResult insideBasicResult) {
        this.insideBasicResult = insideBasicResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof InsideBasicResponse)) return false;
        InsideBasicResponse other = (InsideBasicResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.insideBasicResult==null && other.getInsideBasicResult()==null) ||
             (this.insideBasicResult!=null &&
              this.insideBasicResult.equals(other.getInsideBasicResult())));
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
        if (getInsideBasicResult() != null) {
            _hashCode += getInsideBasicResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InsideBasicResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">InsideBasicResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insideBasicResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "InsideBasicResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>InsideBasicResponse>InsideBasicResult"));
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

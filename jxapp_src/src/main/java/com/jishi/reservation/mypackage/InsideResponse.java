/**
 * InsideResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class InsideResponse  implements java.io.Serializable {
    private InsideResponseInsideResult insideResult;

    public InsideResponse() {
    }

    public InsideResponse(
           InsideResponseInsideResult insideResult) {
           this.insideResult = insideResult;
    }


    /**
     * Gets the insideResult value for this InsideResponse.
     * 
     * @return insideResult
     */
    public InsideResponseInsideResult getInsideResult() {
        return insideResult;
    }


    /**
     * Sets the insideResult value for this InsideResponse.
     * 
     * @param insideResult
     */
    public void setInsideResult(InsideResponseInsideResult insideResult) {
        this.insideResult = insideResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof InsideResponse)) return false;
        InsideResponse other = (InsideResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.insideResult==null && other.getInsideResult()==null) ||
             (this.insideResult!=null &&
              this.insideResult.equals(other.getInsideResult())));
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
        if (getInsideResult() != null) {
            _hashCode += getInsideResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InsideResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">InsideResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insideResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "InsideResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>InsideResponse>InsideResult"));
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

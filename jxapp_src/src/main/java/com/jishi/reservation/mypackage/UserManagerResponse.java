/**
 * UserManagerResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jishi.reservation.mypackage;

public class UserManagerResponse  implements java.io.Serializable {
    private UserManagerResponseUserManagerResult userManagerResult;

    public UserManagerResponse() {
    }

    public UserManagerResponse(
           UserManagerResponseUserManagerResult userManagerResult) {
           this.userManagerResult = userManagerResult;
    }


    /**
     * Gets the userManagerResult value for this UserManagerResponse.
     * 
     * @return userManagerResult
     */
    public UserManagerResponseUserManagerResult getUserManagerResult() {
        return userManagerResult;
    }


    /**
     * Sets the userManagerResult value for this UserManagerResponse.
     * 
     * @param userManagerResult
     */
    public void setUserManagerResult(UserManagerResponseUserManagerResult userManagerResult) {
        this.userManagerResult = userManagerResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof UserManagerResponse)) return false;
        UserManagerResponse other = (UserManagerResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.userManagerResult==null && other.getUserManagerResult()==null) ||
             (this.userManagerResult!=null &&
              this.userManagerResult.equals(other.getUserManagerResult())));
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
        if (getUserManagerResult() != null) {
            _hashCode += getUserManagerResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserManagerResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">UserManagerResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userManagerResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UserManagerResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>UserManagerResponse>UserManagerResult"));
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

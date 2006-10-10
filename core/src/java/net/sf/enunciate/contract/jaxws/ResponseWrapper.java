package net.sf.enunciate.contract.jaxws;

import com.sun.mirror.type.VoidType;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A response wrapper for a web method in document/literal wrapped style.
 *
 * @author Ryan Heaton
 */
public class ResponseWrapper implements WebMessage, WebMessagePart, ImplicitRootElement {

  private WebMethod webMethod;

  /**
   * @param webMethod The web method to wrap.
   */
  protected ResponseWrapper(WebMethod webMethod) {
    this.webMethod = webMethod;
  }

  /**
   * Get the web method to which this response is associated.
   *
   * @return The web method to which this response is associated.
   */
  public WebMethod getWebMethod() {
    return webMethod;
  }

  /**
   * The name of the JAXWS response bean.
   *
   * @return The name of the JAXWS response bean.
   */
  public String getResponseBeanName() {
    String capitalizedName = this.webMethod.getSimpleName();
    capitalizedName = Character.toString(capitalizedName.charAt(0)).toUpperCase() + capitalizedName.substring(1);
    String responseBeanName = this.webMethod.getDeclaringEndpointInterface().getPackage().getQualifiedName() + ".jaxws." + capitalizedName + "Response";

    javax.xml.ws.ResponseWrapper annotation = webMethod.getAnnotation(javax.xml.ws.ResponseWrapper.class);
    if ((annotation != null) && (annotation.className() != null) && (!"".equals(annotation.className()))) {
      responseBeanName = annotation.className();
    }

    return responseBeanName;
  }

  /**
   * The local name of the output.
   *
   * @return The local name of the output.
   */
  public String getElementName() {
    String name = webMethod.getSimpleName() + "Response";

    javax.xml.ws.ResponseWrapper annotation = webMethod.getAnnotation(javax.xml.ws.ResponseWrapper.class);
    if ((annotation != null) && (annotation.localName() != null) && (!"".equals(annotation.localName()))) {
      name = annotation.localName();
    }

    return name;
  }

  /**
   * The local namespace of the output.
   *
   * @return The local namespace of the output.
   */
  public String getElementNamespace() {
    String namespace = webMethod.getDeclaringEndpointInterface().getTargetNamespace();

    javax.xml.ws.ResponseWrapper annotation = webMethod.getAnnotation(javax.xml.ws.ResponseWrapper.class);
    if ((annotation != null) && (annotation.targetNamespace() != null) && (!"".equals(annotation.targetNamespace()))) {
      namespace = annotation.targetNamespace();
    }

    return namespace;
  }

  /**
   * Documentation explaining this is a response wrapper for its method.
   *
   * @return Documentation explaining this is a response wrapper for its method.
   */
  public String getElementDocs() {
    String docs = "doc/lit response wrapper for operation \"" + webMethod.getOperationName() + "\".";
    String methodDocs = webMethod.getJavaDoc().toString();
    if (methodDocs.trim().length() > 0) {
      docs += " (" + methodDocs.trim() + ")";
    }
    return docs;
  }

  /**
   * @return {@link ParticleType#ELEMENT}
   */
  public ParticleType getParticleType() {
    return ParticleType.ELEMENT;
  }

  /**
   * The qname of the response element.
   *
   * @return The qname of the response element.
   */
  public QName getParticleQName() {
    return new QName(getElementNamespace(), getElementName());
  }

  /**
   * @return true.
   */
  public boolean isImplicitSchemaElement() {
    return true;
  }

  /**
   * The schema type for a response wrapper is always anonymous.
   *
   * @return null
   */
  public QName getTypeQName() {
    return null;
  }

  /**
   * The collection of output parameters for this response.
   *
   * @return The collection of output parameters for this response.
   */
  public Collection<ImplicitChildElement> getChildElements() {
    Collection<ImplicitChildElement> childElements = new ArrayList<ImplicitChildElement>();

    if (!(webMethod.getReturnType() instanceof VoidType)) {
      WebResult webResult = webMethod.getWebResult();
      if (!webResult.isHeader()) {
        childElements.add(webResult);
      }
    }

    for (WebParam webParam : webMethod.getWebParameters()) {
      if (webParam.isOutput() && !webParam.isHeader()) {
        childElements.add(webParam);
      }
    }

    return childElements;
  }

  /**
   * There's only one part to a doc/lit response wrapper.
   *
   * @return this.
   */
  public Collection<WebMessagePart> getParts() {
    return new ArrayList<WebMessagePart>(Arrays.asList(this));
  }

  /**
   * @return false
   */
  public boolean isInput() {
    return false;
  }

  /**
   * @return true
   */
  public boolean isOutput() {
    return true;
  }

  /**
   * @return false
   */
  public boolean isHeader() {
    return false;
  }

  /**
   * @return false
   */
  public boolean isFault() {
    return false;
  }

  /**
   * The simple name of the method appended with "Response".
   *
   * @return The simple name of the method appended with "Response".
   */
  public String getMessageName() {
    return webMethod.getDeclaringEndpointInterface().getSimpleName() + "." + webMethod.getSimpleName() + "Response";
  }

  /**
   * Documentation explaining this is a response message for its method.
   *
   * @return Documentation explaining this is a response message for its method.
   */
  public String getMessageDocs() {
    String docs = "response message for operation \"" + webMethod.getOperationName() + "\".";
    String methodDocs = webMethod.getJavaDoc().toString();
    if (methodDocs.trim().length() > 0) {
      docs += " (" + methodDocs.trim() + ")";
    }
    return docs;
  }

  /**
   * @return null
   */
  public String getPartDocs() {
    return null;
  }

  /**
   * The simple name of the method appended with "Response".
   *
   * @return The simple name of the method appended with "Response".
   */
  public String getPartName() {
    return webMethod.getSimpleName() + "Response";
  }

}

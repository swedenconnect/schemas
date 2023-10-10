/*
 * Copyright 2023 Sweden Connect
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.swedenconnect.xml.jaxb;

import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;
import org.w3c.dom.Document;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import se.swedenconnect.xml.CustomNamespaceMapper;
import se.swedenconnect.xml.DOMUtils;

/**
 * Utility class for marshalling of JAXB objects.
 *
 * @author Martin Lindström (martin@idsec.se)
 * @author Stefan Santesson (stefan@idsec.se)
 */
public class JAXBMarshaller {

  /** Namespace prefix mapper. */
  private static NamespacePrefixMapper namespacePrefixMapper = new CustomNamespaceMapper();

  /**
   * Marshalls the supplied JAXB object into a DOM document.
   * <p>
   * Note: elements not having the {@code XmlRootElement} annotation will not be possible to marshall using this method.
   * Instead use the {@link #marshallNonRootElement(JAXBElement)}.
   * </p>
   *
   * @param jaxbObject the object to marshall
   * @return the DOM document
   * @throws JAXBException for marshalling errors
   */
  public static Document marshall(final Object jaxbObject) throws JAXBException {
    final Document document = DOMUtils.createDocumentBuilder().newDocument();
    final JAXBContext context = JAXBContextUtils.createJAXBContext(jaxbObject.getClass());
    final Marshaller marshaller = context.createMarshaller();

    // See https://eclipse-ee4j.github.io/jaxb-ri/3.0.0/docs/ch05.html#section-872160760955562
    marshaller.setProperty("org.glassfish.jaxb.namespacePrefixMapper", namespacePrefixMapper);
    //marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper);
    marshaller.marshal(jaxbObject, document);
    return document;
  }

  /**
   * Marshalls a JAXB object that is not an XML root element (i.e. it is not annotated with {@code XmlRootElement}). In
   * order to get a {@code JAXBElement} for the object use the {@code ObjectFactory}'s create method.
   *
   * @param jaxbElement the element to marshall
   * @return the DOM document
   * @throws JAXBException for marshalling errors
   */
  public static Document marshallNonRootElement(final JAXBElement<?> jaxbElement) throws JAXBException {
    final Document document = DOMUtils.createDocumentBuilder().newDocument();
    final JAXBContext context = JAXBContextUtils.createJAXBContext(jaxbElement.getDeclaredType());
    final Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty("org.glassfish.jaxb.namespacePrefixMapper", namespacePrefixMapper);
    //marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper);
    marshaller.marshal(jaxbElement, document);
    return document;
  }

  // Hidden constructor
  private JAXBMarshaller() {
  }

}

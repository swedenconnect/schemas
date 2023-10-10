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

import org.w3c.dom.Node;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility class for JAXB unmarshalling.
 *
 * @author Martin Lindström (martin@idsec.se)
 * @author Stefan Santesson (stefan@idsec.se)
 */
public class JAXBUnmarshaller {

  /**
   * Utility method for unmarshalling a DOM node into a JAXB object.
   *
   * @param node the DOM node
   * @param clazz the type of the resulting JAXB object
   * @return a JAXB object of type T
   * @throws JAXBException for unmarshalling errors
   */
  public static <T> T unmarshall(final Node node, final Class<T> clazz) throws JAXBException {
    final JAXBContext context = JAXBContextUtils.createJAXBContext(clazz);
    final Unmarshaller unmarshaller = context.createUnmarshaller();
    final Object jaxbObject = unmarshaller.unmarshal(node);
    if (JAXBElement.class.isInstance(jaxbObject)) {
      final JAXBElement<?> jaxbElm = JAXBElement.class.cast(jaxbObject);
      return clazz.cast(jaxbElm.getValue());
    }
    else {
      return clazz.cast(jaxbObject);
    }
  }

  // Hidden constructor
  private JAXBUnmarshaller() {
  }

}

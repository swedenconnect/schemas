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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import se.swedenconnect.schemas.csig.dssext_1_1.SignMessage;
import se.swedenconnect.schemas.saml_2_0.assertion.EncryptedElementType;
import se.swedenconnect.schemas.saml_2_0.assertion.NameIDType;

/**
 * Test cases for JAXBMarshaller.
 *
 * @author Martin Lindström (martin@idsec.se)
 * @author Stefan Santesson (stefan@idsec.se)
 */
public class JAXBMarshallerTest {

  @Test
  public void testMarshallRootElement() throws Exception {
    final se.swedenconnect.schemas.csig.dssext_1_1.ObjectFactory f =
        new se.swedenconnect.schemas.csig.dssext_1_1.ObjectFactory();
    final SignMessage sm = f.createSignMessage();
    sm.setDisplayEntity("abc");

    final Document doc = JAXBMarshaller.marshall(sm);
    Assertions.assertEquals("SignMessage", doc.getDocumentElement().getLocalName());
    Assertions.assertEquals("csig", doc.getDocumentElement().getPrefix());

    // A non-root element should not work ...
    final EncryptedElementType ee = new EncryptedElementType();
    Assertions.assertThrows(JAXBException.class, () -> {
      JAXBMarshaller.marshall(ee);
    });

  }

  @Test
  public void testMarshallNonRootElement() throws Exception {
    final se.swedenconnect.schemas.saml_2_0.assertion.ObjectFactory f =
        new se.swedenconnect.schemas.saml_2_0.assertion.ObjectFactory();

    final NameIDType type = new NameIDType();
    type.setFormat("test");

    final JAXBElement<NameIDType> e = f.createNameID(type);
    final Document doc = JAXBMarshaller.marshallNonRootElement(e);
    Assertions.assertNotNull(doc.getDocumentElement());
  }

}

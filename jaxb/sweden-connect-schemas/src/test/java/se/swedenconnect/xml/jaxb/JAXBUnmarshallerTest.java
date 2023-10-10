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

import java.io.ByteArrayInputStream;

import org.apache.xml.security.utils.ClassLoaderUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.swedenconnect.schemas.csig.dssext_1_1.SignMessage;
import se.swedenconnect.schemas.saml_2_0.assertion.NameIDType;
import se.swedenconnect.xml.DOMUtils;

/**
 * Test cases for JAXBUnmarshaller.
 *
 * @author Martin Lindström (martin@idsec.se)
 * @author Stefan Santesson (stefan@idsec.se)
 */
public class JAXBUnmarshallerTest {

  private static final String SM =
      "<csig:SignMessage xmlns:csig=\"http://id.elegnamnden.se/csig/1.1/dss-ext/ns\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\" xmlns:ns5=\"http://www.w3.org/2004/08/xop/include\" xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" DisplayEntity=\"abc\"/>";

  @Test
  public void testUnmarshall() throws Exception {
    Document doc = DOMUtils.inputStreamToDocument(new ByteArrayInputStream(SM.getBytes()));
    final SignMessage sm = JAXBUnmarshaller.unmarshall(doc, SignMessage.class);
    Assertions.assertNotNull(sm);
    Assertions.assertEquals("abc", sm.getDisplayEntity());

    // Unmarshall a non-root element
    //
    doc = DOMUtils
        .inputStreamToDocument(ClassLoaderUtils.getResourceAsStream("/assertion.xml", JAXBUnmarshallerTest.class));
    final Element nameIDelm = (Element) doc.getDocumentElement()
        .getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Subject").item(0)
        .getChildNodes().item(1);

    final NameIDType nameID = JAXBUnmarshaller.unmarshall(nameIDelm, NameIDType.class);
    Assertions.assertNotNull(nameID);
    Assertions.assertEquals("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent", nameID.getFormat());
  }
}

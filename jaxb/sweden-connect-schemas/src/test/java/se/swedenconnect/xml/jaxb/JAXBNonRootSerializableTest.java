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

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBElement;
import se.swedenconnect.schemas.saml_2_0.assertion.NameIDType;

/**
 * Test cases for JAXBNonRootSerializable.
 *
 * @author Martin Lindström
 */
public class JAXBNonRootSerializableTest {

  @Test
  public void testSerializeDeserialize() {

    final TestObject obj1 = new TestObject("sample text", "FORMAT");

    final TestObject obj2 = SerializationUtils.roundtrip(obj1);

    Assertions.assertEquals(obj1.getText(), obj2.getText());
    Assertions.assertEquals(obj1.getNameId().getFormat(), obj2.getNameId().getFormat());
    Assertions.assertEquals("FORMAT", obj2.getNameId().getFormat());
  }

  @Test
  public void testSerializeDeserializeNoValue() {

    final TestObject obj1 = new TestObject("sample text");

    final TestObject obj2 = SerializationUtils.roundtrip(obj1);

    Assertions.assertEquals(obj1.getText(), obj2.getText());
    Assertions.assertNull(obj1.getNameId());
    Assertions.assertNull(obj2.getNameId());
  }

  public static class TestObject implements Serializable {

    private static final long serialVersionUID = -56880659229972600L;

    private String text;

    private JAXBNonRootSerializable<NameIDType> nameId;

    public TestObject(final String text, final String nameIdFormat) {
      this.text = text;

      final se.swedenconnect.schemas.saml_2_0.assertion.ObjectFactory f =
          new se.swedenconnect.schemas.saml_2_0.assertion.ObjectFactory();

      final NameIDType type = new NameIDType();
      type.setFormat(nameIdFormat);

      final JAXBElement<NameIDType> elm = f.createNameID(type);

      this.nameId = new JAXBNonRootSerializable<NameIDType>(elm);
    }

    public TestObject(final String text) {
      this.text = text;
      final JAXBElement<NameIDType> elm = new JAXBElement<>(
          new QName("urn:oasis:names:tc:SAML:2.0:assertion", "NameID"),
          NameIDType.class, null, null);

      this.nameId = new JAXBNonRootSerializable<NameIDType>(elm);
    }

    public String getText() {
      return this.text;
    }

    public NameIDType getNameId() {
      return this.nameId.get();
    }

  }

}

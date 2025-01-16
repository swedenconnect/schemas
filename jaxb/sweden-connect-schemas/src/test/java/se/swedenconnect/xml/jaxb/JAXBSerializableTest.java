/*
 * Copyright 2017-2025 Sweden Connect
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

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import se.swedenconnect.schemas.csig.dssext_1_1.SignMessage;

/**
 * Test cases for JAXBSerializable.
 *
 * @author Martin Lindström
 */
public class JAXBSerializableTest {

  @Test
  public void testSerializeDeserialize() {

    final TestObject obj1 = new TestObject("sample text", "DISPLAY");

    final TestObject obj2 = SerializationUtils.roundtrip(obj1);

    Assertions.assertEquals(obj1.getText(), obj2.getText());
    Assertions.assertEquals(obj1.getSignMessage().getDisplayEntity(), obj2.getSignMessage().getDisplayEntity());
    Assertions.assertEquals("DISPLAY", obj2.getSignMessage().getDisplayEntity());
  }

  public static class TestObject implements Serializable {

    private static final long serialVersionUID = -56880659229972600L;

    private String text;

    private JAXBSerializable<SignMessage> signMessage;

    public TestObject(final String text, final String display) {
      this.text = text;

      final se.swedenconnect.schemas.csig.dssext_1_1.ObjectFactory f =
          new se.swedenconnect.schemas.csig.dssext_1_1.ObjectFactory();
      final SignMessage sm = f.createSignMessage();
      sm.setDisplayEntity(display);

      this.signMessage = new JAXBSerializable<SignMessage>(sm, SignMessage.class);
    }

    public String getText() {
      return this.text;
    }

    public SignMessage getSignMessage() {
      return this.signMessage.get();
    }

  }

}

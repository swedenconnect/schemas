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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

import org.w3c.dom.Document;

import jakarta.xml.bind.JAXBElement;

/**
 * Utility class for storing JAXB non-root objects in a serializable manner.
 *
 * @param <T> the type of object being stored
 *
 * @author Martin Lindström
 */
public class JAXBNonRootSerializable<T> implements Serializable {

  private static final long serialVersionUID = 2716621745203621874L;

  /** The class of the wrapped object. */
  private Class<T> clazz;

  /** The JAXB element. */
  private JAXBElement<T> jaxb;

  /** Whether the JAXB element has a value. */
  private boolean hasValue;

  /** Whether the nil content flag is set. */
  private boolean nilContent;

  /**
   * Constructor.
   *
   * @param object the JAXB element
   */
  public JAXBNonRootSerializable(final JAXBElement<T> object) {
    this.jaxb = Objects.requireNonNull(object, "JAXB element to serialize must not be null");
    this.clazz = this.jaxb.getDeclaredType();

    this.hasValue = this.jaxb.getValue() != null;
    this.nilContent = this.jaxb.isNil();
  }

  /**
   * Gets the value of the {@link JAXBElement}
   *
   * @return the value of the {@link JAXBElement}
   */
  public T get() {
    return this.jaxb.getValue();
  }

  /**
   * For serialization of the object.
   *
   * @param out the output stream
   * @throws IOException for errors
   */
  private void writeObject(final ObjectOutputStream out) throws IOException {
    try {

      byte[] bytes = null;
      if (this.hasValue) {
        final Document document = JAXBMarshaller.marshallNonRootElement(this.jaxb);
        bytes = DOMUtils.nodeToBytes(document);
      }

      final T value = this.jaxb.getValue();
      this.jaxb.setValue(null);

      final boolean isNil = this.jaxb.isNil();
      this.jaxb.setNil(true);

      out.defaultWriteObject();

      this.jaxb.setValue(value);
      this.jaxb.setNil(isNil);

      if (bytes != null) {
        out.writeObject(bytes);
      }
    }
    catch (final Exception e) {
      throw new IOException("Could not marshall JAXB object", e);
    }
  }

  /**
   * For deserialization of the object.
   *
   * @param in the input stream
   * @throws IOException for errors
   * @throws ClassNotFoundException not thrown by this method
   */
  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    try {
      in.defaultReadObject();

      this.jaxb.setNil(this.nilContent);

      if (this.hasValue) {
        final byte[] bytes = (byte[]) in.readObject();
        final Document document = DOMUtils.bytesToDocument(bytes);

        final T object = JAXBUnmarshaller.unmarshall(document, this.clazz);
        this.jaxb.setValue(object);
      }
    }
    catch (final Exception e) {
      throw new IOException("Could not restore JAXB object", e);
    }
  }

}

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

import org.w3c.dom.Document;

import jakarta.xml.bind.JAXBElement;

/**
 * Utility class for storing JAXB root objects in a serializable manner.
 *
 * @param <T> the type of object being stored
 *
 * @author Martin Lindström
 */
public class JAXBSerializable<T> implements Serializable {

  private static final long serialVersionUID = 3026775002136208779L;

  /** The class of the wrapped object. */
  private Class<T> clazz;

  /** The object that we wrap. */
  private transient T object;

  /**
   * Constructor.
   *
   * @param object the object
   * @param clazz the object class
   */
  public JAXBSerializable(final T object, final Class<T> clazz) {
    this.object = Objects.requireNonNull(object, "JAXB object to serialize must not be null");
    this.clazz = Objects.requireNonNull(clazz, "JAXB object to serialize must not be null");

    if (JAXBElement.class.isInstance(object)) {
      throw new IllegalArgumentException("Object is a JAXBElement - use JAXBNonRootSerializable instead");
    }
  }

  @SuppressWarnings("unused")
  private JAXBSerializable() {
  }

  /**
   * Gets the JAXB object.
   *
   * @return the JAXB object
   */
  public T get() {
    return this.object;
  }

  /**
   * For serialization of the object.
   *
   * @param out the output stream
   * @throws IOException for errors
   */
  private void writeObject(final ObjectOutputStream out) throws IOException {
    try {
      out.defaultWriteObject();

      final Document document = JAXBMarshaller.marshall(this.object);
      final byte[] bytes = DOMUtils.nodeToBytes(document);
      out.writeObject(bytes);
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

      final byte[] bytes = (byte[]) in.readObject();
      final Document document = DOMUtils.bytesToDocument(bytes);

      this.object = JAXBUnmarshaller.unmarshall(document, this.clazz);
    }
    catch (final Exception e) {
      throw new IOException("Could not restore JAXB object", e);
    }
  }

}

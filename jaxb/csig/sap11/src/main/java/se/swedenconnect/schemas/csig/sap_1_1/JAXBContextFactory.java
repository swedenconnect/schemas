/*
 * Copyright 2020-2023 Sweden Connect AB
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
package se.swedenconnect.schemas.csig.sap_1_1;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

/**
 * Utility class that helps creating a {@code JAXBContext} instance to be used for marshalling and unmarshalling objects
 * from this package.
 *
 * @author Martin Lindström (martin@litsec.se)
 */
public class JAXBContextFactory {

  /**
   * Packages holding JAXB classes for the schema imports of the schema that is implemented by the classes of this
   * package.
   */
  private static final String[] DEPENDENT_PACKAGES = {};

  /**
   * Gets the packages holding JAXB classes for the schema imports of the schema that is implemented by the classes of
   * this package.
   *
   * @return an array of package names
   */
  public static String[] getDependentPackages() {
    return DEPENDENT_PACKAGES;
  }

  /**
   * Creates a {@code JAXBContext} that can be used to marshall/unmarshall an object from this package.
   *
   * @return a JAXBContext
   * @throws JAXBException for error creating the context
   */
  public static JAXBContext createContext() throws JAXBException {
    StringBuffer sb = new StringBuffer(JAXBContext.class.getPackage().getName());
    for (String p : DEPENDENT_PACKAGES) {
      sb.append(':').append(p);
    }
    return JAXBContext.newInstance(sb.toString());
  }

}

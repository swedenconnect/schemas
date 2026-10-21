/*
 * Copyright 2017-2026 Sweden Connect
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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Test cases for the {@code JAXBContextFactory} classes found in each generated package.
 *
 * @author Martin Lindström (martin@idsec.se)
 */
public class JAXBContextFactoryTest {

  @ParameterizedTest
  @ValueSource(strings = {
      "se.swedenconnect.schemas.saml_2_0.assertion",
      "se.swedenconnect.schemas.saml_1_1.assertion",
      "se.swedenconnect.schemas.dss_1_0",
      "se.swedenconnect.schemas.etsi.xades_1_3_2",
      "se.swedenconnect.schemas.etsi.xades_1_4_1",
      "se.swedenconnect.schemas.saml.mdext",
      "se.swedenconnect.schemas.saml.psc",
      "se.swedenconnect.schemas.saml.umsg",
      "se.swedenconnect.schemas.csig.dssext_1_1",
      "se.swedenconnect.schemas.csig.sap_1_1",
      "se.swedenconnect.schemas.cert.authcont.saci_1_0",
      "se.swedenconnect.schemas.cert.authcont.ext_auth_info_1_0"
  })
  public void testCreateContext(final String packageName) throws Exception {
    final Class<?> factory = Class.forName(packageName + ".JAXBContextFactory");
    final JAXBContext context = (JAXBContext) factory.getMethod("createContext").invoke(null);
    Assertions.assertNotNull(context);

    // Make sure that the context knows about the root elements of the package itself. Packages
    // that only declare simple type elements have no root element classes to check.
    final Optional<Object> element = createRootElement(packageName);
    if (element.isPresent()) {
      Assertions.assertTrue(context.createJAXBIntrospector().isElement(element.get()),
          "Context does not know about " + element.get().getClass().getName());
    }
  }

  private static Optional<Object> createRootElement(final String packageName) throws Exception {
    final Class<?> objectFactory = Class.forName(packageName + ".ObjectFactory");
    final Optional<Method> method = Arrays.stream(objectFactory.getMethods())
        .filter(m -> m.getName().startsWith("create") && m.getParameterCount() == 0)
        .filter(m -> m.getReturnType().isAnnotationPresent(XmlRootElement.class))
        .findFirst();
    if (method.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(method.get().invoke(objectFactory.getDeclaredConstructor().newInstance()));
  }

}

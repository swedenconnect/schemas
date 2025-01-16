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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

/**
 * Test cases for {@code JAXBContextUtils}.
 *
 * @author Martin Lindström (martin@idsec.se)
 * @author Stefan Santesson (stefan@idsec.se)
 */
public class JAXBContextUtilsTest {

  @Test
  public void testGetPackageNames() throws Exception {
    Assertions.assertEquals(buildPackageString("org.apache.xml.security.binding.xmlenc11",
        new String[] { "org.apache.xml.security.binding.xmldsig", "org.apache.xml.security.binding.xmlenc" }),
        JAXBContextUtils.getPackageNames(org.apache.xml.security.binding.xmlenc11.ConcatKDFParamsType.class));

    Assertions.assertEquals(buildPackageString("org.apache.xml.security.binding.xmlenc",
        new String[] { "org.apache.xml.security.binding.xmldsig" }),
        JAXBContextUtils.getPackageNames(org.apache.xml.security.binding.xmlenc.EncryptionMethodType.class));

    Assertions.assertEquals(buildPackageString("org.apache.xml.security.binding.xmldsig11",
        new String[] { "org.apache.xml.security.binding.xmldsig" }),
        JAXBContextUtils.getPackageNames(org.apache.xml.security.binding.xmldsig11.ECValidationDataType.class));

    Assertions.assertEquals(buildPackageString("org.apache.xml.security.binding.xmldsig", new String[] {}),
        JAXBContextUtils.getPackageNames(org.apache.xml.security.binding.xmldsig.SignatureType.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.cert.authcont.saci_1_0",
        se.swedenconnect.schemas.cert.authcont.saci_1_0.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.cert.authcont.saci_1_0.SAMLAuthContext.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.csig.dssext_1_1",
        se.swedenconnect.schemas.csig.dssext_1_1.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.csig.dssext_1_1.CertRequestProperties.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.etsi.xades_1_4_1",
        se.swedenconnect.schemas.etsi.xades_1_4_1.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.etsi.xades_1_4_1.RecomputedDigestValue.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.etsi.xades_1_3_2",
        se.swedenconnect.schemas.etsi.xades_1_3_2.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.etsi.xades_1_3_2.CounterSignature.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.cert.authcont.ext_auth_info_1_0",
        se.swedenconnect.schemas.cert.authcont.ext_auth_info_1_0.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.cert.authcont.ext_auth_info_1_0.ExtAuthInfo.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.dss_1_0",
        se.swedenconnect.schemas.dss_1_0.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.dss_1_0.AttachmentReference.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.saml_1_1.assertion",
        se.swedenconnect.schemas.saml_1_1.assertion.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.saml_1_1.assertion.Attribute.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.saml_2_0.assertion",
        se.swedenconnect.schemas.saml_2_0.assertion.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.saml_2_0.assertion.AttributeStatement.class));

    Assertions.assertEquals(buildPackageString("se.swedenconnect.schemas.csig.sap_1_1",
        se.swedenconnect.schemas.csig.sap_1_1.JAXBContextFactory.getDependentPackages()),
        JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.csig.sap_1_1.SADRequest.class));

    // Verify that caching works
    final String d1 = JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.csig.sap_1_1.SADRequest.class);
    final String d2 = JAXBContextUtils.getPackageNames(se.swedenconnect.schemas.csig.sap_1_1.SADRequest.class);
    Assertions.assertTrue(d1 == d2);

    // No registered dependent package names ...
    final String e = JAXBContextUtils.getPackageNames(java.lang.Integer.class);
    Assertions.assertEquals("java.lang", e);
  }

  @Test
  public void testCreateJAXBContext() throws Exception {
    final JAXBContext c =
        JAXBContextUtils.createJAXBContext(se.swedenconnect.schemas.saml_2_0.assertion.AttributeStatement.class);
    Assertions.assertNotNull(c);
    Assertions.assertNotNull(c.createMarshaller());
    Assertions.assertNotNull(c.createUnmarshaller());

    Assertions.assertThrows(JAXBException.class, () -> {
      JAXBContextUtils.createJAXBContext(Integer.class);
    });
  }

  private static String buildPackageString(final String pkg, final String[] dependencies) {
    final StringBuffer sb = new StringBuffer(pkg);
    if (dependencies != null) {
      for (final String p : dependencies) {
        sb.append(':').append(p);
      }
    }
    return sb.toString();
  }

}

![Logo](https://docs.swedenconnect.se/schemas/img/sweden-connect.png)

------

# Workaround for JAXB and Apache XML Security

**NOTE**: No longer needed. Newer versions of xmlsec contains the sun-jaxb.episode file.

Apache XML Security (org.apache.santurio:xmlsec) contains Java classes for XMLDSig and XMLEnc and up until version 2.0.10 the xmlsec JAR also exposed an episode file (sun-jaxb.episode). Because of issues with the maven-jaxb2-plugin for Java 9 they no longer generate the Java classes for XMLDSig and XMLEnc from XSD-files, but have them checked in instead.

For most people this doesn't matter, but if we are to generate Java classes from other schemas that include XMLDSig och XMLEnc it is a problem. In fact, there were a problem even before because of the
"SCD x-schema::tns didnt match any schema component"-bug (see https://github.com/highsource/maven-jaxb2-plugin/issues/41).

Therefore, we deliver an empty jar with its only purpose of delivering a sun-jaxb.episode file.



Copyright &copy; 2017-2022, [Sweden Connect](https://swedenconnect.se). Licensed under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).